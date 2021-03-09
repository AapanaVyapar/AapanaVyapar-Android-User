package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.aapanavyapar.dataModel.DataModel;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("keys");
    }

    public native String getNativeKey();
    public static String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.API_KEY = new String(Base64.decode(getNativeKey(), Base64.DEFAULT));

        if ("fdfdsb&*h3uhfdskjwrhufds998Aihwihvbjfjhiur2732wefiuhsd7e98fdsa".equals(MainActivity.API_KEY)) {
            Toast.makeText(this, "Sucess", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_main);
    }
}