package com.location.smartGPS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public  class Mysqlitehelper extends SQLiteOpenHelper
{

	public Mysqlitehelper(Context context) {
	    super(context, "CursorDemo", null, 1);
	}
	
	public void onCreate(SQLiteDatabase db) {
	    
		 db.execSQL("CREATE TABLE IF NOT EXISTS location ("
		            + BaseColumns._ID
		            + " INTEGER PRIMARY KEY AUTOINCREMENT, xaxis DOUBLE, Yaxis DOUBLE, name VARCHAR)");
		   
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query;
        query = "DROP TABLE IF EXISTS location";
        db.execSQL(query);
        onCreate(db);



	}


}
