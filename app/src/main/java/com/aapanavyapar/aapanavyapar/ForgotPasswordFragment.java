package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.aapanavyapar.aapanavyapar.services.ForgetPasswordRequest;
import com.aapanavyapar.aapanavyapar.services.ForgetPasswordResponse;
import com.aapanavyapar.constants.constants;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.validators.validators;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class ForgotPasswordFragment extends Fragment {

    public static final String host = "192.168.43.159";
    public static final int port = 4356;

    private DataModel dataModel;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    EditText input_phno;
    Button btnSendOtp;

    //Button send_OTP;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        input_phno = (EditText)view.findViewById(R.id.forgot_phone_number);
        btnSendOtp = (Button)view.findViewById(R.id.get_OTP);
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validators.validatePhone(input_phno)) {
                    ForgetPasswordRequest request = ForgetPasswordRequest.newBuilder()
                            .setPhoNo(input_phno.getText().toString())
                            .setApiKey(MainActivity.API_KEY)
                            .build();

                    try {
                        ForgetPasswordResponse response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).forgetPassword(request);

                        Log.d("MainActivity", "Success .. !!");
                        Toast.makeText(view.getContext(), response.getResponseData().getToken(), Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "Auth Token : " + response.getResponseData().getToken());
                        Toast.makeText(view.getContext(), response.getResponseData().getRefreshToken(), Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "Refresh Token : " + response.getResponseData().getRefreshToken());

                        int []access = {constants.GetNewToken, constants.ResendOTP, constants.ForgetPassword};

                        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);
                        dataModel.setTokens(response.getResponseData().getToken(), response.getResponseData().getRefreshToken(), access);

                        NavDirections actionForgotPasswordFragmentToForgotPasswordConfirmOtp = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotPasswordConfirmOtpFragment();
                        Navigation.findNavController(view).navigate(actionForgotPasswordFragmentToForgotPasswordConfirmOtp);


                    } catch (StatusRuntimeException e) {
                        if(e.getStatus().getCode().toString().equals("Unauthenticated")){
                            Toast.makeText(view.getContext(),"Please Update Your Application", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("InvalidArguments")){
                            Toast.makeText(view.getContext(), "Please Enter Valid Inputs", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("PermissionDenied")) {
                            Toast.makeText(view.getContext(), "Invalid UserName Or Password", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("Internal")) {
                            Toast.makeText(view.getContext(), "Server Error", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(view.getContext(), "Unknown Error Occurred", Toast.LENGTH_SHORT).show();

                        }
                        NavDirections actionForgotPasswordFragmentToForgotPasswordConfirmOtp = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSigninFragment();
                        Navigation.findNavController(view).navigate(actionForgotPasswordFragmentToForgotPasswordConfirmOtp);

                    }

                }
            }
        });

    }
}