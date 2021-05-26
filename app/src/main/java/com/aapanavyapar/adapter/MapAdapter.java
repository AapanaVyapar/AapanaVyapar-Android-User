package com.aapanavyapar.adapter;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.aapanavyapar.aapanavyapar.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;

public class MapAdapter extends AppCompatActivity {
    
    SupportMapFragment smf;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        smf = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.ps_map);
        client = LocationServices.getFusedLocationProviderClient(this);

//        new FusedLocationProviderClient(new PermissionListener() {
//
//            @Override
//            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
//            {
//
//            }
//
//            @Override
//            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse)
//            {
//
//            }
//
//            public void onPermissionRationaleShouldBeShown()
//            {
//
//            }
//        });

    }

}