package org.pcsoft.android.gif;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the GIF content information class. Only for internal use
 */
final class GifContent {
    private final List<GifFrame> frameList = new ArrayList<GifFrame>();

    /**
     * Returns all GIF frames
     * @return
     */
    public List<GifFrame> getFrameList() {
        return frameList;
    }
}