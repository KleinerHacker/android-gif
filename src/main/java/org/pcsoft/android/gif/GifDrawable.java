package org.pcsoft.android.gif;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

/**
 * Represent an android GIF optional animated drawable.
 */
public class GifDrawable extends AnimationDrawable {

    private final Gif gif;

    /**
     * Creates a new GIF drawable based on the given GIF
     * @param resources Context Resources for density
     * @param gif GIF to show with this drawable
     */
    public GifDrawable(Resources resources, Gif gif) {
        this.gif = gif;
        for (final GifFrame gifFrame : gif.getFrameList()) {
            addFrame(new BitmapDrawable(resources, gifFrame.getImage()), gifFrame.getMetadata().getDelay());
        }
        setVisible(true, true);
        setOneShot(gif.getLoopCount() != 0);
    }

    /**
     * Returns the based GIF
     * @return
     */
    public Gif getGif() {
        return gif;
    }
}
