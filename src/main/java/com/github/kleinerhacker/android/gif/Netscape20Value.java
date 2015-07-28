package com.github.kleinerhacker.android.gif;

/**
 * Represent all known data (codes) from Netscape 2.0 standard
 */
enum Netscape20Value {
    /**
     * Count of loops
     */
    LoopCount((byte)0x01),
    Other(null);

    public static Netscape20Value fromCode(byte code) {
        for (final Netscape20Value data : values()) {
            if (data.getCode() == null)
                continue;
            if (data.getCode() == code)
                return data;
        }

        return Other;
    }

    private final Byte code;

    private Netscape20Value(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }
}
