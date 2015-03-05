package com.projapps.gallery;

import com.projapps.gallery.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GalleryViewActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, "grid"));
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        		Intent i = new Intent(GalleryViewActivity.this, ImageViewActivity.class);
        		i.putExtra(ImageConstants.POSITION, position);
        		startActivity(i);
        	}
        });
    }
    
}
