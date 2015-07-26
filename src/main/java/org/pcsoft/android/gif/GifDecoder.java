package org.pcsoft.android.gif;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Internal GIF decoder to read in the GIF
 */
final class GifDecoder {

    public static Gif decode(InputStream is) throws IOException {
        final DataInputStream in = new DataInputStream(is);

        final GifHeader header = readHeader(in);
        final GifContent content = readContent(header.getColorTable(), in);

        final Gif gif = new Gif(header.getWidth(), header.getHeight(), -1, header.getPixelAspectRatio(), header.getColorTable());
        gif.getFrameList().addAll(content.getFrameList());

        return gif;
    }

    private static GifHeader readHeader(DataInputStream in) throws IOException {
        final GifHeader header = new GifHeader();

        //Magic byte test
        final byte[] magicBytes = new byte[6];
        in.readFully(magicBytes);
        if (!new String(magicBytes, "ASCII").startsWith("GIF"))
            throw new IOException("Unable to find magic bytes of GIF: Input file is not a GIF file!");

        header.setWidth(in.readShort()); // 2 (Width)
        header.setHeight(in.readShort()); // 2 (Height)

        final GifPackedInformation packedInformation = readPackedInformation(in);// 1 (Packaged Info)
        final byte backgroundIndex = in.readByte(); // 1 (Background Index)
        header.setPixelAspectRatio(in.readByte()); // 1 (Pixel Aspect Ratio)
        if (packedInformation.isUseColorTable()) {
            header.setColorTable(readColorTable(packedInformation.getColorTableSize(), in)); // x (Color Table)
            header.setBackgroundColor(header.getColors()[backgroundIndex]);
        }

        return header;
    }

    private static GifColorTable readColorTable(int globalColorTableSize, DataInputStream in) throws IOException {
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

    private static GifContent readContent(GifColorTable globalColorTable, DataInputStream in) throws IOException {
        final GifContent content = new GifContent();
        final Map<GifExtensionType, GifExtension> extensionMap = new HashMap<GifExtensionType, GifExtension>();

        loop:
        while (true) {
            final byte code = in.readByte();
            final GifContentType type = GifContentType.fromCode(code);
            switch (type) {
                case Image:
                    final GifFrame frame = readFrame(globalColorTable, extensionMap, in);
                    content.getFrameList().add(frame);
                    break;
                case Extension:
                    final GifExtension extension = readExtension(in);
                    if (extension != null) {
                        extensionMap.put(extension.getExtensionType(), extension);
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

    private static GifFrame readFrame(GifColorTable globalColorTable, Map<GifExtensionType, GifExtension> extensionMap, DataInputStream in) throws IOException {
        final int left = in.readShort();
        final int top = in.readShort();
        final int width = in.readShort();
        final int height = in.readShort();

        final GifPackedInformation packedInformation = readPackedInformation(in);
        final GifColorTable colorTable;
        if (packedInformation.isUseColorTable()) {
            colorTable = readColorTable(packedInformation.getColorTableSize(), in);
        } else {
            colorTable = globalColorTable.copy();
        }

        //TODO

        return null;
    }

    private static GifExtension readExtension(DataInputStream in) throws IOException {
        final byte extensionCode = in.readByte();
        final GifExtensionType type = GifExtensionType.fromCode(extensionCode);
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

    private static GifApplicationExtension readApplicationExtension(DataInputStream in) throws IOException {
        final GifApplicationExtension extension = new GifApplicationExtension();

        final byte[] appBlockBytes = readBlock(in);
        final String appName = new String(appBlockBytes, 0, 11, "ASCII");
        if (appName.equals("NETSCAPE2.0")) {
            byte[] blockBytes;
            while ((blockBytes = readBlock(in)).length > 0) {
                final DataInputStream blockIn = new DataInputStream(new ByteArrayInputStream(blockBytes));
                try {
                    final byte extensionCode = blockIn.readByte();
                    switch (extensionCode) {
                        case 1:
                            extension.setLoopCount(blockIn.readShort());
                            break;
                        default:
                            continue;
                    }
                } finally {
                    blockIn.close();
                }
            }
            return extension;
        } else {
            skipBlocks(in);
            return null;
        }
    }

    private static GifGraphicControlExtension readGraphicControlExtension(DataInputStream in) throws IOException {
        final GifGraphicControlExtension extension = new GifGraphicControlExtension();

        in.readByte(); //Block size
        final byte packedInfo = in.readByte();
        extension.setDispose((packedInfo & 0x1c) >> 2);
        extension.setTransparency((packedInfo & 1) != 0);
        extension.setDelay(in.readShort() * 10);
        extension.setTransparentIndex(in.readByte());
        in.readByte(); //Block terminator

        return extension;
    }

    private static GifPackedInformation readPackedInformation(DataInputStream in) throws IOException {
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
    private static void skipBlocks(DataInputStream in) throws IOException {
        while (readBlock(in).length > 0) {}
    }

    private static byte[] readBlock(DataInputStream in) throws IOException {
        final byte blockLength = in.readByte();
        final byte[] blockBuffer = new byte[blockLength];

        in.readFully(blockBuffer);

        return blockBuffer;
    }

    private GifDecoder() {
    }
}
