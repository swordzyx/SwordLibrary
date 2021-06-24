package com.example.test;

import java.util.Calendar;

public class SharedPreferenceEntry {
    private final String name;
    private final Calendar birth;
    private final String email;

    public SharedPreferenceEntry(String name, Calendar birth, String email) {
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public Calendar getBirth() {
        return birth;
    }

    public String getEmail() {
        return email;
    }
}
