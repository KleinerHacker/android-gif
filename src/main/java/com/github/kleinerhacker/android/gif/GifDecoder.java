package com.github.kleinerhacker.android.gif;

import android.graphics.Bitmap;
import com.google.common.io.LittleEndianDataInputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Internal GIF decoder to read in the GIF
 */
final class GifDecoder {

    private static final int MAX_STACK_SIZE = 4096;

    public static Gif decode(InputStream is) throws IOException {
        final LittleEndianDataInputStream in = new LittleEndianDataInputStream(is);

        final GifHeader header = readHeader(in);
        final GifContent content = readContent(header, in);

        final Gif gif = new Gif(header.getWidth(), header.getHeight(), content.getLoopCount(), header.getPixelAspectRatio(), header.getColorTable());
        gif.getFrameList().addAll(content.getFrameList());

        return gif;
    }

    private static GifHeader readHeader(DataInput in) throws IOException {
        final GifHeader header = new GifHeader();

        //Magic byte test
        final byte[] magicBytes = new byte[6];
        in.readFully(magicBytes);
        if (!new String(magicBytes, "ASCII").startsWith("GIF"))
            throw new IOException("Unable to find magic bytes of GIF: Input file is not a GIF file!");

        header.setWidth(in.readShort()); // 2 (Width)
        header.setHeight(in.readShort()); // 2 (Height)

        final GifPackedInformation packedInformation = readPackedInformation(in);// 1 (Packaged Info)
        final int backgroundIndex = in.readUnsignedByte(); // 1 (Background Index)
        header.setPixelAspectRatio(in.readUnsignedByte()); // 1 (Pixel Aspect Ratio)
        if (packedInformation.isUseColorTable()) {
            header.setColorTable(readColorTable(packedInformation.getColorTableSize(), in)); // x (Color Table)
            header.setBackgroundColor(header.getColors()[backgroundIndex]);
        }

        return header;
    }

    private static GifColorTable readColorTable(int globalColorTableSize, DataInput in) throws IOException {
        final int bytesCount = globalColorTableSize * 3;
        final byte[] contentBytes = new byte[bytesCount];
        in.readFully(contentBytes);

        final int[] colorTable = new int[256];
        int i = 0, j = 0;
        while (i < globalColorTableSize) {
            final int r = ((int) contentBytes[j++]) & 0xff;
            final int g = ((int) contentBytes[j++]) & 0xff;
            final int b = ((int) contentBytes[j++]) & 0xff;
            colorTable[i++] = 0xff000000 | (r << 16) | (g << 8) | b;
        }

        return new GifColorTable(colorTable);
    }

    private static GifContent readContent(GifHeader header, DataInput in) throws IOException {
        final GifContent content = new GifContent();
        final GifExtensionMap extensionMap = new GifExtensionMap();

        GifFrame lastFrame = null, beforeLastFrame = null;
        loop:
        while (true) {
            final int code = in.readUnsignedByte();
            final GifContentType type = GifContentType.fromCode((byte)code);
            switch (type) {
                case Image:
                    final GifFrame frame = readFrame(header, extensionMap, lastFrame, beforeLastFrame, in);
                    content.getFrameList().add(frame);
                    beforeLastFrame = lastFrame;
                    lastFrame = frame;
                    break;
                case Extension:
                    final GifExtension extension = readExtension(in);
                    if (extension != null) {
                        extensionMap.put(extension);
                        if (extension instanceof GifApplicationNetscape20Extension) {
                            content.setLoopCount(((GifApplicationNetscape20Extension) extension).getLoopCount());
                        }
                    }
                    break;
                case Terminator:
                    break loop;
                case Other:
                default:
                    throw new IOException("Unknown GIF code detected: " +
                            StringUtils.leftPad(Integer.toHexString(code), 2, '0'));
            }
        }

        return content;
    }

    private static GifFrame readFrame(GifHeader header, GifExtensionMap extensionMap, GifFrame lastFrame, GifFrame beforeLastFrame, DataInput in) throws IOException {
        final int left = in.readShort();
        final int top = in.readShort();
        final int width = in.readShort();
        final int height = in.readShort();

        final GifPackedInformation packedInformation = readPackedInformation(in);
        final GifColorTable colorTable;
        if (packedInformation.isUseColorTable()) {
            //Local color table
            colorTable = readColorTable(packedInformation.getColorTableSize(), in);
        } else {
            //Copy of global color table
            colorTable = header.getColorTable().copy();
        }

        //Update transparency
        final GifColorTable actualColorTable;
        if (extensionMap.isTransparency()) {
            actualColorTable = new GifTransparencyColorTable(colorTable, extensionMap.getTransparentIndex());
        } else {
            actualColorTable = colorTable;
        }

        final byte[] bitmapBytes = decodeBitmapData(header.getWidth(), header.getHeight(), in);
        skipBlocks(in);

        final Bitmap bitmap = buildBitmap(left, top, width, height, header, actualColorTable, extensionMap,
                packedInformation.isInterlace(), lastFrame, beforeLastFrame, bitmapBytes);

        return new GifFrame(
                new GifMetadata(left, top, width, height, extensionMap.getDelay(), extensionMap.isTransparency(),
                        extensionMap.isTransparency() ? ((GifTransparencyColorTable)actualColorTable).getTransparentColor() : -1,
                        extensionMap.getDispose()),
                colorTable, bitmap
        );
    }

