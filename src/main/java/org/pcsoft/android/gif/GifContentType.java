package org.pcsoft.android.gif;

/**
 * Contains all content types. Only for internal use
 */
enum GifContentType {
    /**
     * Image section
     */
    Image((byte)0x2C),
    /**
     * Extension section
     */
    Extension((byte)0x21),
    /**
     * Terminator
     */
    Terminator((byte)0x3b),
    Other(null);

    public static GifContentType fromCode(byte code) {
        for (final GifContentType type : values()) {
            if (type.getCode() == null)
                continue;
            if (type.getCode() == code)
                return type;
        }

        return Other;
    }

    private final Byte code;

    private GifContentType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }
}
