package org.pcsoft.android.gif;

/**
 * Represent a Netscape 2.0 application extension in GIF standard
 */
final class GifApplicationNetscape20Extension extends GifApplicationExtension {
    private int loopCount;

    /**
     * Loop Counter Value
     * @return
     */
    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    @Override
    public GifApplicationType getApplicationType() {
        return GifApplicationType.Netscape20;
    }
}
