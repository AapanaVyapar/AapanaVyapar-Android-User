package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("keys");
    }

    public native String getNativeKey();
    public static String API_KEY;

//    tcp://0.tcp.ngrok.io:18538
    public static String VIEW_SERVICE_ADDRESS = "0.tcp.ngrok.io:18538";
    public static String AUTH_SERVICE_ADDRESS = "192.168.43.189:4356";
//    public static final int port = 4356;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.API_KEY = new String(Base64.decode(getNativeKey(), Base64.DEFAULT));
        setContentView(R.layout.activity_main);
    }
}