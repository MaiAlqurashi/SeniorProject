package com.example.seniorproject;

import android.content.Context;

import com.example.seniorproject.MainActivity;

import org.json.JSONObject;

import java.io.FileOutputStream;


public class jsonClass {

    public static void writeJsonFile(Context context, String fileName) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Severity", "high");


            String jsonString = jsonObject.toString();

            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
