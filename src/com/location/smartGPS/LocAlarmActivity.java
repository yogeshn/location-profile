package com.location.smartGPS;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LocAlarmActivity extends Activity {
	
	Button setalarm;
	TextView logotitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homescreen);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "GeosansLight.ttf");
		
		logotitle = (TextView) findViewById(R.id.logotitletext);
		logotitle.setTypeface(font);
		
		setalarm = (Button) findViewById(R.id.setalarmbutton);
		setalarm.setTypeface(font);
		Calendar cal = Calendar.getInstance();
		
	//	AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	//	alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60 * 1000, )
		
	}
	
	public void setAlarm (View view){
		Intent showSetAlarm = new Intent (this, SetAlarm.class);
		startActivity(showSetAlarm);
	}
	
}