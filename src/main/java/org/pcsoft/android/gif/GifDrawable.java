package org.pcsoft.android.gif;

import android.graphics.drawable.AnimationDrawable;

/**
 * Created by Christoph on 26.07.2015.
 */
public class GifDrawable extends AnimationDrawable {

    private final Gif gif;

    public GifDrawable(Gif gif) {
        this.gif = gif;
    }
}
