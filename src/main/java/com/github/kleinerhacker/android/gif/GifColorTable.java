package com.github.kleinerhacker.android.gif;

/**
 * Represent a GIF color table with up to 256 color values
 */
public class GifColorTable {
    private final int[] colors;

    GifColorTable(int[] colors) {
        this.colors = colors;
    }

    /**
     * Returns all colors (up to 256) for this color table
     * @return
     */
    public int[] getColors() {
        return colors;
    }

    public final GifColorTable copy() {
        return new GifColorTable(colors.clone());
    }
}
