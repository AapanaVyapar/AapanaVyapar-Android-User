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
import com.aapanavyapar.aapanavyapar.services.ConformForgetPasswordOTPRequest;
import com.aapanavyapar.aapanavyapar.services.ConformForgetPasswordOTPResponse;
//import com.aapanavyapar.aapanavyapar.services.ContactConformationResponse;
import com.aapanavyapar.aapanavyapar.services.ForgetPasswordRequest;
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
                            .setApiKey(MainActivity.API_KEY)
                            .build();

                try {
                    ConformForgetPasswordOTPResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).conformForgetPasswordOTP(request);

                    Toast.makeText(getContext(), "Success .. !! " + response.getNewPassToken(), Toast.LENGTH_LONG).show();

                    NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToCreateNewPasswordFragment();
                    Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);

                }catch (StatusRuntimeException e){
                    Log.d("ConfirmOtpFragment", e.getMessage());

                }
                }


    });


    }
}