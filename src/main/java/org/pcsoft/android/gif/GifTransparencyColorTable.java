package org.pcsoft.android.gif;

/**
 * Created by Christoph on 26.07.2015.
 */
class GifTransparencyColorTable extends GifColorTable {

    private final int transparentIndex;
    private final int transparentColor;

    public GifTransparencyColorTable(GifColorTable colorTable, int transparentIndex) {
        super(colorTable.getColors().clone());
        this.transparentIndex = transparentIndex;
        this.transparentColor = getColors()[transparentIndex];

        getColors()[transparentIndex] = 0;
    }

    public int getTransparentIndex() {
        return transparentIndex;
    }

    public int getTransparentColor() {
        return transparentColor;
    }
}
