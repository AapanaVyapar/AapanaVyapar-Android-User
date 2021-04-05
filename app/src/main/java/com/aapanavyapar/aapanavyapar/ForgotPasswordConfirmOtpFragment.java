package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.aapanavyapar.aapanavyapar.services.ConformForgetPasswordOTPRequest;
import com.aapanavyapar.aapanavyapar.services.ConformForgetPasswordOTPResponse;
//import com.aapanavyapar.aapanavyapar.services.ContactConformationResponse;
import com.aapanavyapar.aapanavyapar.services.ContactConformationRequest;
import com.aapanavyapar.aapanavyapar.services.ContactConformationResponse;
import com.aapanavyapar.aapanavyapar.services.ForgetPasswordRequest;
import com.aapanavyapar.aapanavyapar.services.NewTokenRequest;
import com.aapanavyapar.aapanavyapar.services.NewTokenResponse;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.validators.validators;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class ForgotPasswordConfirmOtpFragment extends Fragment {

    public static final String host = "192.168.43.159";
    public static final int port = 4356;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    private DataModel dataModel;

    EditText input_Otp;
    Button btnNext;

    public ForgotPasswordConfirmOtpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_confirm_otp, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        input_Otp = (EditText)view.findViewById(R.id.forgot_password_confirm_otp_text);
        btnNext = (Button)view.findViewById(R.id.forgot_password_confirm_otp_button);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConformForgetPasswordOTPRequest request = ConformForgetPasswordOTPRequest.newBuilder()
                            .setOtp(input_Otp.getText().toString())
                            .setToken(dataModel.getAuthToken())
                            .setApiKey(MainActivity.API_KEY)
                            .build();

                try {
                    ConformForgetPasswordOTPResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).conformForgetPasswordOTP(request);

                    Toast.makeText(getContext(), "Success .. !! " + response.getNewPassToken(), Toast.LENGTH_LONG).show();

                    NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToCreateNewPasswordFragment();
                    Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);

                }catch (StatusRuntimeException e){
                    //Log.d("ConfirmOtpFragment", e.getMessage());

                    if (e.getStatus().getCode().toString().equals("Unauthenticated")){
                        if (e.getMessage().equals("Request With Invalid Token")) {
                            Toast.makeText(view.getContext(), "Update Refresh Token", Toast.LENGTH_SHORT).show();
                            NewTokenRequest newTokenRequest = NewTokenRequest.newBuilder()
                                    .setApiKey(MainActivity.API_KEY)
                                    .setRefreshToken(dataModel.getRefreshToken())
                                    .build();

                            try {
                                NewTokenResponse response = blockingStub.getNewToken(newTokenRequest);
                                dataModel.setAuthToken(response.getToken());

                                ConformForgetPasswordOTPRequest reRequest = ConformForgetPasswordOTPRequest.newBuilder()
                                        .setOtp(input_Otp.getText().toString())
                                        .setToken(dataModel.getAuthToken())
                                        .setApiKey(MainActivity.API_KEY)
                                        .build();
                                ConformForgetPasswordOTPResponse reResponse = blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).conformForgetPasswordOTP(request);

                                Toast.makeText(getContext(), "Success .. !! " + reResponse.getNewPassToken(), Toast.LENGTH_LONG).show();

                                NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToCreateNewPasswordFragment();
                                Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);




                            }catch (StatusRuntimeException e1){
                                Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                            }

                        } else{
                            Toast.makeText(view.getContext(), "Try With Another Mobile Number", Toast.LENGTH_SHORT).show();
                        }

                    } else if(e.getStatus().getCode().toString().equals("Internal")){
                        Toast.makeText(view.getContext(), "Server Error ..!!. Please Try After Some Time.", Toast.LENGTH_SHORT).show();

                    } else if(e.getStatus().getCode().toString().equals("PermissionDenied")) {
                        Toast.makeText(view.getContext(), "Invalid OTP Or Password", Toast.LENGTH_SHORT).show();

                    } else{
                        e.getMessage();
                        Toast.makeText(view.getContext(), "Unknown Error Occured", Toast.LENGTH_SHORT).show();
                    }

                }


    });
        }



}