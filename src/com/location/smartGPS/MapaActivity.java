package com.location.smartGPS;

import java.util.Arrays; 
import java.util.List;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.location.smartGPS.R;

public class MapaActivity extends FragmentActivity/*-MapActivity*/{

private static final float ZOOM = 12;
private static final float ZOOM_IN = 17;


@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mapa_activity_track);

	if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {

		GoogleMap mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa)).getMap();
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		int index = getIntent().getExtras().getInt(BaseActivity.PARAM_INDEX);
		List<Location> locations = getIntent().getExtras().getParcelableArrayList(BaseActivity.PARAM_LOCATIONS);
		if (index<locations.size()) { /*-nomes mostrar l'element d'index "index" */
			locations = Arrays.asList(new Location[] { locations.get(index) } );
		}
		
		LatLng latLng = new LatLng(0, 0);
		for (int i=0; i<locations.size(); i++) {
			Location location =locations.get(i);
			double latitut = location.getLatitude();
			double longitut = location.getLongitude();
			latLng = new LatLng(latitut, longitut);
			String titol = String.format("#%d: %tR",i+1, location.getTime());
			String snippet = String.format("Lat:%.6f, Long:%.6f", latitut, longitut);
			mapa.addMarker(new MarkerOptions().position(latLng).title(titol).snippet(snippet));
		}
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
		mapa.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_IN));
	} else {
		((TextView) findViewById(R.id.textMapa)).setText(R.string.errorMapa);
	}
}
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	return true;
}

/*- ********************************************************************************** */
/*- *********** PRIVATE ************* */
/*- ********************************************************************************** */
}
