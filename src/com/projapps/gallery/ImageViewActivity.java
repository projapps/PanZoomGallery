package com.projapps.gallery;

import com.projapps.gallery.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {
	private Integer mPosition;
	MyGallery mGallery;
	ImageView mImageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPosition = (savedInstanceState == null) ? null : (Integer) savedInstanceState.getSerializable(ImageConstants.POSITION);
		if (mPosition == null) {
			Bundle extras = getIntent().getExtras();
			mPosition = extras != null ? extras.getInt(ImageConstants.POSITION) : null;
		}
		
		//setContentView(R.layout.gallery);
		setContentView(R.layout.main);
		mGallery = (MyGallery) findViewById(R.id.gallery);
		mGallery.setAdapter(new ImageAdapter(this, "gallery"));
		mGallery.setSelection(mPosition);
		
		/*
		setContentView(R.layout.image);
		mImageView = (ImageView) findViewById(R.id.imageview);
		mImageView.setImageResource(ImageConstants.ImageIds[mPosition]);
		mImageView.setOnTouchListener(new MyTouchListener());
		*/
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putSerializable(ImageConstants.POSITION, mPosition);
    }
	
	@Override
    protected void onResume() {
    	super.onResume();
    	if (mPosition != null) {
    		mGallery.setSelection(mPosition);
    		//mImageView.setImageResource(ImageConstants.ImageIds[mPosition]);
    	}
	}
}
