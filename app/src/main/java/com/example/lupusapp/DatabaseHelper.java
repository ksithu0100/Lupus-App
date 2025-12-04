package com.example.lupusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "symptoms.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "symptom_logs";

    public static final String COL_ID = "id";
    public static final String COL_DATE = "date";
    public static final String COL_SYMPTOM_NAME = "symptomName";
    public static final String COL_RATING = "rating";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // first time the database is created, building the sql
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE + " TEXT NOT NULL, " +
                COL_SYMPTOM_NAME + "TEXT NOT NULL," +
                COL_RATING + "INTEGER NULL" +
                ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Insert one symptom entry.
     * If rating is null, the column becomes NULL in the database.
     */
    public boolean insertSymptom(String date, String symptomName, Integer rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_DATE, date);
        cv.put(COL_SYMPTOM_NAME, symptomName);

        if (rating != null) {
            cv.put(COL_RATING, rating);
        } else {
            cv.putNull(COL_RATING);
        }

        long result = db.insert(TABLE_NAME, null, cv);
        db.close();

        return result != -1;
    }
}
