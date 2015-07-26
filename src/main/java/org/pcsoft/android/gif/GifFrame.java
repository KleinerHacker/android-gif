package org.pcsoft.android.gif;

import android.graphics.Bitmap;

/**
 * Created by Christoph on 26.07.2015.
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

    public GifMetadata getMetadata() {
        return metadata;
    }

    public GifColorTable getColorTable() {
        return colorTable;
    }

    public Bitmap getImage() {
        return image;
    }
}
