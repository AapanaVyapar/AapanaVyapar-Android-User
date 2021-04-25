package com.aapanavyapar.aapanavyapar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ViewProvider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_provider);
        Toast.makeText(this.getApplicationContext(),"View Provider...",Toast.LENGTH_LONG).show();
    }
}