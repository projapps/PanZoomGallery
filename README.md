# PanZoomGallery
Android Gallery with Pan/Zoom ImageView

> *This is a set of reference codes for a [blog post](http://blog.projapps.com/?p=85) I wrote 3 years ago to simulate the Android photo gallery with panning and zooming for individual images. In the days of SDK level android-10, this was difficult to achieve. As some of the links I referenced to in the blog post no longer exist, I decided to publish my codes so the knowledge won't be lost and I can link to them in my updated post.*

**Posted on February 20, 2012**

I was working on an image gallery project whereby the gallery has its own album of pictures, is side-scrolling and allows zooming and panning of each image; just like the built-in gallery in an Android phone.

After more than a week of hair-pulling and eye popping googling, I managed to find the list of resources to display pictures using the [Android gallery widget](http://developer.android.com/resources/tutorials/views/hello-gallery.html) and the codes of panning and zooming a single image. However, I was left with two issues to resolve, which I did below:

1. How to differentiate which touch movements belong to the gallery and which ones belong to the image views?
2. How to, somewhat seamlessly, switch an image view from a scale type of CENTER_INSIDE to MATRIX for zooming?

Before I explain what I did, please try out the gallery tutorial in developer.android.com and modify the xml to display the gallery full-screen. Hint: fill_parent…

First up, create your own ImageView class by extending Android’s and adapt the pan/zoom codes given in [[6](http://blogs.zdnet.com/Burnette/?p=1847)]. You may make use of the [TouchImageView](http://code.google.com/p/4chan-image-browser/source/browse/src/se/robertfoss/MultiTouchZoom/TouchImageView.java?r=02836650d3b69dd6a2fce1304c34ded1531d6ad5) class which basically has the same implementation but I prefer to learn from following the tutorial.

Why creating our own MyImageView class and not create a TouchListener to be set in ImageView? It is because we need to include some custom attributes to control the touch rights. **Note that we allow initial setting of matrices to prevent the abrupt jump in display size when we switch scale types.**

```
public class MyImageView extends ImageView {
    boolean isDraggable = false;
    boolean isRight = false;
    boolean isLeft = false;

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    public void setMatrix(Matrix m) {
        matrix.set(m);
    }

    public void setSavedMatrix(Matrix m) {
        savedMatrix.set(m);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDraggable) {
            // place pan/zoom codes here
        }
        return true;
    }
}
```

Look at the comments section of [[6](http://blogs.zdnet.com/Burnette/?p=1847)]. You may make use of the [TouchImageView](http://code.google.com/p/4chan-image-browser/source/browse/src/se/robertfoss/MultiTouchZoom/TouchImageView.java?r=02836650d3b69dd6a2fce1304c34ded1531d6ad5) and you will find the codes to adding a limit to the distance one can pan the image when the image is bigger than the view. This is set to the border of the image and thus allow us to return control to the gallery for scrolling. Add the following codes at the end:

```
if (dx == 0) {
    if (event.getX() - start.x < 0)
        isRight = true;
    else if (event.getX() - start.x > 0)
        isLeft = true;
}
else {
    isRight = false;
    isLeft = false;
}
```

Now back to the gallery. Google gave me numerous hints to intercepting touch events to the gallery and control which view processes what events. I could not find any concrete sample codes but after some trial-and-error tries, I came up with the following:

```
public class MyGallery extends Gallery {
    private PointF m_start = new PointF();
    private MyImageView imageView;

    public MyGallery(Context context) {
        super(context);
    }

    public MyGallery(Context context, AttributeSet attrSet) {
        super(context, attrSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        imageView = (MyImageView) getSelectedView();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                m_start.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                imageView.isDraggable = true;
                if (imageView.getScaleType() != ImageView.ScaleType.MATRIX) {
                    imageView.setMatrix(imageView.getImageMatrix());
                    imageView.setScaleType(ImageView.ScaleType.MATRIX);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if ((event.getX() - m_start.x < 0 && imageView.isRight) ||
                    (event.getX() - m_start.x > 0 && imageView.isLeft))
                    imageView.isDraggable = false;
                break;
        }
        if (!imageView.isDraggable) {
            onTouchEvent(event);
        }
        return super.onInterceptTouchEvent(event);
    }
}
```

References:

1. http://www.eoeandroid.com/thread-101038-1-1.html
2. http://stackoverflow.com/questions/7441114/image-view-zoom-in-and-out-inside-gallery-view-in-android
3. http://stackoverflow.com/questions/3171452/scrollview-and-gallery-interfering
4. http://twnin.blogspot.com/2011/04/extend-gallery-support.html
5. http://stackoverflow.com/questions/6075363/android-image-view-matrix-scale-translate
6. http://blogs.zdnet.com/Burnette/?p=1847

Acknowledgement:

* Anggra Sastriawan of [Gratif Pte Ltd](http://www.gratif.com/)

Disclaimer:

This solution should only be considered to be a ‘pseudo-solution’ as it mimics the usual album gallery app in an Android phone but nowhere as smooth as it. If anyone is kind enough to share (privately or publicly) with me how the phone’s gallery app is achieved, it will be greatly appreciated.
