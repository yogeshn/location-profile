package com.location.smartGPS;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class pedometerActivity extends Activity implements SensorEventListener{

	private int mStepValue;
	private final static int SENSITIVITY = 13;
	
	private boolean mInitialized;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStepValue = 0;
		setContentView(R.layout.pedometer_main);
		mInitialized = false;
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	TextView numberSteps = (TextView)findViewById( R.id.textView2);
            	numberSteps.setText(Integer.toString(0));
            }
        });
        System.out.println("ONCREATE");
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  TextView numberSteps = (TextView)findViewById( R.id.textView2);
  	  numberSteps.setText(numberSteps.getText()); 
  	  System.out.println("CHAANGED");
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public void onSensorChanged(SensorEvent event){
		// alpha is calculated as t / (t + dT)
		// with t, the low-pass filter's time-constant
		// and dT, the event delivery rate
		
		TextView numberSteps = (TextView)findViewById( R.id.textView2);


		if (!mInitialized) {
			mInitialized = true;
		} else {
			float[] gravity = new float[3];
			float[] linear_acceleration = new float[3];

			final float alpha = (float) 0.8;

			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

			linear_acceleration[0] = event.values[0] - gravity[0];
			linear_acceleration[1] = event.values[1] - gravity[1];
			linear_acceleration[2] = event.values[2] - gravity[2];
			System.out.println(linear_acceleration[0]+" , "+ linear_acceleration[1]+" , "+linear_acceleration[2]+" , ");

			mStepValue = Integer.parseInt((String) numberSteps.getText());
			if(/*linear_acceleration[0] > SENSITIVITY || linear_acceleration[1] > SENSITIVITY ||*/ linear_acceleration[2] > SENSITIVITY && linear_acceleration[2]>0){
				System.out.println("ONEEEE STEPP");
				numberSteps.setText(Integer.toString(++mStepValue));
				System.out.println(numberSteps);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy){

	}
	


}
