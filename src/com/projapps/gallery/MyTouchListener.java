package com.projapps.gallery;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MyTouchListener implements OnTouchListener {
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	
	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	
	// Limit zoomable/pannable image
	private ImageView view;
	private float[] matrixValues = new float[9];
	
	public boolean onTouch(View v, MotionEvent event) {
		view = (ImageView) v;
		if (view.getScaleType() != ImageView.ScaleType.MATRIX) {
			view.setScaleType(ImageView.ScaleType.MATRIX);
		}
		
		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					//matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
					limitPan(event);
				}
				else if (mode == ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
		}
		
		view.setImageMatrix(matrix);
		return true;
	}

	/** Limit pan */
	private void limitPan(MotionEvent event) {
		matrix.getValues(matrixValues);
		
		float height = view.getDrawable().getIntrinsicHeight();
		float width = view.getDrawable().getIntrinsicWidth();
		RectF viewRect = new RectF(0, 0, view.getWidth(), view.getHeight());
		
		float currentY = matrixValues[Matrix.MTRANS_Y];
		float currentX = matrixValues[Matrix.MTRANS_X];
		float currentScale = matrixValues[Matrix.MSCALE_X];
		float currentHeight = height * currentScale;
		float currentWidth = width * currentScale;
		float dx = event.getX() - start.x;
		float dy = event.getY() - start.y;
		float newX = currentX+dx;
		float newY = currentY+dy; 
		
		RectF drawingRect = new RectF(newX, newY, newX+currentWidth, newY+currentHeight);
		float diffUp = Math.min(viewRect.bottom-drawingRect.bottom, viewRect.top-drawingRect.top);
		float diffDown = Math.max(viewRect.bottom-drawingRect.bottom, viewRect.top-drawingRect.top);
		float diffLeft = Math.min(viewRect.left-drawingRect.left, viewRect.right-drawingRect.right);
		float diffRight = Math.max(viewRect.left-drawingRect.left, viewRect.right-drawingRect.right);
		
		if (diffUp > 0 ){
			dy += diffUp; 
		}
		if (diffDown < 0){
			dy += diffDown;
		} 
		if (diffLeft > 0){ 
			dx += diffLeft;
		}
		if (diffRight < 0){
			dx += diffRight;
		}
		matrix.postTranslate(dx, dy);
	}

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}
