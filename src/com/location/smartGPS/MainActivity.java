package com.location.smartGPS;

import com.location.smartGPS.R;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

private static final int MIN_MINUTS = 1;
private static final int MAX_MINUTS = 99;
private static final String PREF_SEGUEIXME_DURANT = "pref_segueixme_durant";
private static final String PREF_GUARDA_CADA = "pref_guarda_cada";

private SharedPreferences preferences;
private EditText editSegueixme;
private EditText editGuarda;


@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	CONFIG_STRICT_MODE();
	setContentView(R.layout.main_activity_track);
	editSegueixme = (EditText) findViewById(R.id.editSegueixme);
	editGuarda = (EditText) findViewById(R.id.editGuarda);
	preferences = getPreferences(MODE_PRIVATE);
	
	editSegueixme.setText(""+preferences.getInt(PREF_SEGUEIXME_DURANT, DEFAULT_SEGUEIXME_DURANT));
	editGuarda.setText(""+preferences.getInt(PREF_GUARDA_CADA, DEFAULT_GUARDA_CADA));
	this.setEventHandlers();
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	return true;
}

@Override
public void onStart() {
	super.onStart();
	activaLocationProviders();
}


private void setEventHandlers() {
	((Button) findViewById(R.id.botoAcceptar)).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			int segueixmeDurant = Integer.parseInt(editSegueixme.getText().toString());
			int guardaCada = Integer.parseInt(editGuarda.getText().toString());
			if (isValid(segueixmeDurant) && isValid(guardaCada)) {
				preferences.edit().putInt(PREF_SEGUEIXME_DURANT, segueixmeDurant).putInt(PREF_GUARDA_CADA, guardaCada).apply();
				Intent intent = new Intent(MainActivity.this, TrackMeActivity.class);
				intent.putExtra(PARAM_SEGUEIXME, segueixmeDurant);
				intent.putExtra(PARAM_GUARDA, guardaCada);
				startActivity(intent);

			} else {
				Toast.makeText(MainActivity.this, R.string.errorEntradaUsuari, Toast.LENGTH_SHORT).show();
			}
		}
	});
}

private boolean isValid(int minuts) {
	return minuts >= MIN_MINUTS && minuts <= MAX_MINUTS;
}


private void activaLocationProviders() {
	LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
	if (!service.isProviderEnabled(LocationManager.GPS_PROVIDER) && !service.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		mostraDialog();
	}
}

private void mostraDialog() {
	String titol = getString(R.string.dialogTitol);
	String msg = getString(R.string.dialogMsg);
	String botoSi = getString(R.string.dialogBotoSi);
	String botoNo = getString(R.string.dialogBotoNo);
	new AlertDialog.Builder(this).setTitle(titol).setMessage(msg).setCancelable(false)
		.setPositiveButton(botoSi, new OnAlertGpsClickListener()).setNegativeButton(botoNo, new OnAlertCancelClickListener())
		.create().show();
}

private class OnAlertGpsClickListener implements DialogInterface.OnClickListener {
@Override
public void onClick(DialogInterface dialog, int which) {
	startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
}
}

private class OnAlertCancelClickListener implements DialogInterface.OnClickListener {
@Override
public void onClick(DialogInterface dialog, int which) {
	MainActivity.this.finish();
}
}
}