package com.github.kleinerhacker.android.gif;

/**
 * Transparent color table, based on {@link GifColorTable}. Only for internal use.
 */
class GifTransparencyColorTable extends GifColorTable {

    private final int transparentIndex;
    private final int transparentColor;

    /**
     * Creates, based on a regular color table, a transparent value color table with help og a transparent index value.
     * @param colorTable
     * @param transparentIndex
     */
    public GifTransparencyColorTable(GifColorTable colorTable, int transparentIndex) {
        super(colorTable.getColors().clone());
        this.transparentIndex = transparentIndex;
        this.transparentColor = getColors()[transparentIndex];

        getColors()[transparentIndex] = 0;
    }

    /**
     * Index of transparent color in color table
     * @return
     */
    public int getTransparentIndex() {
        return transparentIndex;
    }

    /**
     * Color to use as transparent
     * @return
     */
    public int getTransparentColor() {
        return transparentColor;
    }
}
