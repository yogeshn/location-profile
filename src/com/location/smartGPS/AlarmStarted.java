package com.location.smartGPS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmStarted extends Activity implements LocationListener{
	
	TextView textline1;
	Double la,lo;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

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
		
		Intent it=this.getIntent();
		la=it.getDoubleExtra("lat", 0.00);
		lo=it.getDoubleExtra("long", 0.00);
	}
	
	@Override
	public void onBackPressed() {}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		double lat=location.getLatitude();
		double loc=location.getLongitude();
		if (distance(lat, loc, la, lo) < 0.1) { // if distance < 0.1 miles we take locations as equal
		       //do what you want to do...
			
		    }
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	private double distance(double lat1, double lng1, double lat2, double lng2) {

	    double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);

	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);

	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	        * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

	    double dist = earthRadius * c;

	    return dist;
	}
	
	
}
