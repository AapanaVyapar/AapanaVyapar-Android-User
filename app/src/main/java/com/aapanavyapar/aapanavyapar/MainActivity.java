package com.aapanavyapar.aapanavyapar;

import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aapanavyapar.serviceWrappers.UpdateToken;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static {
        System.loadLibrary("keys");
    }

    public native String getNativeKey();
    public static String API_KEY;

//    tcp://0.tcp.ngrok.io:18538

    public static String VIEW_SERVICE_ADDRESS = "4.tcp.ngrok.io:13650";
    public static String AUTH_SERVICE_ADDRESS = "6.tcp.ngrok.io:14714";
//    public static final int port = 4356;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.API_KEY = new String(Base64.decode(getNativeKey(), Base64.DEFAULT));
        setContentView(R.layout.activity_main);

        if(checkInternetConnection()) {
            AuthDB authdb = new AuthDB(getApplicationContext());
            if (!authdb.dbIsEmpty()) {
                Cursor c = authdb.getRefToken();
                String token = "", access = "";
                while (c.moveToNext()) {
                    token = c.getString(1);
                    access = c.getString(2);
                }
                c.close();

                if(!token.equals("") && !access.equals("")) {

                    int[] accessInt = AuthDB.convertStringToArray(access);
                    UpdateToken updateToken = new UpdateToken();
                    if (updateToken.GetUpdatedToken(token)) {
                        Intent intent = new Intent(getApplicationContext(), ViewProvider.class);
                        intent.putExtra("Token", token);
                        intent.putExtra("Access", accessInt);
                        intent.putExtra("AuthToken",updateToken.getAuthToken());
                        startActivity(intent);
                    } else {
                        authdb.clearDatabase();
                    }
                }
            }
        }
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connect = (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
