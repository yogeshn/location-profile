package com.location.smartGPS;

import java.io.File; 
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.location.smartGPS.R;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;


public class MainActivitynavigation extends Activity implements LocationListener{
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainnavigation);
		Toast.makeText(getApplicationContext(), "Turn on GPS and Mobile Data for accurate tracking!", Toast.LENGTH_SHORT).show();
		
		
		//CREATING FOLDER
		File direct = new File(Environment.getExternalStorageDirectory() + "/Trip Log");
        if(!direct.exists())
         {
             if(direct.mkdir()); //directory is created;
         }
		if(savedInstanceState!=null)
		{
			if(savedInstanceState.containsKey("points")){
                points = savedInstanceState.getParcelableArrayList("points");
                if(points!=null){
                    for(int i=1;i<points.size();i++)
                    {
                       // drawMarker(points.get(i));
                        drawPolyLine(points.get(i-1),points.get(i));
                    }
                }
            }
		}
		 
		 // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
 
        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        }else 
        { // Google Play Services are available
		
		
	//SETTING UP MAP
	mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	mMap.setPadding(0, 0, 0, 0);
	mMap.setMyLocationEnabled(true);
	mMap.setPadding(0,100, 0, 0);
		
	
	//DEFINING LOCATION MANAGER 
		  locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		  final boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		 // if(!statusOfGPS)
			//    turnGPSOn();
		  requestLocationUpdates();
		  
		      
	//ASYNCHRONOUS TIMER TASK THAT CHECKS LOCATION REGULARY,
	//CREATES A MARKER FOR THAT LOCATION AND WRITES THAT LOCATION INTO A TEXT FILE
		  
		  stopTimer=false;
		  final Handler handler = new Handler();
		  Timer timer = new Timer();
		  TimerTask doAsynchronousTask = new TimerTask() {       
		   @Override
		  public void run()
		   {
			   if(stopTimer!=true){
		     handler.post(new Runnable() 
		     {
		       public void run() 
		          { 
		         	try {
		         		file = new File(Environment.getExternalStorageDirectory().getPath()+"/GPS.txt");
	          			if(!file.exists())
	          		    {
	          					file.createNewFile();
	          					
	          		    }
	          			out = new FileWriter(file,true);
		         		  
		          		  if((currentBestLocation!=null)&&(currentBestLocation.getAccuracy()<100))
		          		  { 
		          			if(currentBestLocation.getProvider()==LocationManager.GPS_PROVIDER)
		          				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,MainActivitynavigation.this);
		          			
		          		   // Toast.makeText(getApplicationContext(), "LOCATION DETERMINED", Toast.LENGTH_SHORT).show();	
		          			latitude=currentBestLocation.getLatitude();
		          			
		          			longitude=currentBestLocation.getLongitude();
		          			latlng=new LatLng(latitude,longitude);
		          			
		          		//MOVE CAMERA TO POSITION OF NEWLY CREATED MARKER
		          			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,17.0f));
		          			
		          			count++;
		          		//	drawMarker(latlng);
		          			points.add(++i,latlng);
		          			if(i!=0)
		          			{ src = points.get(i-1);
		             		  dest =points.get(i);
		             		  drawPolyLine(src,dest);
		          			}
		          			String Text = "\n"+count+".\t"+"Latitude =" +latitude+"\n\tLongitude ="+ longitude+"\n\t";
		          			Geocoder geo = new Geocoder(MainActivitynavigation.this, Locale.getDefault());
		          			List<Address> addresses;
		          			addresses = geo.getFromLocation(latitude,longitude, 1);
		          				if (addresses.size() > 0) 
		          				{       
		          					String message =getAddress(addresses.get(0).toString());
		          					String dateTime = DateFormat.getDateTimeInstance().format(new Date());
		          						out.write(Text);
		          						out.write(message);
		          						out.write("\n\t"+dateTime+"\n\n");
		          						//Toast.makeText(getApplicationContext(), "WRITTEN INTO FILE", Toast.LENGTH_SHORT).show();	
		          				}
		          				
		          				
		          		   }
		          		    else
		          		    {   
		          		    	//Toast.makeText(getApplicationContext(), "LOCATION NOT FOUND", Toast.LENGTH_SHORT).show();
		          		    	String dateTime = DateFormat.getDateTimeInstance().format(new Date());
		          				 out.write("\n\tLocation is unavailable");
		          				 out.write("\n\t"+dateTime+"\n\n");
		          				}
		          		out.close();
		          			}
		          		
		                 catch (Exception e) {
		                        // TODO Auto-generated catch block
		                    }
		                }
		            });
		        }
		   } };
		  
		    timer.schedule(doAsynchronousTask, 0,20000); 
      }
        
        ExitDialogFragment alertSave = new ExitDialogFragment();
        Button exit = (Button) findViewById(R.id.ExitButton);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
            	//Toast.makeText(getApplicationContext(), "EXIT BUTTON CLICKED", Toast.LENGTH_SHORT).show();
            	ExitDialogFragment dialog = new ExitDialogFragment();
            	
            	dialog.show(getFragmentManager(), "DialogFragment");
            }
         });
        
     
        
        final Button pause = (Button) findViewById(R.id.StopButton);
        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
            	if(stopTimer==false){
            		removeUpdates();
            		pause.setText("Resume");
            	//	turnGPSOff();
            		stopTimer=true;
            	}
            	else{
            		stopTimer=false;
            		requestLocationUpdates();
            		pause.setText("Pause");
            	}
            }
         });
	}
	
	public void removeUpdates()
	{
		locationManager.removeUpdates(this);
	}
	
	 public void terminate()
     {
  	  removeUpdates();
  	   finish();
     }
	
	 
	 
	 public void save(String value)
	 {
		 
		   // Do something with value!
		Toast.makeText(getApplicationContext(),value, Toast.LENGTH_SHORT).show();
		   try{
			   File dir = new File(Environment.getExternalStorageDirectory() + "/Trip Log/"+value);
		        if(!dir.exists())
		         {
		             if(dir.mkdir()); //directory is created;
		         }
			File pointFile = new File(Environment.getExternalStorageDirectory().getPath()+"/Trip Log/"+value+"/points.txt");
			if(!pointFile.exists())
  		    {
  					pointFile.createNewFile();
  					
  		    }
			
			 FileWriter coordinates=new FileWriter(pointFile);
			 if(points!=null){
                 for(int i=0;i<points.size();i++)
                 {
                 LatLng l=points.get(i);
			     coordinates.write(l.latitude+"\t"+l.longitude+"\n\n");
		   }
	      }
			 coordinates.close();
		 }
			catch (Exception e) {
                // TODO Auto-generated catch block
            }
	 }
		 
	 
	 
	 public void requestLocationUpdates()
	 {
		// turnGPSOn();
		 locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2*60*1000, 0, this);
		  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2*60*1000, 0,this);
		  locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,0,0,this);
		//  Toast.makeText(getApplicationContext(), "LOCATION LISTENERS CALLED", Toast.LENGTH_SHORT).show();
	 }
	 
	 
	//DRAW A POLYLINE
	private void drawPolyLine(LatLng src, LatLng dest)
	{
		{
     		
     		mMap.addPolyline(new PolylineOptions() //mMap is the Map Object
     		.add(src,dest)
     		.width(5)
     		.color(Color.BLUE)
     		.geodesic(true));	
		}
	}
	
	// Draw a marker at the "point"
    private void drawMarker(LatLng point)
    {
    	mMap.addMarker(new MarkerOptions()
			 .position(latlng)
			 .title(String.valueOf(count))
			 .visible(true)
			 .draggable(true) );
        
    }
	
