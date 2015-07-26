package org.pcsoft.android.gif;

/**
 * Extension Types. Only for internal use
 */
enum GifExtensionType {
    /**
     * Application (loop count)
     */
    Application((byte) 0xff),
    /**
     * Graphic Control (transparency)
     */
    GraphicControl((byte) 0xf9),
    /**
     * Comment (not supported yet)
     */
    Comment((byte) 0xfe),
    /**
     * Plain Text (not supported yet)
     */
    PlainText((byte) 0x01),
    Other(null);

    /**
     * Creates from given code
     * @param code
     * @return
     */
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
