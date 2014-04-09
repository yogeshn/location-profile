package com.location.smartGPS;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class AlarmStarted extends Activity{
	
	TextView textline1;
	TextView textline2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmstartedscreen);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "GeosansLight.ttf");
		
		textline1 = (TextView) findViewById(R.id.textline1);
		textline1.setTypeface(font);
		textline2 = (TextView) findViewById(R.id.textline2);
		textline2.setTypeface(font);
	}
	
	@Override
	public void onBackPressed() {}
}
