package org.pcsoft.android.gif;

import android.content.res.Resources;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Christoph on 26.07.2015.
 */
public final class GifFactory {

    public static Gif decodeByteArray(byte[] bytes, int offset, int length) {
        try {
            return GifDecoder.decode(new ByteArrayInputStream(bytes, 0, length));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to decode bytes!", e);
        }
    }

    public static Gif decodeFile(String pathname) {
        try {
            return GifDecoder.decode(new FileInputStream(pathname));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to decode file: " + pathname, e);
        }
    }

    public static Gif decodeFileDescriptor(FileDescriptor fileDescriptor) {
        return null;
    }

    public static Gif decodeResource(Resources resources, int id) {
        try {
            return GifDecoder.decode(resources.openRawResource(id));
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to decode resource 0x" + StringUtils.leftPad(Integer.toHexString(id), 8, '0'), e);
        }
    }

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
