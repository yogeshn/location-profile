package com.location.smartGPS;

import java.io.File; 
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.location.smartGPS.R;

public class MainActivityold extends Activity implements LocationListener , OnMapClickListener ,OnClickListener {

	
	GoogleMap map;
	private GoogleMap mMap;
	GoogleMapOptions options = new GoogleMapOptions();
	CameraPosition cameraPosition;
	LatLng latlng;
	MapFragment mMapFragment;
	private LocationManager locationManager;
	protected LocationListener listener;
	private static Location currentBestLocation = null;
	private double latitude;
	private double longitude;
	private int count=0;
	File file;
	FileWriter out;
	ArrayList<LatLng> points=new ArrayList<LatLng>();
	LatLng src,dest;
	private int i=-1;
	boolean stopTimer;
	
	private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
	SQLiteDatabase datab;
	Marker marker;
	 double latpoint;
     double lngpoint;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityold);
    
 //  map=((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
 //  map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
       LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
       lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
      //  map.clear();
        
        
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
       mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
         // mDrawerList.setOnClickListener(this);
       
        // Set the adapter for the list view
      mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
       mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
      map.setOnMapClickListener(this);
        
    }
    
    
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // marker.remove();
        return true;
        
    }

    
    
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		map.clear();
		latpoint=0.0;
		return super.onOptionsItemSelected(item);
		
	}





	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	//	map.clear();
		Address address;
	//		Toast.makeText(this, "working", Toast.LENGTH_LONG).show();

			double lat=location.getLatitude();
			double loc=location.getLongitude();
			LatLng ll = new LatLng(lat, loc);
		//	Toast.makeText(this,"my "+latpoint, Toast.LENGTH_LONG).show();	
			if(latpoint==0.0||lngpoint==0.0)
			{
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,15));
				latpoint=0.0001;
				
			}
/*	          StringBuilder sb2;
			  Geocoder gc2 = new Geocoder(this, Locale.getDefault());
	          try {
	            List<Address> addresses = gc2.getFromLocation(lat, loc, 1);
	           sb2= new StringBuilder();
	            if (addresses.size() > 0) {
	               address = addresses.get(0);

	              for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
	                sb2.append(address.getAddressLine(i)).append("\n");

	                sb2.append(address.getLocality()).append("\n");
	                sb2.append(address.getPostalCode()).append("\n");
	                sb2.append(address.getCountryName());
	            }
	           String addressString = sb2.toString();
	           Toast.makeText(this,""+addressString, Toast.LENGTH_LONG).show();

	  		 
	          
	          } catch (IOException e) {}
*/
			//		LatLng latloc =marker.getPosition();
	//		double l=latloc.latitude;
//			Toast.makeText(this, ""+lat +"long"+loc, Toast.LENGTH_LONG).show();
			double lat1=lat+0.000100;
			//if((latpoint>lat-0.000100 || latpoint<lat1 && lngpoint>loc-0.000100||lngpoint<loc+0.000100))
			if (distance(lat, loc, latpoint, lngpoint) < 0.1) { // if distance < 0.1 miles we take locations as equal
			       //do what you want to do...
				Toast.makeText(this,"my lat "+lat+"new lat"+lat1, Toast.LENGTH_LONG).show();
				Toast.makeText(this,"my "+latpoint, Toast.LENGTH_LONG).show();
	   			AudioManager audMangr;
	   			audMangr= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);

	   			

	   			//For Silent mode
		   			audMangr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

			    }	
/*			{			
				
				Toast.makeText(this,"my lat "+lat+"new lat"+lat1, Toast.LENGTH_LONG).show();
				Toast.makeText(this,"my "+latpoint, Toast.LENGTH_LONG).show();
	   			AudioManager audMangr;
	   			audMangr= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);

	   			

	   			//For Silent mode
		   			audMangr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		   } */
    else
   {
	   AudioManager audMangr;
			audMangr= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
	   audMangr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
   }
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
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
 
      */
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activityold, menu);
        return true;
    }*/
	
	
	
	
	 private class DrawerItemClickListener implements ListView.OnItemClickListener {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	           selectItem(position);
	        	
	           if(position==4)
	            {
	            	Intent i=new Intent(view.getContext(),GPSSettingActivity.class);
		        	startActivity(i);
	            }
	           
	           
	           
	           if(position==2)
	            {
	            	Intent i=new Intent(view.getContext(),MainActivity.class);
		        	startActivity(i);
	            }


	           if(position==0)
	            {
	            	Intent i=new Intent(view.getContext(),MainActivitynavigation.class);
		        	startActivity(i);
	            }
	           
	           if(position==1)
	           {
	        	   Intent i=new Intent(view.getContext(),pedometerActivity.class);
		        	startActivity(i);
	           }

	           if(position==3)
	           {
	        	   Intent i=new Intent(view.getContext(),LocAlarmActivity.class);
		        	startActivity(i);
	           }

	           //gs.openAndQueryDatabase();
	        }

	        private void selectItem(int position) {
	            // update the main content by replacing fragments
	            Fragment fragment = new Fragment();
	            Bundle args = new Bundle();
	          //  args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
	            fragment.setArguments(args);

	            FragmentManager fragmentManager = getFragmentManager();
	            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

	            // update selected item and title, then close the drawer
	            mDrawerList.setItemChecked(position, true);
	            	
	            
	            setTitle(mPlanetTitles[position]);
	            mDrawerLayout.closeDrawer(mDrawerList);
	        }
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


