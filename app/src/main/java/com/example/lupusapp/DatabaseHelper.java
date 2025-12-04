package com.example.lupusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "symptoms.db";
    private static final int DATABASE_VERSION =  4;
    public static final String TABLE_NAME = "symptom_log";

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
                COL_DATE + " TEXT NOT NULL UNIQUE, " +
                "symptom1 INTEGER, " +
                "symptom2 INTEGER, " +
                "symptom3 INTEGER, " +
                "symptom4 INTEGER, " +
                "symptom5 INTEGER, " +
                "symptom6 INTEGER, " +
                "symptom7 INTEGER" +
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
        values.put("symptom1", s1);
        values.put("symptom2", s2);
        values.put("symptom3", s3);
        values.put("symptom4", s4);
        values.put("symptom5", s5);
        values.put("symptom6", s6);
        values.put("symptom7", s7);
        String today = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now().toString();
        }
        values.put("date", today);
        long result = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    public String getLogSummary(Context context, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_DATE + "=?", new String[]{date}, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        StringBuilder sb = new StringBuilder();

        if (PreferencesManager.getSymptom1(context)) {
            sb.append("General Pain: ").append(cursor.getInt(cursor.getColumnIndexOrThrow("symptom1"))).append("\n");
        }
        if (PreferencesManager.getSymptom2(context)) {
            sb.append("Mood: ").append(cursor.getInt(cursor.getColumnIndexOrThrow("symptom2"))).append("\n");
        }
        if (PreferencesManager.getSymptom3(context)) {
            sb.append("Skin: ").append(cursor.getInt(cursor.getColumnIndexOrThrow("symptom3"))).append("\n");
        }
        if (PreferencesManager.getSymptom4(context)) {
            sb.append("Memory: ").append(cursor.getInt(cursor.getColumnIndexOrThrow("symptom4"))).append("\n");
        }
        if (PreferencesManager.getSymptom5(context)) {
            sb.append("Rash: ").append(cursor.getInt(cursor.getColumnIndexOrThrow("symptom5"))).append("\n");
        }
        if (PreferencesManager.getSymptom6(context)) {
            sb.append("Symptom 6: ").append(cursor.getInt(cursor.getColumnIndexOrThrow("symptom6"))).append("\n");
        }
        if (PreferencesManager.getSymptom7(context)) {
            sb.append("Symptom 7: ").append(cursor.getInt(cursor.getColumnIndexOrThrow("symptom7"))).append("\n");
        }

        cursor.close();
        if (sb.length() == 0) {
            return "No symptoms selected.";
        }
        return sb.toString();
    }

    public Set<String> getAllLoggedDates() {
        Set<String> dates = new HashSet<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{COL_DATE}, null, null, null, null, null);
        while (c.moveToNext()) {
            dates.add(c.getString(c.getColumnIndexOrThrow(COL_DATE)));
        }
        c.close();
        return dates;
    }
}
