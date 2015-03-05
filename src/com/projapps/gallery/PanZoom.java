package com.projapps.gallery;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import com.phonegap.*;

public class PanZoom extends DroidGap {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
        WebSettings settings = appView.getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDefaultZoom(ZoomDensity.FAR);
    }
}
