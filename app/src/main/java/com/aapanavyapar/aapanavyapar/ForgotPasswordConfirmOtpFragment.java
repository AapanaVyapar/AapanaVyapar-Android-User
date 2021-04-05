package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.aapanavyapar.aapanavyapar.services.AuthenticationGrpc;
import com.aapanavyapar.aapanavyapar.services.ConformForgetPasswordOTPRequest;
import com.aapanavyapar.aapanavyapar.services.ConformForgetPasswordOTPResponse;
import com.aapanavyapar.aapanavyapar.services.NewTokenRequest;
import com.aapanavyapar.aapanavyapar.services.NewTokenResponse;
import com.aapanavyapar.aapanavyapar.services.ResendOTPRequest;
import com.aapanavyapar.aapanavyapar.services.ResendOTPResponse;
import com.aapanavyapar.constants.constants;
import com.aapanavyapar.dataModel.DataModel;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class ForgotPasswordConfirmOtpFragment extends Fragment {

    public static final String host = MainActivity.IPAddress;
    public static final int port = 4356;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    private DataModel dataModel;

    EditText input_Otp;
    Button btnNext;
    TextView resend_Otp;

    CountDownTimer timer;

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

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        mChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_confirm_otp, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        input_Otp = (EditText) view.findViewById(R.id.forgot_password_confirm_otp_text);
        btnNext = (Button) view.findViewById(R.id.forgot_password_confirm_otp_button);
        resend_Otp = (TextView) view.findViewById(R.id.resend_otp);

        resend_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dataModel.CanWeUseTokenForThis(constants.ResendOTP)){
                    Toast.makeText(getContext(), "Please Try Again ..!!", Toast.LENGTH_LONG).show();

                    NavDirections actionWithOtp = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToForgotPasswordFragment();
                    Navigation.findNavController(view).navigate(actionWithOtp);
                }
                ResendOTPRequest request = ResendOTPRequest.newBuilder()
                        .setToken(dataModel.getAuthToken())
                        .setApiKey(MainActivity.API_KEY)
                        .build();

                try{
                    ResendOTPResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).resendOTP(request);
                    Log.d("ConfirmOtpFragment", String.valueOf(response.getResponse().getNumber()));
                    Log.d("ConfirmOtpFragment", String.valueOf(response.getTimeToWaitForNextRequest()));

                    if(response.getResponse().getNumber() == 1){
                        Toast.makeText(view.getContext(), "Please Enter OTP .. !!", Toast.LENGTH_SHORT).show();

                    }else {
                        ((TextView)view.findViewById(R.id.resend_otp)).setEnabled(false);
                        timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(response.getTimeToWaitForNextRequest().getSeconds()), 10000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.d("Timer", String.valueOf(millisUntilFinished));
                                ((TextView) view.findViewById(R.id.timer)).setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
                            }

                            @Override
                            public void onFinish() {
                                ((TextView) view.findViewById(R.id.resend_otp)).setEnabled(true);

                            }
                        }.start();

                    }

                }
                catch (StatusRuntimeException e){
                    Log.d("ERROR : ", e.getMessage());

                    if (e.getStatus().getCode().toString().equals("UNAUTHENTICATED")) {
                        if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                            Toast.makeText(view.getContext(), "Update Refresh Token", Toast.LENGTH_SHORT).show();
                            NewTokenRequest newTokenRequest = NewTokenRequest.newBuilder()
                                    .setApiKey(MainActivity.API_KEY)
                                    .setRefreshToken(dataModel.getRefreshToken())
                                    .build();

                            try {
                                NewTokenResponse newTokenResponse = blockingStub.getNewToken(newTokenRequest);
                                dataModel.setAuthToken(newTokenResponse.getToken());

                                ResendOTPRequest reRequest = ResendOTPRequest.newBuilder()
                                        .setToken(dataModel.getAuthToken())
                                        .setApiKey(MainActivity.API_KEY)
                                        .build();

                                ResendOTPResponse reResponse = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).resendOTP(reRequest);
                                if(reResponse.getResponse().getNumber() == 1){
                                    Toast.makeText(view.getContext(), "Please Enter OTP .. !!", Toast.LENGTH_SHORT).show();

                                }else {
                                    ((TextView)view.findViewById(R.id.resend_otp)).setEnabled(false);
                                    timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(reResponse.getTimeToWaitForNextRequest().getSeconds()), 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            Log.d("Timer", String.valueOf(millisUntilFinished));
                                            ((TextView) view.findViewById(R.id.timer)).setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
                                        }

                                        @Override
                                        public void onFinish() {
                                            ((TextView) view.findViewById(R.id.resend_otp)).setEnabled(true);

                                        }
                                    }.start();

                                }
                                Log.d("ConfirmOtpFragment", String.valueOf(reResponse.getResponse().getNumber()));

                                Toast.makeText(getContext(), "Success .. !! " + reResponse.getResponse().getNumber(), Toast.LENGTH_LONG).show();


                            } catch (StatusRuntimeException e1){
                                Log.d("ERROR : ", e.getMessage());
                                Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                                NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToForgotPasswordFragment();
                                Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);

                            }
                        }
                    }

                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!dataModel.CanWeUseTokenForThis(constants.ForgetPassword)){
                    Toast.makeText(getContext(), "Please Try Again ..!!", Toast.LENGTH_LONG).show();

                    NavDirections actionWithOtp = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToSigninFragment();
                    Navigation.findNavController(view).navigate(actionWithOtp);
                }


                ConformForgetPasswordOTPRequest request = ConformForgetPasswordOTPRequest.newBuilder()
                        .setOtp(input_Otp.getText().toString())
                        .setToken(dataModel.getAuthToken())
                        .setApiKey(MainActivity.API_KEY)
                        .build();

                try {
                    ConformForgetPasswordOTPResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).conformForgetPasswordOTP(request);

                    Toast.makeText(getContext(), "Success .. !! " + response.getNewPassToken(), Toast.LENGTH_LONG).show();

                    dataModel.setPassToken(response.getNewPassToken());

                    NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToCreateNewPasswordFragment();
                    Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);

                } catch (StatusRuntimeException e) {
                    Log.d("ConfirmOtpFragment1", e.getMessage());

                    if (e.getStatus().getCode().toString().equals("UNAUTHENTICATED")) {
                        if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                            Toast.makeText(view.getContext(), "Update Refresh Token", Toast.LENGTH_SHORT).show();
                            NewTokenRequest newTokenRequest = NewTokenRequest.newBuilder()
                                    .setApiKey(MainActivity.API_KEY)
                                    .setRefreshToken(dataModel.getRefreshToken())
                                    .build();

                            try {
                                NewTokenResponse newTokenResponse = blockingStub.getNewToken(newTokenRequest);
                                dataModel.setAuthToken(newTokenResponse.getToken());

                                ConformForgetPasswordOTPRequest reRequest = ConformForgetPasswordOTPRequest.newBuilder()
                                        .setOtp(input_Otp.getText().toString())
                                        .setToken(dataModel.getAuthToken())
                                        .setApiKey(MainActivity.API_KEY)
                                        .build();
                                ConformForgetPasswordOTPResponse reResponse = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).conformForgetPasswordOTP(reRequest);

                                Toast.makeText(getContext(), "Success .. !! " + reResponse.getNewPassToken(), Toast.LENGTH_LONG).show();

                                dataModel.setPassToken(reResponse.getNewPassToken());

                                NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToCreateNewPasswordFragment();
                                Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);


                            } catch (StatusRuntimeException e1) {
                                Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                                NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToForgotPasswordFragment();
                                Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);

                            }

                        } else {
                            Toast.makeText(view.getContext(), "Please Update Your App ..!!", Toast.LENGTH_SHORT).show();

                            NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToSigninFragment();
                            Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);

                        }

                    } else if (e.getStatus().getCode().toString().equals("INTERNAL")) {
                        Toast.makeText(view.getContext(), "Server Error ..!!. Please Try After Some Time.", Toast.LENGTH_SHORT).show();

                        NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToSigninFragment();
                        Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);

                    } else if (e.getStatus().getCode().toString().equals("PERMISSION_DENIED")) {
                        Toast.makeText(view.getContext(), "Invalid OTP Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(view.getContext(), "Unknown Error Occurred", Toast.LENGTH_SHORT).show();

                        NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToSigninFragment();
                        Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);
                    }

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