package com.location.smartGPS;

import com.location.smartGPS.BuildConfig;

import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;

public class BaseActivity extends Activity {

protected static final int NO_FLAGS = 0;

protected static final int DEFAULT_SEGUEIXME_DURANT = 8;
protected static final int DEFAULT_GUARDA_CADA = 2;
protected static final String PARAM_SEGUEIXME = "param_segueixme";
protected static final String PARAM_GUARDA = "param_guarda";
protected static final String PARAM_LOCATIONS = "param_locations";
protected static final String PARAM_INDEX = "param_index";
protected static final String PARAM_HE_OBTINGUT_LOCATION_NOVA = "param_he_obtingut_ln";
protected static final int NOTIFICATION_TRACKME = 776;

private static final String LOG_TAG = "TrackMe";
protected void LOG(String msg) {
	if (BuildConfig.DEBUG) {
		Log.i(LOG_TAG, msg);
	}
}
protected void CONFIG_STRICT_MODE() {
	if (BuildConfig.DEBUG) {
		StrictMode.enableDefaults();
	}
}

}
