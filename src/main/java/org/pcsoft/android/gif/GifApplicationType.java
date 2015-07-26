package org.pcsoft.android.gif;

/**
 * Created by Christoph on 26.07.2015.
 */
public enum GifApplicationType {
    Netscape_2_0("NETSCAPE2.0"),
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
