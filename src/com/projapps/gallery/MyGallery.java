package com.projapps.gallery;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * References: [1] http://www.eoeandroid.com/thread-101038-1-1.html
 * 			   [2] http://stackoverflow.com/questions/7441114/image-view-zoom-in-and-out-inside-gallery-view-in-android
 * 			   [3] http://stackoverflow.com/questions/3171452/scrollview-and-gallery-interfering
 * 			   [4] http://twnin.blogspot.com/2011/04/extend-gallery-support.html
 * 			   [5] http://stackoverflow.com/questions/6075363/android-image-view-matrix-scale-translate
 */
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
			if ((event.getX() - m_start.x < 0 && imageView.isRight) || (event.getX() - m_start.x > 0 && imageView.isLeft))
				imageView.isDraggable = false;
			break;
		}
		if (!imageView.isDraggable) {
	        onTouchEvent(event);
	    }
		return super.onInterceptTouchEvent(event);
	}
	/*
	private void setImageTransformation(float tx, float ty, float scale) {
	    savedMatrix.reset();
	    savedMatrix.postTranslate(-imageWidth / 2f, -imageHeight / 2f);
	    savedMatrix.postScale(scale, scale);
	    savedMatrix.postTranslate(tx, ty);
	    imageView.setImageMatrix(savedMatrix);
	}

	public void resetImageMatrix() {
		displayWidth = imageView.getWidth();
		displayHeight = imageView.getHeight();
	    imageWidth = imageView.getDrawable().getIntrinsicWidth();
	    imageHeight = imageView.getDrawable().getIntrinsicHeight();

	    float scaleX = (float) displayWidth / (float) imageWidth;
	    float scaleY = (float) displayHeight / (float) imageHeight;
	    float minScale = Math.min(scaleX, scaleY);
	    float maxScale = 2.5f * minScale;
	    
	    initialTranslation.set(
	              Math.max(0, 
	                minScale * imageWidth / 2f 
	                + 0.5f * (displayWidth - (minScale * imageWidth))), 
	              Math.max(0, 
	                minScale * imageHeight / 2f
	                + 0.5f * (displayHeight - (minScale * imageHeight))));

	    float currentScale = minScale;
	    currentTranslation.set(initialTranslation);
	    initialImageRect.set(0, 0, imageWidth, imageHeight);

	    setImageTransformation(initialTranslation.x, initialTranslation.y, 
	                minScale);
	}
	*/
}
