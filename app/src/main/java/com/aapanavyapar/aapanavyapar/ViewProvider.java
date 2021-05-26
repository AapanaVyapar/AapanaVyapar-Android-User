package com.aapanavyapar.aapanavyapar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aapanavyapar.aapanavyapar.services.BuyingServiceGrpc;
import com.aapanavyapar.aapanavyapar.services.CapturePaymentRequest;
import com.aapanavyapar.aapanavyapar.services.CapturePaymentResponse;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.serviceWrappers.UpdateToken;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class ViewProvider extends AppCompatActivity implements PaymentResultWithDataListener {

    static {
        System.loadLibrary("keys");
    }

    public static native String getNativeKeyRazorPay();

    public static final String TAG = "ViewProvider";
    private static final int PERMISSION_LOCATION = 10;

    public static final String BUYING_SERVICE_ADDRESS = "192.168.43.200:9359";
    public static final int PRIORITY_ACCURACY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    public static final int DEFAULT_UPDATE_INTERVAL = 3;
    public static final int FAST_UPDATE_INTERVAL = 5;

    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;

    DataModel dataModel;
    private LocationCallback locationCallback;

    static public Location currentLocation = null;

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions();
        while (!checkForPermissions()) {
            Toast.makeText(getApplicationContext(), "Provide Permission To Continue", Toast.LENGTH_SHORT).show();
        }

        Intent intent = getIntent();
        String token = intent.getStringExtra("Token");
        String authToken = intent.getStringExtra("AuthToken");
        int[] access =  intent.getIntArrayExtra("Access");

        dataModel = new ViewModelProvider(this).get(DataModel.class);
        dataModel.setRefreshToken(token);
        dataModel.setAccess(access);
        dataModel.setAuthToken(authToken);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(PRIORITY_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    currentLocation = location;
                }
            }
        };
        updateGPS();
        startLocationUpdates();

        setContentView(R.layout.view_provider);
        Toast.makeText(this.getApplicationContext(), "View Provider...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateGPS();
        startLocationUpdates();

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
                        selectedFragment = new ProductSearchFragment();
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
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Provide Permissions To Get Location ..!!", Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(ViewProvider.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ViewProvider.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(ViewProvider.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
    }

    private void startLocationUpdates() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    currentLocation = location;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(this,"Payment successful", Toast.LENGTH_SHORT).show();

        ManagedChannel mChannel = ManagedChannelBuilder.forTarget(BUYING_SERVICE_ADDRESS).usePlaintext().build();
        BuyingServiceGrpc.BuyingServiceBlockingStub blockingStub = BuyingServiceGrpc.newBlockingStub(mChannel);

        CapturePaymentRequest request = CapturePaymentRequest.newBuilder()
                .setApiKey(MainActivity.API_KEY)
                .setRazorpayPaymentId(paymentData.getPaymentId())
                .setToken(dataModel.getAuthToken())
                .setRazorpayOrderId(paymentData.getOrderId())
                .build();
        CapturePaymentResponse response = null;
        try{
            response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).capturePayment(request);

        }catch (StatusRuntimeException e) {
            if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                UpdateToken updateToken = new UpdateToken();
                if (updateToken.GetUpdatedToken(dataModel.getRefreshToken())) {
                    dataModel.setAuthToken(updateToken.getAuthToken());
                    CapturePaymentRequest reRequest = CapturePaymentRequest.newBuilder()
                            .setApiKey(MainActivity.API_KEY)
                            .setRazorpayPaymentId(paymentData.getPaymentId())
                            .setToken(dataModel.getAuthToken())
                            .setRazorpayOrderId(paymentData.getOrderId())
                            .build();
                    try {
                        response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).capturePayment(reRequest);

                    }catch (StatusRuntimeException e1) {
                        Toast.makeText(getApplicationContext(), "Fail To Capture Your Payment If Payment Debited From Your Account it Will Be Back in 6 - 7 working days", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Unable To Update Refresh Token ..!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Fail To Capture Your Payment If Payment Debited From Your Account it Will Be Back in 6 - 7 working days", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        Toast.makeText(getApplicationContext(), "PayMent Successfull ..!!", Toast.LENGTH_LONG).show();

        Log.d(TAG, "Response : " + response);
        Log.d(TAG, "Success : " + paymentData.getPaymentId());

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        switch (i) {
            case Checkout.NETWORK_ERROR:
                Toast.makeText(this,"Payment is not successful NETWORK_ERROR", Toast.LENGTH_SHORT).show();
                break;
            case Checkout.INVALID_OPTIONS:
                Toast.makeText(this,"Payment is not successful INVALID_OPTIONS", Toast.LENGTH_SHORT).show();
                break;
            case Checkout.PAYMENT_CANCELED:
                Toast.makeText(this,"Payment is not successful PAYMENT_CANCELED", Toast.LENGTH_SHORT).show();
                break;
            case Checkout.TLS_ERROR:
                Toast.makeText(this,"Payment is not successful TLS_ERROR", Toast.LENGTH_SHORT).show();
                break;
        }

        Toast.makeText(this,"Payment is not successful", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Error : " + s);
    }
}

/*
package com.aapanavyapar.aapanavyapar;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
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
import com.aapanavyapar.serviceWrappers.InitUserWrapper;
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
            Log.d(TAG, "CONNECTED");
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "Not CONNECTED");
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

        requestPermissions();
        while (!checkForPermissions()) {
            Toast.makeText(getApplicationContext(), "Provide Permission To Continue", Toast.LENGTH_SHORT).show();
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

//        if(locationEnabled) {
//            Intent i = new Intent(getApplicationContext(), LocationService.class);
//            bindService(i, connection, Context.BIND_AUTO_CREATE);
//        }

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



*/