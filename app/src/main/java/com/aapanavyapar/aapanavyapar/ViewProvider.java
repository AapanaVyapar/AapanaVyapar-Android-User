package com.aapanavyapar.aapanavyapar;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.location.LocationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewProvider extends AppCompatActivity {

    public static final String TAG = "ViewProvider";
    private static final int PERMISSION_LOCATION = 10;

    private boolean locationEnabled = false;

    public static LocationService mService;
    public static boolean mBound = false;

    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        if(locationEnabled) {
            Intent intent = new Intent(this, LocationService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        while (!checkForPermissions()){requestPermissions();}

        if(locationEnabled) {
            Intent i = new Intent(this, LocationService.class);
            bindService(i, connection, Context.BIND_AUTO_CREATE);
        }

        Intent intent = getIntent();
        String token = intent.getStringExtra("Token");
        String authToken = intent.getStringExtra("AuthToken");
        int[] access =  intent.getIntArrayExtra("Access");

        DataModel dataModel = new ViewModelProvider(this).get(DataModel.class);
        dataModel.setRefreshToken(token);
        dataModel.setAccess(access);
        dataModel.setAuthToken(authToken);


        setContentView(R.layout.view_provider);
        Toast.makeText(this.getApplicationContext(), "View Provider...", Toast.LENGTH_LONG).show();


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setItemHorizontalTranslationEnabled(true);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.av_trend:
                        selectedFragment = new TrendingFragment();
                        break;
                    case R.id.av_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.av_orders:
                        selectedFragment = new OrderFragment();
                        break;
                    case R.id.av_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TrendingFragment()).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationEnabled = true;
            } else {
                locationEnabled = false;
                Toast.makeText(getApplicationContext(), "Provide Permissions To Get Location ..!!", Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(ViewProvider.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ViewProvider.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationEnabled = false;
            return false;
        }
        locationEnabled = true;
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(ViewProvider.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
    }

}

