package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.text.Layout;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("keys");
    }

    public native String getNativeKey();
    public static String API_KEY;

    public static String IPAddress = "192.168.43.189";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.API_KEY = new String(Base64.decode(getNativeKey(), Base64.DEFAULT));
        setContentView(R.layout.activity_main);
    }
}