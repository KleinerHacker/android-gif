package org.pcsoft.android.gif;

import android.graphics.Bitmap;

/**
 * Represent a dingle GIF frame
 */
public final class GifFrame {
    private final GifMetadata metadata;
    private final GifColorTable colorTable;
    private final Bitmap image;

    GifFrame(GifMetadata metadata, GifColorTable colorTable, Bitmap image) {
        this.metadata = metadata;
        this.colorTable = colorTable;
        this.image = image;
    }

    /**
     * Returns the metadata for this frame
     * @return
     */
    public GifMetadata getMetadata() {
        return metadata;
    }

    /**
     * Returns the color table for this frame, or null, if global is to use
     * @return
     */
    public GifColorTable getColorTable() {
        return colorTable;
    }

    /**
     * Returns the decoded bitmap for this frame
     * @return
     */
    public Bitmap getImage() {
        return image;
    }
}
