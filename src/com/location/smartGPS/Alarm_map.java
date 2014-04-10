package com.location.smartGPS;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Alarm_map extends Activity implements LocationListener , OnMapClickListener ,OnClickListener {
	GoogleMap map;
	 double latpoint;
     double lngpoint;
 	Marker marker;
 	CameraPosition cameraPosition;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_map_activity);
		 LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
	       lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	        map.setOnMapClickListener(this);
	        
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		 Address address;
		 latpoint =point.latitude;
         lngpoint = point.longitude;
         
        
     	        
     	        StringBuilder sb1;
		  Geocoder gc1 = new Geocoder(this, Locale.getDefault());
        try {
          List<Address> addresses = gc1.getFromLocation(latpoint, lngpoint, 1);
         sb1= new StringBuilder();
          if (addresses.size() > 0) {
             address = addresses.get(0);

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
              sb1.append(address.getAddressLine(i)).append("\n");
              sb1.append(address.getLocality()).append("\n");
            }
         String addressString = sb1.toString();
        
         marker = map.addMarker(new MarkerOptions().position(
      	        new LatLng(point.latitude, point.longitude)).title("alarm"));
         
         Intent i=new Intent();
         Bundle bundle = new Bundle();
        bundle.putString("addressString", addressString);
        bundle.putDouble("lat", latpoint);
        bundle.putDouble("longi", lngpoint);
     //   Toast.makeText(this,""+addressString, Toast.LENGTH_LONG).show();
        i.putExtras(bundle);
         setResult(RESULT_OK, i);
         finish();
        
        } catch (IOException e) {}
        


     	// Intent i=new Intent();
     	
		 
	}
/*
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		 Address address;
		
		
		//	mTapTextView.setText("tapped, point=" + point);
		 Toast.makeText(this, " "+point,Toast.LENGTH_LONG).show();
	//	 ArrayAdapter<string> =
	//	 list.add("B");
	//	 list.add("C");
		  latpoint =point.latitude;
          lngpoint = point.longitude;
         
          
          
          StringBuilder sb;
		  Geocoder gc = new Geocoder(this, Locale.getDefault());
          try {
            List<Address> addresses = gc.getFromLocation(latpoint, lngpoint, 1);
           sb= new StringBuilder();
            if (addresses.size() > 0) {
               address = addresses.get(0);

              for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                sb.append(address.getAddressLine(i)).append("\n");

                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
            }
           String addressString = sb.toString();
           Toast.makeText(this,""+addressString, Toast.LENGTH_LONG).show();

  		 
          
          } catch (IOException e) {}
          
		
		
		
		StringBuilder sb1;
		  Geocoder gc1 = new Geocoder(this, Locale.getDefault());
        try {
          List<Address> addresses = gc1.getFromLocation(latpoint, lngpoint, 1);
         sb1= new StringBuilder();
          if (addresses.size() > 0) {
             address = addresses.get(0);

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
              sb1.append(address.getAddressLine(i)).append("\n");
              sb1.append(address.getLocality()).append("\n");
            }
         String addressString = sb1.toString();

         marker = map.addMarker(new MarkerOptions().position(
        	        new LatLng(point.latitude, point.longitude)).title(""+addressString));
        	 marker.setDraggable(true);
		 
        
        } catch (IOException e) {}
        



          
          
          
		 
	/*	 
		 
		 
		Mysqlitehelper mysq=new Mysqlitehelper(this);
		SQLiteDatabase datab=mysq.getWritableDatabase();
		 ContentValues values = new ContentValues();
		 values.put("xaxis", point.latitude);
		 values.put("yaxis", point.longitude);
		 values.put("name", "name");
		 datab.insert("location", null, values);
		 datab.close();
       // map.addMarker(marker);
 
      /
		
	} */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		
		double lat=location.getLatitude();
		double loc=location.getLongitude();
		LatLng ll = new LatLng(lat, loc);
		
		if(latpoint==0.0||lngpoint==0.0)
		{
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,15));
			latpoint=0.0001;
			
		}
		
		
		if (distance(lat, loc, latpoint, lngpoint) < 0.1)
		{
			
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
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
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
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
