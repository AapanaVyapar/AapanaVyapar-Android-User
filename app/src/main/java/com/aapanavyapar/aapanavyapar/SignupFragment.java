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
import android.widget.Toast;

import com.aapanavyapar.aapanavyapar.services.AuthenticationGrpc;
import com.aapanavyapar.aapanavyapar.services.SignInForMailBaseRequest;
import com.aapanavyapar.aapanavyapar.services.SignInForMailBaseResponse;
import com.aapanavyapar.aapanavyapar.services.SignUpRequest;
import com.aapanavyapar.aapanavyapar.services.SignUpResponse;
import com.aapanavyapar.validators.validators;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SignupFragment extends Fragment {

    public static final String host = "192.168.43.159";
    public static final int port = 4356;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    Button signUp;
    EditText mail;
    EditText password;
    EditText phone;
    EditText pinCode;
    EditText userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUp = (Button)view.findViewById(R.id.signup);
        userName = (EditText)view.findViewById(R.id.input_username);
        mail = (EditText)view.findViewById(R.id.input_mail_id);
        password = (EditText)view.findViewById(R.id.input_pass);
        phone = (EditText)view.findViewById(R.id.input_phno);
        pinCode = (EditText)view.findViewById(R.id.input_pincode);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validators.validateUserName(userName) && validators.validateMail(mail) && validators.validatePinCode(pinCode) && validators.validatePassword(password)){
                 //   testRpc(v);
                    SignUpRequest request = SignUpRequest.newBuilder()
                            .setEmail(mail.getText().toString())
                            .setUsername(userName.getText().toString())
                            .setPinCode(pinCode.getText().toString())
                            .setPhoneNo(phone.getText().toString())
                            .setPassword(password.getText().toString())
                            .build();
                    try{
                        SignUpResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).signup(request);
                        if (response.hasResponseData()) {
                            Log.d("MainActivity", "Success .. !!");
                            Toast.makeText(view.getContext(), response.getResponseData().getToken(), Toast.LENGTH_SHORT).show();
                            Log.d("MainActivity", "Auth Token : " + response.getResponseData().getToken());
                            Toast.makeText(view.getContext(), response.getResponseData().getRefreshToken(), Toast.LENGTH_SHORT).show();
                            Log.d("MainActivity", "Refresh Token : " + response.getResponseData().getRefreshToken());

                            Bundle result = new Bundle();
                            result.putString("auth", response.getResponseData().getToken());
                            result.putString("refresh", response.getResponseData().getRefreshToken());
                            getChildFragmentManager().setFragmentResult("OTP_OF_SIGN_UP", result);

                            NavDirections actionWithOtp = SignupFragmentDirections.actionSignupFragmentToSignupConfirmOtpFragment();

                            Navigation.findNavController(view).navigate(actionWithOtp);

                        } else {
                            Toast.makeText(view.getContext(), response.getCode().name(), Toast.LENGTH_SHORT).show();

                        }


                    }
                    catch(StatusRuntimeException e){
                        Log.d("MainActivity", e.getMessage());
                    }

                }

            }
        });
    }

//    public void testRpc(View view) {
//
//        SignInForMailBaseRequest request = SignInForMailBaseRequest.newBuilder().setMail("shitij18@mail.com").setPassword("1234567881").build();
//
//        try {
//            SignInForMailBaseResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).signInWithMail(request);
//
//            if (response.hasResponseData()) {
//                Log.d("MainActivity", "Success .. !!");
//                Toast.makeText(view.getContext(), response.getResponseData().getToken(), Toast.LENGTH_SHORT).show();
//                Log.d("MainActivity", "Auth Token : " + response.getResponseData().getToken());
//                Toast.makeText(view.getContext(), response.getResponseData().getRefreshToken(), Toast.LENGTH_SHORT).show();
//                Log.d("MainActivity", "Refresh Token : " + response.getResponseData().getRefreshToken());
//
//            } else {
//                Toast.makeText(view.getContext(), response.getCode().name(), Toast.LENGTH_SHORT).show();
//
//            }
//
////            mChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
//
//        }catch (StatusRuntimeException e){
//            Log.d("MainActivity", e.getMessage());
//        }
//
//    }

}