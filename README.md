# Android GIF Library
The Android GIF Libraray (android-gif) adds support for GIF files in Android like existing structure of Android Bitmap. This is the simplest way to use GIFs in Android:

## Examples

The following examples shows you how easy to use is this library:

### Load a GIF file

<p><code>
final Gif myGif = GifFactory.decodeByteArray(gifBytes, 0, gifBytes.length);
</code></p>

### Show a GIF file on drawable

<pre><code>final Gif myGif = GifFactory.decodeResource(context.getResources(), R.raw.my_gif);
final Drawable myDrawable = new GifDrawable(context.getResources(), myGif);</code></pre>

### Scale a loaded GIF

<pre><code>final Gif myGif = GifFactory.decodeStream(gifInputStream);
final Gif myScaledGif = Gif.createScaledGif(myGif, 200, 150, true);</code></pre>

### Show single image of GIF

<pre><code>final Gif myGif = GifFactory.decodeFile("test.gif");
final Drawable myDrawable = new BitmapDrawable(context.getResources(), myGif.getFrames()[0].getImage());</code></pre>

***

## Known Bugs

* Loop Count faulty: Only one loop or endless loop supported
