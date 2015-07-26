package org.pcsoft.android.gif;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Christoph on 26.07.2015.
 */
public class GifDrawable extends AnimationDrawable {

    private final Gif gif;

    public GifDrawable(Resources resources, Gif gif) {
        this.gif = gif;
        for (final GifFrame gifFrame : gif.getFrameList()) {
            addFrame(new BitmapDrawable(resources, gifFrame.getImage()), gifFrame.getMetadata().getDelay());
        }
        setVisible(true, true);
        setOneShot(gif.getLoopCount() != 0);
    }

    public Gif getGif() {
        return gif;
    }
}