    private static Bitmap buildBitmap(int left, int top, int width, int height, GifHeader header, GifColorTable colorTable,
                                      GifExtensionMap extensionMap, boolean interlace, GifFrame lastFrame, GifFrame beforeLastFrame,
                                      byte[] pixels) {
        // expose destination image's pixels as int array
        int[] dest = new int[header.getWidth() * header.getHeight()];
        // fill in starting image contents based on last image's dispose code
        if (lastFrame != null && lastFrame.getMetadata().getDispose() > 0) {
            final Bitmap lastBitmap;
            if (lastFrame.getMetadata().getDispose() == 3) {
                lastBitmap = beforeLastFrame == null ? null : beforeLastFrame.getImage();
            } else {
                lastBitmap = lastFrame == null ? null : lastFrame.getImage();
            }
            if (lastBitmap != null) {
                lastBitmap.getPixels(dest, 0, header.getWidth(), 0, 0, header.getWidth(), header.getHeight());
                // copy pixels
                if (lastFrame.getMetadata().getDispose() == 2) {
                    // fill last image rect area with background color
                    int c = 0;
                    if (!extensionMap.isTransparency()) {
                        c = header.getBackgroundColor();
                    }
                    for (int i = 0; i < lastFrame.getMetadata().getHeight(); i++) {
                        int n1 = (lastFrame.getMetadata().getTop() + i) * header.getWidth() + lastFrame.getMetadata().getLeft();
                        int n2 = n1 + lastFrame.getMetadata().getWidth();
                        for (int k = n1; k < n2; k++) {
                            dest[k] = c;
                        }
                    }
                }
            }
        }
        // copy each source line to the appropriate place in the destination
        int pass = 1;
        int inc = 8;
        int iline = 0;
        for (int i = 0; i < height; i++) {
            int line = i;
            if (interlace) {
                if (iline >= height) {
                    pass++;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            inc = 4;
                            break;
                        case 4:
                            iline = 1;
                            inc = 2;
                            break;
                        default:
                            break;
                    }
                }
                line = iline;
                iline += inc;
            }
            line += top;
            if (line < header.getHeight()) {
                int k = line * header.getWidth();
                int dx = k + left; // start of line in dest
                int dlim = dx + width; // end of dest line
                if ((k + header.getWidth()) < dlim) {
                    dlim = k + header.getHeight(); // past dest edge
                }
                int sx = i * width; // start of line in source
                while (dx < dlim) {
                    // map color and insert in destination
                    int index = ((int) pixels[sx++]) & 0xff;
                    int c = colorTable.getColors()[index];
                    if (c != 0) {
                        dest[dx] = c;
                    }
                    dx++;
                }
            }
        }

