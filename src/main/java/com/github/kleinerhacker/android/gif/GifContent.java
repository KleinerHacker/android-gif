package com.github.kleinerhacker.android.gif;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the GIF content information class. Only for internal use
 */
final class GifContent {
    private final List<GifFrame> frameList = new ArrayList<GifFrame>();
    private int loopCount = 0;

    /**
     * Returns all GIF frames
     * @return
     */
    public List<GifFrame> getFrameList() {
        return frameList;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }
}