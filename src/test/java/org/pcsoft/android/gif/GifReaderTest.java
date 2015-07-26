package org.pcsoft.android.gif;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Christoph on 26.07.2015.
 */
@Ignore
public class GifReaderTest {

    @Test
    public void test() {
        final Gif gif = GifFactory.decodeStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("test.gif"));
    }

}
