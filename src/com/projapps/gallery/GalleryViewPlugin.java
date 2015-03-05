package com.projapps.gallery;

import android.content.Intent;
import android.util.Log;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;
import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

public class GalleryViewPlugin extends Plugin {
	public static final String ACTION = "view";
	
	@Override
	public PluginResult execute(String action, JSONArray data, String callbackId) {
		Log.d("GalleryViewPlugin", "Plugin Called");
		PluginResult result = null;
		if (ACTION.equals(action)) {
			/*
			try {
				
			}
			catch (JSONException jsonEx) {
				Log.d("GalleryViewPlugin", "Got JSON Exception " + jsonEx.getMessage());
				result = new PluginResult(Status.JSON_EXCEPTION);
			}
			*/
			Intent intent = new Intent(this.ctx, GalleryViewActivity.class);
			this.ctx.startActivity(intent);
			result = new PluginResult(Status.OK);
		}
		else {
			result = new PluginResult(Status.INVALID_ACTION);
			Log.d("GalleryViewPlugin", "Invalid action : " + action + " passed");
		}
		return result;
	}
}
