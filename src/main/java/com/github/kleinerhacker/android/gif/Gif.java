package com.github.kleinerhacker.android.gif;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the immutable GIF image. GIF images can only loaded, not saved!
 */
public final class Gif {
    /**
     * Endless loop constant
     */
    public static final int LOOP_ENDLESS = 0;

    /**
     * Scale the given source gif to the given bounds and use an optional filter
     * @param source Source GIF to scale
     * @param dstWidth Destination Width
     * @param dstHeight Destination Height
     * @param filter TRUE to aktivate the filter
     * @return The immutable scaled GIF
     */
    public static Gif createScaledGif(Gif source, int dstWidth, int dstHeight, boolean filter) {
        final Gif gif = new Gif(dstWidth, dstHeight, source.getLoopCount(), source.getPixelAspectRatio(), source.getGlobalColorTable());
        for (final GifFrame frame : source.getFrameList()) {
            gif.getFrameList().add(new GifFrame(
                    new GifMetadata(
                            dstWidth * frame.getMetadata().getLeft() / frame.getMetadata().getWidth(),
                            dstHeight * frame.getMetadata().getTop() / frame.getMetadata().getHeight(),
                            dstWidth, dstHeight, frame.getMetadata().getDelay(),
                            frame.getMetadata().isTransparency(), frame.getMetadata().getTransparentColor(),
                            frame.getMetadata().getDispose()
                    ),
                    frame.getColorTable().copy(),
                    Bitmap.createScaledBitmap(frame.getImage(), dstWidth, dstHeight, filter)
            ));
        }

        return gif;
    }

    private final int width, height;
    private final int loopCount;
    private final int pixelAspectRatio;
    private final GifColorTable globalColorTable;
    private final List<GifFrame> frameList = new ArrayList<GifFrame>();

    Gif(int width, int height, int loopCount, int pixelAspectRatio, GifColorTable globalColorTable) {
        this.width = width;
        this.height = height;
        this.loopCount = loopCount;
        this.pixelAspectRatio = pixelAspectRatio;
        this.globalColorTable = globalColorTable;
    }

    /**
     * Width of the GIF
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Height of the GIF
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * Count of Loops or endless, see {@link #LOOP_ENDLESS}
     * @return
     */
    public int getLoopCount() {
        return loopCount;
    }

    List<GifFrame> getFrameList() {
        return frameList;
    }

    /**
     * The list of all frames in this GIF
     * @return
     */
    public GifFrame[] getFrames() {
        return frameList.toArray(new GifFrame[frameList.size()]);
    }

    /**
     * Returns the global color table
     * @return
     */
    public GifColorTable getGlobalColorTable() {
        return globalColorTable;
    }

    /**
     * Returns the pixel aspect ratio factor
     * @return
     */
    public int getPixelAspectRatio() {
        return pixelAspectRatio;
    }
}
