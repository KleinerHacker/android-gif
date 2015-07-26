package org.pcsoft.android.gif;

/**
 * Created by Christoph on 26.07.2015.
 */
public class GifColorTable {
    private final int[] colors;

    GifColorTable(int[] colors) {
        this.colors = colors;
    }

    public int[] getColors() {
        return colors;
    }

    public final GifColorTable copy() {
        return new GifColorTable(colors.clone());
    }
}
