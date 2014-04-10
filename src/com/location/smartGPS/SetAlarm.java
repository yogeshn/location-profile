package com.location.smartGPS;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetAlarm extends Activity {

	private Button addbutton;
	private Button settone;
	private Button enddistance;
	private Button startAlarm;
	private String destinationAddress;
	private Location destinationPoint;
	private TextView destAddTextView;
	private TextView alarmToneTextView;
	private TextView endDistanceTextView;
	private String selectedTone;
	private String endDistance;
	private Integer wakeUpDistance = Integer.MIN_VALUE;
	Double lati,longi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setalarmscreen);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		addbutton = (Button) findViewById(R.id.setdestbutton);
		settone = (Button) findViewById(R.id.settonebutton);
		
		startAlarm = (Button) findViewById(R.id.startthealarm);
		destAddTextView = (TextView) findViewById(R.id.destAddTextView);
		alarmToneTextView = (TextView) findViewById(R.id.setToneTextView);
		endDistanceTextView = (TextView) findViewById(R.id.endDistTextView);

		addbutton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			
				
				Intent i=new Intent(getApplicationContext(),Alarm_map.class);
				startActivityForResult(i, 1);
				
				
			}
		});
		
		settone.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				final String[] ringTones = {"Soft Tone", "Medium Tone", "Loud Tone"};
				AlertDialog.Builder selectRingTone = new AlertDialog.Builder(SetAlarm.this);
				
				selectRingTone.setTitle("Select A Alarm Tone");
				
				selectRingTone.setSingleChoiceItems(ringTones, -1, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int item) {
						selectedTone = ringTones[item];
					}
				});
				
				selectRingTone.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						alarmToneTextView.setText(selectedTone);
					
						settone.setBackgroundResource(R.drawable.setringtonecheck);
					}
				});
				
				selectRingTone.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				});
				
				AlertDialog setToneDialog = selectRingTone.create();
				setToneDialog.show();
			}
		});
		
		
		
		startAlarm.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if (destinationAddress == null || destinationAddress == "--Address Not Found--" ){
					Toast.makeText(getApplicationContext(), "Set Destination/End Distance", Toast.LENGTH_SHORT).show();
				} else {
					
						Intent alarmStartActivity = new Intent(getApplicationContext(), AlarmStarted.class);
						alarmStartActivity.putExtra("lat", lati);
						alarmStartActivity.putExtra("long", longi);
						startActivity(alarmStartActivity);
					
				}
				
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		    	 String result=data.getStringExtra("addressString"); 
		    	 longi=data.getDoubleExtra("lat", 0.00);
		    	 lati=data.getDoubleExtra("longi", 0.00);
		    		//	Toast.makeText(this, "location"+result, Toast.LENGTH_LONG).show();
		    	        destAddTextView.setText(result);
		    	        destinationAddress=result;
		         
		     }
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
	}
	
	

	
	
	public boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
}
