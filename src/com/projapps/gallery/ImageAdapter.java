package com.projapps.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	public static final String GRID_VIEW = "grid";
	public static final String GALLERY_VIEW = "gallery";
	private Integer[] mImageIds;
	private Context mContext;
	private String mView;
	int mGalleryItemBackground;
	
	public ImageAdapter(Context c, String s) {
		mImageIds = ImageConstants.ImageIds;
        mContext = c;
        mView = s;
    }
	
	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		if (mView == GALLERY_VIEW)
			return position;
		else
			return null;
	}

	public long getItemId(int position) {
		if (mView == GALLERY_VIEW)
			return position;
		else
			return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(mContext);
		if (mView == GRID_VIEW) {
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				imageView.setLayoutParams(new GridView.LayoutParams(140, 140));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			} else {
				imageView = (ImageView) convertView;
			}
		}
		else if (mView == GALLERY_VIEW) {
			imageView = new MyImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			//imageView.setScaleType(ImageView.ScaleType.MATRIX);
			//imageView.setOnTouchListener(new MyTouchListener());
		}
		imageView.setImageResource(mImageIds[position]);
		return imageView;
	}
	
}
