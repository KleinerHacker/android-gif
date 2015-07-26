package org.pcsoft.android.gif;

/**
 * Application Extension. Only for internal use
 */
final class GifApplicationExtension extends GifExtension {
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
    public GifExtensionType getExtensionType() {
        return GifExtensionType.Application;
    }
}