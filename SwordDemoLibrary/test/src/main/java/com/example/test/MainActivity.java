package com.example.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText mNameText;
    private DatePicker mDobPicker;
    private EditText mEmailText;

    private EmailValidator mEmailValidator;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private String TAG = "sword";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNameText = (EditText) findViewById(R.id.userNameInput);
        mDobPicker = (DatePicker) findViewById(R.id.dateOfBirthInput);
        mEmailText = (EditText) findViewById(R.id.emailInput);

        mEmailValidator = new EmailValidator();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferencesHelper = new SharedPreferencesHelper(sharedPreferences);

        populateUi();
    }

    private void populateUi() {
        SharedPreferenceEntry sharedPreferenceEntry;
        sharedPreferenceEntry = mSharedPreferencesHelper.getPersonalInfo();
        mNameText.setText(sharedPreferenceEntry.getName());
        Calendar dateOfBirth = sharedPreferenceEntry.getBirth();
        mDobPicker.init(dateOfBirth.get(Calendar.YEAR), dateOfBirth.get(Calendar.MONTH), dateOfBirth.get(Calendar.DAY_OF_MONTH), null);
        mEmailText.setText(sharedPreferenceEntry.getEmail());
    }

    public void onSaveClick(View view) {
        if (!mEmailValidator.isValid()) {
            mEmailText.setError("Invalid email");
            Log.w(TAG, "Not saving personal information: Invalid email");
            return;
        }

        String name = mNameText.getText().toString();
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.set(mDobPicker.getYear(), mDobPicker.getMonth(), mDobPicker.getDayOfMonth());
        String email = mEmailText.getText().toString();

        SharedPreferenceEntry sharedPreferenceEntry = new SharedPreferenceEntry(name, dateOfBirth, email);

        boolean isSuccess = mSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry);
        if (isSuccess) {
            Toast.makeText(this, "Personal information saved", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Personal information saved");
        } else {
            Log.e(TAG, "Failed to write personal information to SharedPreferences");
        }
    }

    public void onRevertClick(View view) {
        populateUi();
        Toast.makeText(this, "Personal information reverted", Toast.LENGTH_LONG).show();
        Log.i(TAG, "Personal information reverted");
    }

}
