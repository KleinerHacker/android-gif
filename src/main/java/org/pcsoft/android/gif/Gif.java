package org.pcsoft.android.gif;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christoph on 26.07.2015.
 */
public final class Gif {
    public static final int LOOP_ENDLESS = -1;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLoopCount() {
        return loopCount;
    }

    List<GifFrame> getFrameList() {
        return frameList;
    }

    public GifFrame[] getFrames() {
        return frameList.toArray(new GifFrame[frameList.size()]);
    }

    public GifColorTable getGlobalColorTable() {
        return globalColorTable;
    }

    public int getPixelAspectRatio() {
        return pixelAspectRatio;
    }
}
