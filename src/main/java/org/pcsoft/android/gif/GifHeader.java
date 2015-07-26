package org.pcsoft.android.gif;

/**
 * Represent the GIF header information class. Only for internal use
 */
final class GifHeader {
    private int width, height;
    private int backgroundColor, pixelAspectRatio;
    private GifColorTable colorTable;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getPixelAspectRatio() {
        return pixelAspectRatio;
    }

    public void setPixelAspectRatio(int pixelAspectRatio) {
        this.pixelAspectRatio = pixelAspectRatio;
    }

    public GifColorTable getColorTable() {
        return colorTable;
    }

    /**
     * Delegate from color table, see {@link GifColorTable#getColors()}
     * @return
     */
    public int[] getColors() {
        return colorTable.getColors();
    }

    public void setColorTable(GifColorTable colorTable) {
        this.colorTable = colorTable;
    }
}