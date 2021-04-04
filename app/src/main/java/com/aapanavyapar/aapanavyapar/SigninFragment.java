package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aapanavyapar.aapanavyapar.services.AuthenticationGrpc;
import com.aapanavyapar.aapanavyapar.services.SignInForMailBaseResponse;
import com.aapanavyapar.aapanavyapar.services.SignInRequest;
import com.aapanavyapar.aapanavyapar.services.SignInResponse;
import com.aapanavyapar.validators.validators;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SigninFragment extends Fragment {

    public static final String host = "192.168.43.159";
    public static final int port = 4356;


    EditText phoneNo;
    Button signIn, signUp;
    EditText password;
    TextView forgotPassword;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        phoneNo = (EditText)view.findViewById(R.id.sign_in_input_phoneNo);
        signIn = (Button)view.findViewById(R.id.sign_in);
        signUp = (Button)view.findViewById(R.id.sign_up_in);
        password = (EditText)view.findViewById(R.id.sign_in_input_password);
        forgotPassword = (TextView)view.findViewById(R.id.sign_in_forgot_password);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionToForgotPassword = SigninFragmentDirections.actionSigninFragmentToForgotPasswordFragment();
                Navigation.findNavController(view).navigate(actionToForgotPassword);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionToUp = SigninFragmentDirections.actionSigninFragmentToSignupFragment();
                Navigation.findNavController(view).navigate(actionToUp);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validators.validatePhone(phoneNo) && validators.validatePassword(password)){
                    SignInRequest request = SignInRequest.newBuilder()
                            .setPhoneNo(phoneNo.getText().toString())
                            .setPassword(password.getText().toString())
                            .setApiKey(MainActivity.API_KEY)
                            .build();

                    try {
                        SignInResponse response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).signIn(request);

                        Log.d("MainActivity", "Success .. !!");
                        Toast.makeText(view.getContext(), response.getResponseData().getToken(), Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "Auth Token : " + response.getResponseData().getToken());
                        Toast.makeText(view.getContext(), response.getResponseData().getRefreshToken(), Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "Refresh Token : " + response.getResponseData().getRefreshToken());


            //            mChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);

                    }catch (StatusRuntimeException e){

                        if (e.getStatus().getCode().toString().equals("NOT_FOUND")) {
                            Log.d("MainActivity", "YES");
                        } else {
                            Log.d("MainActivity", "NO");
                        }

                        Log.d("MainActivity", String.valueOf(e.getStatus()));
                        Log.d("MainActivity", e.getMessage());
                    }
                    //SignInRequest request = SignInRequest.newBuilder().setEmail(phoneNo.getText().toString()).build();
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mChannel.shutdown();
    }

}