//METHOD TO SET UP THE MAP IF NOT CREATED	
	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	                            .getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null) {
	            // The Map is verified. It is now safe to manipulate the map.

	        }
	    }
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		currentBestLocation=location;
		
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	
	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	//SAVE INSTANCE STATE
	protected void onSaveInstanceState(Bundle outState)
	{
	    outState.putParcelableArrayList("points", points);
	    super.onSaveInstanceState(outState);
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public String getAddress(String s)
	{
	String modified="";
	char x;
	int i;
	 for( i=0;i<s.length();i++)
	 {
		 if(s.charAt(i)=='[')
			 break;
	 }
	while((x=s.charAt(++i))!=']')
	 {
		if(x!='['&&x!=']'&&x!='"')
		modified=modified+x;
	 }
	 return modified;
	}
   
	
	//TURN ON GPS
	    private void turnGPSOn(){   

		    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);   
		    if(!provider.contains("gps")){      
		        final Intent poke = new Intent();  
		        poke.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");           poke.addCategory(Intent.CATEGORY_ALTERNATIVE);   
		        poke.setData(Uri.parse("3"));      
		        sendBroadcast(poke);  
		   }  }    

	    
	    //TURN OFF GPS
		public void turnGPSOff()
		{
		  String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		  if(provider.contains("gps")){ //if gps is enabled
		      final Intent poke = new Intent();
		      poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		      poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		      poke.setData(Uri.parse("3")); 
		      sendBroadcast(poke);
		  }
		}

		
		//TURN ON DATA
		boolean turnOnDataConnection(boolean ON,Context context)
	    {

	        try{        	
	                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	                final Class<?> conmanClass = Class.forName(conman.getClass().getName());
	                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
	                iConnectivityManagerField.setAccessible(true);
	                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
	                final Class<?> iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
	                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	                setMobileDataEnabledMethod.setAccessible(true);
	                setMobileDataEnabledMethod.invoke(iConnectivityManager, ON);
	           // }
	            return true;
	        }
	        catch(Exception e){
	            return false;
	       }     

	    }

}


 class ExitDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to save before exiting?")
               .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Save and then exit
                	   
                	   SaveDialog save=new SaveDialog();
                	   save.show(getFragmentManager(), "save");
                	   
                   }
               })
               .setNegativeButton("No!", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Exit without saving
                	   dismiss();
                	   
                	   ((MainActivitynavigation) getActivity()).terminate();
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
 
 class SaveDialog extends DialogFragment
 {
	
	 public Dialog onCreateDialog(Bundle savedInstanceState)
	 {
	  AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
	  alert.setMessage("Enter the name of your trip");

	 // Set an EditText view to get user input 
	 final EditText input = new EditText(getActivity());
	 alert.setView(input);
	 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	 public void onClick(DialogInterface dialog, int whichButton) {
	   Editable value = input.getText();
	   
	   ((MainActivitynavigation)getActivity()).save(String.valueOf(value));
	   ((MainActivitynavigation) getActivity()).terminate();
   }
	 });
	 
	 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int whichButton) {
		     // Canceled.
			   dialog.dismiss();
		   }
		 });
	 return alert.create();
 }
 }