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
import com.aapanavyapar.aapanavyapar.services.SignInForMailBaseRequest;
import com.aapanavyapar.aapanavyapar.services.SignInForMailBaseResponse;
import com.aapanavyapar.validators.validators;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SigninFragment extends Fragment {

    public static final String host = "192.168.8.21";
    public static final int port = 4356;


    EditText email;
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

        email = (EditText)view.findViewById(R.id.sign_in_input_email);
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
                if(validators.validateMail(email) && validators.validatePassword(password)){
                    SignInForMailBaseRequest request = SignInForMailBaseRequest.newBuilder()
                            .setMail(email.getText().toString())
                            .setPassword(password.getText().toString())
                            .setApiKey(MainActivity.API_KEY)
                            .build();

                    try {
                        SignInForMailBaseResponse response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).signInWithMail(request);

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
                    //SignInRequest request = SignInRequest.newBuilder().setEmail(email.getText().toString()).build();
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