package org.pcsoft.android.gif;

/**
 * Created by Christoph on 26.07.2015.
 */
public final class GifMetadata {
    private final int top, left, width, height;
    private final int delay;
    private final boolean transparency;
    private final int transparentColor;
    private final int dispose;

    GifMetadata(int left, int top, int width, int height, int delay, boolean transparency, int transparentColor, int dispose) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
        this.delay = delay;
        this.transparency = transparency;
        this.transparentColor = transparentColor;
        this.dispose = dispose;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isTransparency() {
        return transparency;
    }

    public int getTransparentColor() {
        return transparentColor;
    }

    int getDispose() {
        return dispose;
    }
}
