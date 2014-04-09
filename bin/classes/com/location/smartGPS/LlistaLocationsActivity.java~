package com.example.locationprofile;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LlistaLocationsActivity extends BaseActivity {

/*- ********************************************************************************** */
/*- *********** OVERRIDE ************* */
/*- ********************************************************************************** */
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.llista_locations_activity_track); /*-no caldria si fos una ListActivity*/
	this.setList();
	this.setEventHandlers();
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	return true;
}

/*- ********************************************************************************** */
/*- *********** PRIVATE ************* */
/*- ********************************************************************************** */
private void setList() {
	final ArrayList<Parcelable> locations = getIntent().getExtras().getParcelableArrayList(PARAM_LOCATIONS);
	final ListView list = (ListView) findViewById(R.id.listResultat);    
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, locationsToText(locations));	
	list.setAdapter(adapter);
	list.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getCount()==1) {
			return;
		}
	    Intent intent = new Intent(LlistaLocationsActivity.this, MapaActivity.class);
	    intent.putExtra(PARAM_INDEX, position);    
		intent.putParcelableArrayListExtra(PARAM_LOCATIONS, locations);
	    startActivity(intent);	   			
		}
	});
}

private void setEventHandlers() {}

/*- ********************************************************************************** */
/*- *********** PRIVATE ************* */
/*- ********************************************************************************** */
@SuppressLint("DefaultLocale")
private List<String> locationsToText(ArrayList<Parcelable> locations) {
	List<String> ret = new ArrayList<String>();
	if (locations.size() == 0) {
		ret.add(getString(R.string.noHiHaLocations));
	} else {
		for (Parcelable p : locations) {
			Location l = (Location) p;
			String lText = String.format("%tR - Lat:%.8f Long:%.8f", l.getTime(), l.getLatitude(),l.getLongitude());
			ret.add(lText);
		}
		ret.add(getString(R.string.toesLesLocations));
	}
	return ret;
}
		
}