        return Bitmap.createBitmap(dest, header.getWidth(), header.getHeight(), Bitmap.Config.ARGB_4444);
    }

    private static byte[] decodeBitmapData(int width, int height, DataInput in) throws IOException {
        int nullCode = -1;
        int pixelsCount = width * height;
        int available, clear, code_mask, code_size, end_of_information, in_code, old_code, bits, code, count, i, datum, data_size, first, top, bi, pi;
        final byte[] pixels = new byte[pixelsCount];
        final short[] prefix = new short[MAX_STACK_SIZE];
        final byte[] suffix = new byte[MAX_STACK_SIZE];
        final byte[] pixelStack = new byte[MAX_STACK_SIZE + 1];
        // Initialize GIF data stream decoder.
        data_size = in.readByte();
        clear = 1 << data_size;
        end_of_information = clear + 1;
        available = clear + 2;
        old_code = nullCode;
        code_size = data_size + 1;
        code_mask = (1 << code_size) - 1;
        for (code = 0; code < clear; code++) {
            prefix[code] = 0; // XXX ArrayIndexOutOfBoundsException
            suffix[code] = (byte) code;
        }
        // Decode GIF pixel stream.
        datum = bits = count = first = top = pi = bi = 0;
        byte[] blockBuffer = new byte[256];
        for (i = 0; i < pixelsCount; ) {
            if (top == 0) {
                if (bits < code_size) {
                    // Load bytes until there are enough bits for a code.
                    if (count == 0) {
                        // Read a new data block.
                        blockBuffer = readBlock(in);
                        count = blockBuffer.length;
                        if (count <= 0) {
                            break;
                        }
                        bi = 0;
                    }
                    datum += (((int) blockBuffer[bi]) & 0xff) << bits;
                    bits += 8;
                    bi++;
                    count--;
                    continue;
                }
                // Get the next code.
                code = datum & code_mask;
                datum >>= code_size;
                bits -= code_size;
                // Interpret the code
                if ((code > available) || (code == end_of_information)) {
                    break;
                }
                if (code == clear) {
                    // Reset decoder.
                    code_size = data_size + 1;
                    code_mask = (1 << code_size) - 1;
                    available = clear + 2;
                    old_code = nullCode;
                    continue;
                }
                if (old_code == nullCode) {
                    pixelStack[top++] = suffix[code];
                    old_code = code;
                    first = code;
                    continue;
                }
                in_code = code;
                if (code == available) {
                    pixelStack[top++] = (byte) first;
                    code = old_code;
                }
                while (code > clear) {
                    pixelStack[top++] = suffix[code];
                    code = prefix[code];
                }
                first = ((int) suffix[code]) & 0xff;
                // Add a new string to the string table,
                if (available >= MAX_STACK_SIZE) {
                    break;
                }
                pixelStack[top++] = (byte) first;
                prefix[available] = (short) old_code;
                suffix[available] = (byte) first;
                available++;
                if (((available & code_mask) == 0) && (available < MAX_STACK_SIZE)) {
                    code_size++;
                    code_mask += available;
                }
                old_code = in_code;
            }
            // Pop a pixel off the pixel stack.
            top--;
            pixels[pi++] = pixelStack[top];
            i++;
        }
        for (i = pi; i < pixelsCount; i++) {
            pixels[i] = 0; // clear missing pixels
        }

        return pixels;
    }

    private static GifExtension readExtension(DataInput in) throws IOException {
        final int extensionCode = in.readUnsignedByte();
        final GifExtensionType type = GifExtensionType.fromCode((byte)extensionCode);
        switch (type) {
            case GraphicControl: // graphic control extension
                return readGraphicControlExtension(in);
            case Application: // application extension
                return readApplicationExtension(in);
            case Comment: // comment extension
                skipBlocks(in);
                break;
            case PlainText: // plain text extension
                skipBlocks(in);
                break;
            default:
                skipBlocks(in);
        }

        return null;
    }

    private static GifApplicationExtension readApplicationExtension(DataInput in) throws IOException {
        final GifApplicationExtension extension;

        final byte[] appBlockBytes = readBlock(in);
        final String appName = new String(appBlockBytes, 0, 11, "ASCII");
        final GifApplicationType applicationType = GifApplicationType.fromName(appName);
        switch (applicationType) {
            case Netscape20: {
                extension = new GifApplicationNetscape20Extension();
                byte[] blockBytes;
                while ((blockBytes = readBlock(in)).length > 0) {
                    final DataInputStream blockIn = new DataInputStream(new ByteArrayInputStream(blockBytes));
                    try {
                        final int extensionCode = blockIn.readUnsignedByte();
                        final Netscape20Value value = Netscape20Value.fromCode((byte) extensionCode);
                        switch (value) {
                            case LoopCount:
                                ((GifApplicationNetscape20Extension)extension).setLoopCount(blockIn.readShort());
                                break;
                            case Other:
                            default:
                                continue;
                        }
                    } finally {
                        blockIn.close();
                    }
                }
                return extension;
            }
            case Other:
            default:
                skipBlocks(in);
                return null;
        }
    }

    private static GifGraphicControlExtension readGraphicControlExtension(DataInput in) throws IOException {
        final GifGraphicControlExtension extension = new GifGraphicControlExtension();

        in.readByte(); //Block size
        final byte packedInfo = in.readByte();
        extension.setDispose((packedInfo & 0x1c) >> 2);
        extension.setTransparency((packedInfo & 1) != 0);
        extension.setDelay(in.readShort() * 10);
        extension.setTransparentIndex(in.readUnsignedByte());
        in.readByte(); //Block terminator

        return extension;
    }

    private static GifPackedInformation readPackedInformation(DataInput in) throws IOException {
        final GifPackedInformation packedInformation = new GifPackedInformation();

        final byte packedByte = in.readByte();
        packedInformation.setUseColorTable((packedByte & 0x80) != 0);
        packedInformation.setColorTableSize(2 << (packedByte & 7));
        packedInformation.setInterlace((packedByte & 0x40) != 0);

        return packedInformation;
    }

    /**
     * Skips variable length blocks up to and including next zero length block.
     */
    private static void skipBlocks(DataInput in) throws IOException {
        while (readBlock(in).length > 0) {}
    }

    private static byte[] readBlock(DataInput in) throws IOException {
        final int blockLength = in.readUnsignedByte();
        final byte[] blockBuffer = new byte[blockLength];

        in.readFully(blockBuffer);

        return blockBuffer;
    }

    private GifDecoder() {
    }
}
