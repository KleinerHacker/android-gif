package com.github.kleinerhacker.android.gif;

/**
 * Packed Information. Only for internal use.
 */
final class GifPackedInformation {
    private boolean useColorTable;
    private int colorTableSize;
    private boolean interlace;

    public boolean isUseColorTable() {
        return useColorTable;
    }

    public void setUseColorTable(boolean useColorTable) {
        this.useColorTable = useColorTable;
    }

    public int getColorTableSize() {
        return colorTableSize;
    }

    public void setColorTableSize(int colorTableSize) {
        this.colorTableSize = colorTableSize;
    }

    public boolean isInterlace() {
        return interlace;
    }

    public void setInterlace(boolean interlace) {
        this.interlace = interlace;
    }
}