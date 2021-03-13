package com.example.swordlibrary.java;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String readFileInfo(String fileName) {
        StringBuilder fileInfo = new StringBuilder();

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String message = null;
            while ((message = fileReader.readLine()) != null) {
                fileInfo.append(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileInfo.toString();
    }
}
