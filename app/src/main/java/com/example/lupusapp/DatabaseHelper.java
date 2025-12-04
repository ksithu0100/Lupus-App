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
    public boolean insertSymptomRatings(int s1, int s2, int s3, int s4, int s5, int s6, int s7) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("symptom 1", s1);
        values.put("symptom 2", s2);
        values.put("symptom 3", s3);
        values.put("symptom 4", s4);
        values.put("symptom 5", s5);
        values.put("symptom 6", s6);
        values.put("symptom 7", s7);
        long result = db.insert("symptom_log", null, values);
        return result != -1; // true if insert succeeded
    }

}
