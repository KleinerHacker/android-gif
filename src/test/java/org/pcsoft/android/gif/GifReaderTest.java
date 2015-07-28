package org.pcsoft.android.gif;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Christoph on 26.07.2015.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GifReaderTest {

    @Test
    public void basicTest() {
        final Gif gif = GifFactory.decodeStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test.gif"));

        Assert.assertEquals(365, gif.getWidth());
        Assert.assertEquals(360, gif.getHeight());
        Assert.assertEquals(8, gif.getFrames().length);

        Assert.assertEquals(256, gif.getGlobalColorTable().getColors().length);
        Assert.assertEquals(true, gif.getFrames()[0].getMetadata().isTransparency());
        Assert.assertEquals(-1, gif.getFrames()[0].getMetadata().getTransparentColor());
    }

}
