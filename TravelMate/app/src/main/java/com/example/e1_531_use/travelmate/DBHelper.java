package com.example.e1_531_use.travelmate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by E1-531-USE on 2016/5/25.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TravelMate";
    private static final int DATABASE_VERSION = 1;
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ProcessList("
                                        + "Process_ID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
                                        + "ProcessName VARCHAR,"
                                        + "ProcessAddress VARCHAR,"
                                        + "ProcessPx VARCHAR,"
                                        + "ProcessPy VARCHAR,"
                                        + "ProcessDepartment INTEGER"
                                        + ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ProcessList");
        onCreate(db);
    }
}