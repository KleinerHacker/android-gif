package org.pcsoft.android.gif;

/**
 * Graphic Control Extension. Only for internal use.
 */
final class GifGraphicControlExtension extends GifExtension {
    private int dispose;
    private boolean transparency;
    private int delay, transparentIndex;

    public int getDispose() {
        return dispose;
    }

    public void setDispose(int dispose) {
        if (dispose == 0) {
            this.dispose = 1;
            return;
        }

        this.dispose = dispose;
    }

    public boolean isTransparency() {
        return transparency;
    }

    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getTransparentIndex() {
        return transparentIndex;
    }

    public void setTransparentIndex(int transparentIndex) {
        this.transparentIndex = transparentIndex;
    }

    @Override
    public GifExtensionType getExtensionType() {
        return GifExtensionType.GraphicControl;
    }
}