package com.github.kleinerhacker.android.gif;

import java.util.HashMap;
import java.util.Map;

/**
 * Represent the extension map. Only for internal use
 */
final class GifExtensionMap {

    private final Map<GifExtensionType, GifExtension> extensionMap = new HashMap<GifExtensionType, GifExtension>();

    public void put(GifExtension extension) {
        extensionMap.put(extension.getExtensionType(), extension);
    }

    public GifExtension get(GifExtensionType type) {
        return extensionMap.get(type);
    }

    public boolean isTransparency() {
        return extensionMap.containsKey(GifExtensionType.GraphicControl) &&
                ((GifGraphicControlExtension)extensionMap.get(GifExtensionType.GraphicControl)).isTransparency();
    }

    public Integer getTransparentIndex() {
        return extensionMap.containsKey(GifExtensionType.GraphicControl) ?
                ((GifGraphicControlExtension)extensionMap.get(GifExtensionType.GraphicControl)).getTransparentIndex() : null;
    }

    public int getDispose() {
        return extensionMap.containsKey(GifExtensionType.GraphicControl) ?
                ((GifGraphicControlExtension)extensionMap.get(GifExtensionType.GraphicControl)).getDispose() : 0;
    }

    public int getDelay() {
        return extensionMap.containsKey(GifExtensionType.GraphicControl) ?
                ((GifGraphicControlExtension)extensionMap.get(GifExtensionType.GraphicControl)).getDelay() : 100;
    }

    public int getLoopCount() {
        if (extensionMap.containsKey(GifExtensionType.Application)) {
            if (((GifApplicationExtension)extensionMap.get(GifExtensionType.Application)).getApplicationType() == GifApplicationType.Netscape20) {
                return ((GifApplicationNetscape20Extension)extensionMap.get(GifExtensionType.Application)).getLoopCount();
            }
        }

        return -1;
    }

}
