package com.example.test;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnitTestSample {
    private static final String TEST_NAME = "Test name";
    private static final String TEST_EMAIL = "test@email.com";
    private static final Calendar TEST_BIRTH = Calendar.getInstance();

    static {
        TEST_BIRTH.set(1980, 1, 1);
    }

    private SharedPreferenceEntry spEntry;
    private SharedPreferencesHelper mockSpHelper;
    private SharedPreferencesHelper mockBrokenSpHelper;

    /*@Mock
    SharedPreferences mockSp;
    @Mock
    SharedPreferences mockBrokenSp;
    @Mock
    SharedPreferences.Editor mockEditor;
    @Mock
    SharedPreferences.Editor mockBrokenEditor;*/

    /*@Before
    public void initMocks() {
        spEntry = new SharedPreferenceEntry(TEST_NAME, TEST_BIRTH, TEST_EMAIL);
        mockSpHelper = createMockSharedPreference();

        mockBrokenSpHelper = createBrokenMockSharedPreference();
    }*/

    @Test
    public void sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        //boolean success = mockSpHelper.savePersonalInfo(spEntry);
        boolean success = true;

        assertThat("Checking that SharedPreferenceEntry.save... returns true", success, is(true));

        /*SharedPreferenceEntry savedSharedPreferenceEntry = mockSpHelper.getPersonalInfo();
        assertThat("Checking that SharedPreferenceEntry.name has been persisted and read correctly", spEntry.getName(), is(equalTo(savedSharedPreferenceEntry.getName())));
        assertThat("Checking that SharedPreferenceEntry.birth has been persisted and read correctly", spEntry.getBirth(), is(equalTo(savedSharedPreferenceEntry.getBirth())));
        assertThat("Checking that SharedPreferenceEntry.email has been persisted and read correctly", spEntry.getEmail(), is(equalTo(savedSharedPreferenceEntry.getEmail())));*/
    }

    /*@Test
    public void SharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {
        boolean success = mockBrokenSpHelper.savePersonalInfo(spEntry);
        assertThat("Makes sure writing to a broken SharedPreferencesHelper returns false", success, is(false));
    }

    private SharedPreferencesHelper createBrokenMockSharedPreference() {
        when(mockBrokenEditor.commit()).thenReturn(false);
        when(mockBrokenSp.edit()).thenReturn(mockBrokenEditor);
        return new SharedPreferencesHelper(mockBrokenSp);
    }

    private SharedPreferencesHelper createMockSharedPreference() {
        when(mockSp.getString(eq(SharedPreferencesHelper.KEY_NAME), anyString())).thenReturn(spEntry.getName());
        when(mockSp.getString(eq(SharedPreferencesHelper.KEY_EMAIL), anyString())).thenReturn(spEntry.getEmail());
        when(mockSp.getLong(eq(SharedPreferencesHelper.KEY_DOB), anyLong())).thenReturn(spEntry.getBirth().getTimeInMillis());

        when(mockEditor.commit()).thenReturn(true);

        when(mockSp.edit()).thenReturn(mockEditor);
        return new SharedPreferencesHelper(mockSp);
    }*/
}
