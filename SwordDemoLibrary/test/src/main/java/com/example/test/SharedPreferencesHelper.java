package com.example.test;

import android.content.SharedPreferences;

import java.util.Calendar;

public class SharedPreferencesHelper {
    static final String KEY_NAME = "key_name";
    static final String KEY_DOB = "key_dob_millis";
    static final String KEY_EMAIL = "key_email";

    private final SharedPreferences mSharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sp) {
        mSharedPreferences = sp;
    }

    public boolean savePersonalInfo(SharedPreferenceEntry entry) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_NAME, entry.getName());
        editor.putLong(KEY_DOB, entry.getBirth().getTimeInMillis());
        editor.putString(KEY_EMAIL, entry.getEmail());
        return editor.commit();
    }

    public SharedPreferenceEntry getPersonalInfo() {
        String name = mSharedPreferences.getString(KEY_NAME, "");
        Calendar birth = Calendar.getInstance();
        birth.setTimeInMillis(mSharedPreferences.getLong(KEY_DOB, Calendar.getInstance().getTimeInMillis()));
        String email = mSharedPreferences.getString(KEY_EMAIL, "");
        return new SharedPreferenceEntry(name, birth, email);
    }

}
