package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aapanavyapar.aapanavyapar.services.AuthenticationGrpc;
import com.aapanavyapar.aapanavyapar.services.SignInForMailBaseRequest;
import com.aapanavyapar.aapanavyapar.services.SignInForMailBaseResponse;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class MainActivity extends AppCompatActivity {

    public static final String host = "192.168.43.159";
    public static final int port = 4356;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

    }

    public void testRpc(View view) {

        SignInForMailBaseRequest request = SignInForMailBaseRequest.newBuilder().setMail("shitij18@mail.com").setPassword("1234567881").build();

        try {
            SignInForMailBaseResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).signInWithMail(request);

            if (response.hasResponseData()) {
                Log.d("MainActivity", "Success .. !!");
                Toast.makeText(view.getContext(), response.getResponseData().getToken(), Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "Auth Token : " + response.getResponseData().getToken());
                Toast.makeText(view.getContext(), response.getResponseData().getRefreshToken(), Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "Refresh Token : " + response.getResponseData().getRefreshToken());

            } else {
                Toast.makeText(view.getContext(), response.getCode().name(), Toast.LENGTH_SHORT).show();

            }

//            mChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);

        }catch (StatusRuntimeException e){
            Log.d("MainActivity", e.getMessage());
        }

    }
}