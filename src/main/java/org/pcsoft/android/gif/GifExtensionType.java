package org.pcsoft.android.gif;

/**
 * Created by Christoph on 26.07.2015.
 */
enum GifExtensionType {
    Application((byte) 0xff),
    GraphicControl((byte) 0xf9),
    Comment((byte) 0xfe),
    PlainText((byte) 0x01),
    Other(null);

    public static GifExtensionType fromCode(byte code) {
        for (final GifExtensionType type : values()) {
            if (type.getCode() == null)
                continue;
            if (type.getCode() == code)
                return type;
        }

        return Other;
    }

    private final Byte code;

    private GifExtensionType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }
}
