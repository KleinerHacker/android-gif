package com.github.kleinerhacker.android.gif;

import android.content.Context;
import android.content.res.Resources;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represent the factory to create a GIF from different input variants
 */
public final class GifFactory {

    /**
     * Decode GIF from byte array
     * @param bytes bytes to read
     * @param offset offset to start reading in buffer
     * @param length length to read in buffer
     * @return GIF
     */
    public static Gif decodeByteArray(byte[] bytes, int offset, int length) {
        try {
            return GifDecoder.decode(new ByteArrayInputStream(bytes, 0, length));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to decode bytes!", e);
        }
    }

    /**
     * Decode GIF from file
     * @param pathname GIF file name
     * @return GIF
     */
    public static Gif decodeFile(String pathname) {
        try {
            return GifDecoder.decode(new FileInputStream(pathname));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to decode file: " + pathname, e);
        }
    }

    /**
     * Decode GIF from file descriptor
     * @param fileDescriptor
     * @return GIF
     */
    public static Gif decodeFileDescriptor(Context context, FileDescriptor fileDescriptor) {
        try {
            return GifDecoder.decode(new FileInputStream(fileDescriptor));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to decode from file descriptor!", e);
        }
    }

    /**
     * Decode GIF from raw-resource
     * @param resources Resources
     * @param id ID of raw-resource to decode
     * @return GIF
     */
    public static Gif decodeResource(Resources resources, int id) {
        try {
            return GifDecoder.decode(resources.openRawResource(id));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to decode resource 0x" + StringUtils.leftPad(Integer.toHexString(id), 8, '0'), e);
        }
    }

    /**
     * Decode GIF from stream
     * @param is Stream with GIF bytes
     * @return GIF
     */
    public static Gif decodeStream(InputStream is) {
        try {
            return GifDecoder.decode(is);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to decode GIF from stream!", e);
        }
    }

    private GifFactory() {
    }
}
