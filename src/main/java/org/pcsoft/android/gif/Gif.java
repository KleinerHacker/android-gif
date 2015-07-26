package org.pcsoft.android.gif;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christoph on 26.07.2015.
 */
public final class Gif {
    public static final int LOOP_ENDLESS = -1;

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
