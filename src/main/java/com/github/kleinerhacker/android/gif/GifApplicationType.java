package com.github.kleinerhacker.android.gif;

/**
 * Contains all GIF Application Types. Only for internal use
 */
enum GifApplicationType {
    /**
     * Netscape 2.0
     */
    Netscape20("NETSCAPE2.0"),
    Other(null);

    public static GifApplicationType fromName(String name) {
        for (final GifApplicationType type : values()) {
            if (type.getName() == null)
                continue;
            if (type.getName().equals(name))
                return type;
        }

        return Other;
    }

    private final String name;

    private GifApplicationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
