package com.location.smartGPS;
//
import java.util.ArrayList; 

import com.location.smartGPS.R;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class TrackMeActivity extends BaseActivity {

private static final int PINTENT_OBTE_LOCATIONS = 888;
private static final int PINTENT_NOTIFICA_LOCATIONS = 889;
private static final int PINTENT_ATURA_OBTENIR_LOCATION = 890;
private static final int PINTENT_OBTINGUDA_LOCATION = 891;
private static final String ACTION_OBTE_LOCATIONS = "action_obte_LOCATIONS";
private static final String ACTION_NOTIFICA_LOCATIONS = "action_notifica_LOCATIONS";
private static final String ACTION_ATURA_OBTENIR_LOCATION = "action_atura_LOCATIONS";
private static final String ACTION_OBTINGUDA_LOCATION = "action_obtinguda_LOCATION";

private static final int DURACIO_OBTENIR_LOCATIONS = 30000; /*-30 segons*/
private static final int INTERVAL_OBTENIR_LOCATIONS = 5000; /*-5 segons*/

private NotificationManager notificationManager;
private LocationManager locationManager;
private AlarmManager alarmManager;
private PendingIntent obteLocationsIntent;
private PendingIntent obtingudaLocationIntent;
private PendingIntent aturaObtenirLocationIntent;
private PendingIntent notificaLocationsIntent;
private long segueixmeDurant;
private long guardaCada;
private ArrayList<Location> locations;
private Criteria criteria;

/*- ********************************************************************************** */
/*- *********** OVERRIDE ************* */
/*- ********************************************************************************** */
@Override
protected void onCreate(Bundle bundle) {
	super.onCreate(bundle);
	setContentView(R.layout.track_me_activity);

	notificationManager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
	locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	alarmManager =(AlarmManager)getSystemService(ALARM_SERVICE);
	criteria = iniCriteria();
	segueixmeDurant = getIntent().getIntExtra(PARAM_SEGUEIXME, DEFAULT_SEGUEIXME_DURANT) * 60 * 1000;
	guardaCada = getIntent().getIntExtra(PARAM_GUARDA, DEFAULT_GUARDA_CADA) * 60 * 1000;
	registerReceiver(obteLocationsReceiver,new IntentFilter(ACTION_OBTE_LOCATIONS));
	registerReceiver(obteLocationsReceiver,new IntentFilter(ACTION_OBTINGUDA_LOCATION));
	registerReceiver(obteLocationsReceiver,new IntentFilter(ACTION_ATURA_OBTENIR_LOCATION));
	registerReceiver(notificaLocationsReceiver,new IntentFilter(ACTION_NOTIFICA_LOCATIONS));

	if (restoreLocations(bundle)==false) {
		this.setAlarmes();
	}
	this.setEventHandlers();
}

@Override
protected void onResume() {
	super.onResume();
	IntentFilter iFilter = new IntentFilter(ACTION_NOTIFICA_LOCATIONS);
	iFilter.setPriority(1);
	registerReceiver(mostraLlistaDeLocationsReceiver,iFilter);
}

@Override
protected void onPause() {
	unregisterReceiver(mostraLlistaDeLocationsReceiver);
	super.onPause();
}

@Override
protected void onDestroy() {
	unregisterReceiver(obteLocationsReceiver);
	unregisterReceiver(notificaLocationsReceiver);
	super.onDestroy();
}

@Override
protected void onSaveInstanceState(Bundle bundle) {
	super.onSaveInstanceState(bundle);
	bundle.putParcelableArrayList(PARAM_LOCATIONS, locations);
}

public void onConfigurationChanged(Configuration newConfig) { /*-evita refer l'activity quan canvia la orientacio*/
	super.onConfigurationChanged(newConfig);
	setContentView(R.layout.track_me_activity);
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	return true;
}

@Override
public void onBackPressed() {}


private void setAlarmes() {
	obteLocationsIntent = PendingIntent.getBroadcast(this, PINTENT_OBTE_LOCATIONS, new Intent(ACTION_OBTE_LOCATIONS), NO_FLAGS);
	alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), guardaCada,
		obteLocationsIntent);
	notificaLocationsIntent = PendingIntent.getBroadcast(this, PINTENT_NOTIFICA_LOCATIONS, new Intent(ACTION_NOTIFICA_LOCATIONS),
		NO_FLAGS);
	alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + segueixmeDurant
		+ DURACIO_OBTENIR_LOCATIONS, notificaLocationsIntent);
}

