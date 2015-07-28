package com.github.kleinerhacker.android.gif;

/**
 * Application Extension (Base). Only for internal use
 */
abstract class GifApplicationExtension extends GifExtension {

    /**
     * Type of application extension
     * @return
     */
    public abstract GifApplicationType getApplicationType();

    @Override
    public final GifExtensionType getExtensionType() {
        return GifExtensionType.Application;
    }
}