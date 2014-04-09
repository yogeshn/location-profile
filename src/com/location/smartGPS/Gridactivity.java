package com.location.smartGPS;

import com.location.smartGPS.R;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;
import android.widget.Toast;

public class Gridactivity extends Activity {

	SQLiteDatabase   newDB;
String xaxis,yaxis,name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gridactivity);
		
		/*

		try {
             Mysqlitehelper dbHelper = new Mysqlitehelper(this.getApplicationContext());
             SQLiteDatabase   newDB = dbHelper.getWritableDatabase();
              Cursor c = newDB.rawQuery("SELECT * FROM location", null);
        if (c != null ) {
              if  (c.moveToFirst()) {
                    do {
                          String xaxis = c.getString(c.getColumnIndex("xaxis"));
                          String yaxis = c.getString(c.getColumnIndex("yaxis"));
                          String name = c.getString(c.getColumnIndex("name"));
                         
					 
                    }while (c.moveToNext());
                    Toast.makeText(this, ""+xaxis+"", Toast.LENGTH_LONG);
                    
              }
        }                
        } catch (SQLiteException se ) {
        Log.e(getClass().getSimpleName(), "Could not create or Open the database");
    } finally {
        if (newDB != null)
              newDB.execSQL("DELETE FROM location");
              newDB.close();
    }*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gridactivity, menu);
		return true;
	}

	

		}