private void setEventHandlers() {
	((Button) findViewById(R.id.botoCancelar)).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			notificationManager.cancel(NOTIFICATION_TRACKME);
			alarmManager.cancel(aturaObtenirLocationIntent);
			alarmManager.cancel(obteLocationsIntent);
			alarmManager.cancel(notificaLocationsIntent);
			finish();
		}
	});
}

private boolean restoreLocations(Bundle bundle) {
	boolean ret = (bundle!=null);/*- && bundle.containsKey(PARAM_LOCATIONS)*/;
	if (ret) {
		locations = bundle.getParcelableArrayList(PARAM_LOCATIONS);
	} else {
		locations = new ArrayList<Location>();
	}
	return ret;
}	

private Criteria iniCriteria() {
	Criteria ret = new Criteria();
	ret.setAccuracy(Criteria.ACCURACY_FINE);
	return ret;
}

private ObteLocationsReceiver obteLocationsReceiver = new ObteLocationsReceiver();
private class ObteLocationsReceiver extends BroadcastReceiver {
private Location bestLocation = null;

@Override
public synchronized void onReceive(Context c, Intent i) {
	String action = i.getAction();

	if (ACTION_OBTE_LOCATIONS.equals(action)) {
		LOG(action);
		bestLocation = null;
		obtingudaLocationIntent = PendingIntent.getBroadcast(TrackMeActivity.this, PINTENT_OBTINGUDA_LOCATION, new Intent(
			ACTION_OBTINGUDA_LOCATION), NO_FLAGS);
		locationManager.requestLocationUpdates(INTERVAL_OBTENIR_LOCATIONS, 1, criteria, obtingudaLocationIntent);
		aturaObtenirLocationIntent = PendingIntent.getBroadcast(TrackMeActivity.this, PINTENT_ATURA_OBTENIR_LOCATION, new Intent(
			ACTION_ATURA_OBTENIR_LOCATION), NO_FLAGS);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + DURACIO_OBTENIR_LOCATIONS,
			aturaObtenirLocationIntent);

	} else if (ACTION_OBTINGUDA_LOCATION.equals(action)) {
		Location location = (Location) i.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
		if (bestLocation == null || bestLocation.hasAccuracy() == false || location.getAccuracy() < bestLocation.getAccuracy()) {
			bestLocation = location;
		}

	} else if (ACTION_ATURA_OBTENIR_LOCATION.equals(action)) {
		locationManager.removeUpdates(obtingudaLocationIntent);
		if (bestLocation != null) {
			locations.add(bestLocation);
			LOG(action + " Best=" + bestLocation.getProvider() + " Acc=" + bestLocation.getAccuracy());
		} else if (locations.size() > 0) { /*-repetim darrera location*/
			locations.add(locations.get(locations.size() - 1));
		}
	}
}
}
private BroadcastReceiver notificaLocationsReceiver = new BroadcastReceiver() {
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context c, Intent i) {
		TrackMeActivity _this = TrackMeActivity.this;
		alarmManager.cancel(aturaObtenirLocationIntent);
		alarmManager.cancel(obteLocationsIntent);
		Intent intent = new Intent(_this, LlistaLocationsActivity.class);
		intent.putParcelableArrayListExtra(PARAM_LOCATIONS, locations);

		PendingIntent pIntent = PendingIntent.getActivity(_this, PINTENT_NOTIFICA_LOCATIONS, intent, PendingIntent.FLAG_ONE_SHOT);
	//	Notification noti = new Notification(R.drawable.ic_launcher, "", System.currentTimeMillis());
	//	noti.setLatestEventInfo(_this, getString(R.string.notiTitol), getString(R.string.notiDescripcio), pIntent);
	//	noti.flags |= Notification.FLAG_AUTO_CANCEL;
	//	notificationManager.notify(NOTIFICATION_TRACKME, noti);
		abortBroadcast();
		finish();
	}
};

private BroadcastReceiver mostraLlistaDeLocationsReceiver = new BroadcastReceiver() {
	@Override
	public void onReceive(Context c, Intent i) {
		TrackMeActivity _this = TrackMeActivity.this;
		alarmManager.cancel(aturaObtenirLocationIntent);
		alarmManager.cancel(obteLocationsIntent);
		Intent intent = new Intent(_this, LlistaLocationsActivity.class);
		intent.putParcelableArrayListExtra(PARAM_LOCATIONS, locations);

		notificationManager.cancel(NOTIFICATION_TRACKME);
		startActivity(intent);
		abortBroadcast();
		finish();
	}
};

}