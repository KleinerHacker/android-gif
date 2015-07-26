package org.pcsoft.android.gif;

/**
 * Represent the GIF metadata for each single GIF frame, see {@link GifFrame}
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

    /**
     * Top position
     * @return
     */
    public int getTop() {
        return top;
    }

    /**
     * Left position
     * @return
     */
    public int getLeft() {
        return left;
    }

    /**
     * Image width
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Image height
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * Delay for this single image
     * @return
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Transparency flag
     * @return
     */
    public boolean isTransparency() {
        return transparency;
    }

    /**
     * Transparent color, if {@link #isTransparency()} is TRUE
     * @return
     */
    public int getTransparentColor() {
        return transparentColor;
    }

    /**
     * Dispose value. Only for internal use.
     * @return
     */
    int getDispose() {
        return dispose;
    }
}
