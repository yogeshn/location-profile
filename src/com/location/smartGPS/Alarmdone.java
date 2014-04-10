package com.location.smartGPS;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Alarmdone extends Activity{
	MediaPlayer mp;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmdone);
		mp=new MediaPlayer().create(this, R.raw.mediumtone);
		mp.start();
		Button bt=(Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mp.stop();
			}
		});
	}

}
