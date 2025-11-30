package com.example.lupusapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static final String PREFS_NAME = "prefs";
    private static final String KEY_ONBOARDING_COMPLETE = "onboardingComplete";

    // Symptom keys
    private static final String KEY_SYMPTOM1 = "symptom1";
    private static final String KEY_SYMPTOM2 = "symptom2";
    private static final String KEY_SYMPTOM3 = "symptom3";
    private static final String KEY_SYMPTOM4 = "symptom4";
    private static final String KEY_SYMPTOM5 = "symptom5";
    private static final String KEY_SYMPTOM6 = "symptom6";
    private static final String KEY_SYMPTOM7 = "symptom7";

    // You can access the syptoms in fragments like this:
    // boolean s1 = PreferencesManager.getSymptom1(this);

    // Using this method as entrypoint for shared prefs (where we keep the data). onboarding stuff will need a revamp but we can probably get away with it for now
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Call this if user completes first time preferences
    public static void setOnboardingComplete(Context context, boolean complete) {
        getPrefs(context).edit().putBoolean(KEY_ONBOARDING_COMPLETE, complete).apply();
    }

    // Call this to check if user has completed their first time preferences
    public static boolean isOnboardingComplete(Context context) {
        return getPrefs(context).getBoolean(KEY_ONBOARDING_COMPLETE, false);
    }

    //Call this at the end of a preferences page (First time login, symptom settings, etc)
    public static void saveSymptoms(Context context, boolean s1, boolean s2, boolean s3, boolean s4, boolean s5, boolean s6, boolean s7) {
        getPrefs(context).edit()
                .putBoolean(KEY_SYMPTOM1, s1)
                .putBoolean(KEY_SYMPTOM2, s2)
                .putBoolean(KEY_SYMPTOM3, s3)
                .putBoolean(KEY_SYMPTOM4, s4)
                .putBoolean(KEY_SYMPTOM5, s5)
                .putBoolean(KEY_SYMPTOM6, s6)
                .putBoolean(KEY_SYMPTOM7, s7)
                .apply();
    }

    public static boolean getSymptom1(Context context) { return getPrefs(context).getBoolean(KEY_SYMPTOM1, false); }
    public static boolean getSymptom2(Context context) { return getPrefs(context).getBoolean(KEY_SYMPTOM2, false); }
    public static boolean getSymptom3(Context context) { return getPrefs(context).getBoolean(KEY_SYMPTOM3, false); }
    public static boolean getSymptom4(Context context) { return getPrefs(context).getBoolean(KEY_SYMPTOM4, false); }
    public static boolean getSymptom5(Context context) { return getPrefs(context).getBoolean(KEY_SYMPTOM5, false); }
    public static boolean getSymptom6(Context context) { return getPrefs(context).getBoolean(KEY_SYMPTOM6, false); }
    public static boolean getSymptom7(Context context) { return getPrefs(context).getBoolean(KEY_SYMPTOM7, false); }
}
