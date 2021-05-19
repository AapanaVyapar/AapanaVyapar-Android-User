package com.aapanavyapar.aapanavyapar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.aapanavyapar.aapanavyapar.services.ContactConformationRequest;
import com.aapanavyapar.aapanavyapar.services.ContactConformationResponse;
import com.aapanavyapar.aapanavyapar.services.ResendOTPRequest;
import com.aapanavyapar.aapanavyapar.services.ResendOTPResponse;
import com.aapanavyapar.constants.constants;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.serviceWrappers.UpdateToken;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class SignupConfirmOtpFragment extends Fragment {

    private DataModel dataModel;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    Button conformOtp;
    EditText otpText;
    TextView resend_Otp;
    CountDownTimer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        mChannel = ManagedChannelBuilder.forTarget(MainActivity.AUTH_SERVICE_ADDRESS).usePlaintext().build();

        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        return inflater.inflate(R.layout.fragment_signup_confirm_otp, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        conformOtp = view.findViewById(R.id.signup_forgot_password_confirm_otp_button);
        otpText = view.findViewById(R.id.signup_forgot_password_confirm_otp_text);
        resend_Otp = view.findViewById(R.id.resend_otp_signup);

        resend_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dataModel.CanWeUseTokenForThis(constants.ResendOTP)){
                    Toast.makeText(getContext(), "Please Try Again ..!!", Toast.LENGTH_LONG).show();

                    if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                        NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);
                    }
                }
                ResendOTPRequest request = ResendOTPRequest.newBuilder()
                        .setToken(dataModel.getAuthToken())
                        .setApiKey(MainActivity.API_KEY)
                        .build();

                try{
                    ResendOTPResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).resendOTP(request);
                    Log.d("ConfirmOtpFragment", String.valueOf(response.getResponse().getNumber()));
                    Log.d("ConfirmOtpFragment", String.valueOf(response.getTimeToWaitForNextRequest()));

                    ((TextView)view.findViewById(R.id.resend_otp_signup)).setEnabled(false);
                    timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(response.getTimeToWaitForNextRequest().getSeconds()), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d("Timer", String.valueOf(millisUntilFinished));
                            ((TextView) view.findViewById(R.id.resend_otp_signup)).setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
                        }

                        @Override
                        public void onFinish() {
                            ((TextView) view.findViewById(R.id.resend_otp_signup)).setText("resend OTP");
                            ((TextView) view.findViewById(R.id.resend_otp_signup)).setEnabled(true);

                        }
                    }.start();

                }
                catch (StatusRuntimeException e){
                    Log.d("ERROR : ", e.getMessage());

                    if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                        Toast.makeText(view.getContext(), "Update Refresh Token", Toast.LENGTH_SHORT).show();

                        UpdateToken updateToken = new UpdateToken();
                        if(updateToken.GetUpdatedToken(dataModel.getRefreshToken())) {
                            dataModel.setAuthToken(updateToken.getAuthToken());
                            try {
                                ResendOTPRequest reRequest = ResendOTPRequest.newBuilder()
                                        .setToken(dataModel.getAuthToken())
                                        .setApiKey(MainActivity.API_KEY)
                                        .build();

                                ResendOTPResponse reResponse = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).resendOTP(reRequest);
                                ((TextView) view.findViewById(R.id.resend_otp_signup)).setEnabled(false);
                                timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(reResponse.getTimeToWaitForNextRequest().getSeconds()), 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        Log.d("Timer", String.valueOf(millisUntilFinished));
                                        ((TextView) view.findViewById(R.id.resend_otp_signup)).setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
                                    }

                                    @Override
                                    public void onFinish() {
                                        ((TextView) view.findViewById(R.id.resend_otp_signup)).setText("resend OTP");
                                        ((TextView) view.findViewById(R.id.resend_otp_signup)).setEnabled(true);

                                    }
                                }.start();

                                Log.d("ConfirmOtpFragment", String.valueOf(reResponse.getResponse().getNumber()));

                                Toast.makeText(getContext(), "Success .. !! ", Toast.LENGTH_LONG).show();


                            } catch (StatusRuntimeException e1) {
                                Log.d("ERROR : ", e.getMessage());
                                Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                                if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                                    NavDirections actionSignupConfirmOtpFragment = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                                    Navigation.findNavController(view).navigate(actionSignupConfirmOtpFragment);
                                }
                            }

                        } else {
                            Log.d("ERROR : ", "Fail To Update Auth Token");
                            Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                                NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                                Navigation.findNavController(view).navigate(actionWithOtp);
                            }
                        }
                    }
                }
            }
        });



        conformOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!dataModel.CanWeUseTokenForThis(constants.ConformContact)){
                    Toast.makeText(getContext(), "Please Try Again ..!!", Toast.LENGTH_LONG).show();

                    if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                        NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);
                    }
                }

                ContactConformationRequest request = ContactConformationRequest.newBuilder()
                        .setOtp(otpText.getText().toString().trim())
                        .setToken(dataModel.getAuthToken())
                        .setApiKey(MainActivity.API_KEY)
                        .build();
                try {
                    ContactConformationResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).contactConformation(request);

                    Toast.makeText(getContext(), "Success .. !! ", Toast.LENGTH_LONG).show();

                    int []access = {constants.GetNewToken, constants.LogOut, constants.GetNewToken, constants.External};
                    dataModel.setTokens(response.getToken(), response.getRefreshToken(), access);

                    AuthDB authdb = new AuthDB(getContext());
                    SQLiteDatabase db = authdb.getReadableDatabase();

                    authdb.insertData(dataModel.getRefreshToken(),access);

                    Intent intent  = new Intent(getContext(), ViewProvider.class);
                    intent.putExtra("Token",dataModel.getRefreshToken());
                    intent.putExtra("AuthToken",dataModel.getAuthToken());
                    intent.putExtra("Access",access);
                    startActivity(intent);

                }catch (StatusRuntimeException e){
                    if (e.getStatus().getCode().toString().equals("INVALID_ARGUMENT")) {
                        Toast.makeText(view.getContext(), "Invalid OTP Try Again ..!!", Toast.LENGTH_SHORT).show();

                    } else if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                        UpdateToken updateToken = new UpdateToken();
                        if(updateToken.GetUpdatedToken(dataModel.getRefreshToken())) {
                            dataModel.setAuthToken(updateToken.getAuthToken());

                            int []access = {constants.GetNewToken, constants.LogOut, constants.External};
                            dataModel.setAccess(access);
                            dataModel.setAuthToken(updateToken.getAuthToken());

                            Toast.makeText(view.getContext(), "Update Refresh Token", Toast.LENGTH_SHORT).show();
                            try {
                                ContactConformationRequest reRequest = ContactConformationRequest.newBuilder()
                                        .setOtp(otpText.getText().toString().trim())
                                        .setToken(dataModel.getAuthToken())
                                        .setApiKey(MainActivity.API_KEY)
                                        .build();

                                ContactConformationResponse reResponse = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).contactConformation(reRequest);
                                Toast.makeText(getContext(), "Success .. !! " + dataModel.getAuthToken(), Toast.LENGTH_LONG).show();

                                access = new int[]{constants.GetNewToken, constants.LogOut, constants.GetNewToken, constants.External};
                                dataModel.setTokens(reResponse.getToken(), reResponse.getRefreshToken(), access);

                                AuthDB authdb = new AuthDB(getContext());
                                SQLiteDatabase db = authdb.getReadableDatabase();

                                authdb.insertData(dataModel.getRefreshToken(),access);

                                Intent intent  = new Intent(getContext(), ViewProvider.class);
                                intent.putExtra("Token",dataModel.getRefreshToken());
                                intent.putExtra("AuthToken",dataModel.getAuthToken());
                                intent.putExtra("Access",access);
                                startActivity(intent);

                            }catch (StatusRuntimeException e1){
                                Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                                if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                                    NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                                    Navigation.findNavController(view).navigate(actionWithOtp);
                                }
                            }

                        } else {
                            Toast.makeText(view.getContext(), "Please Update Your Application", Toast.LENGTH_SHORT).show();

                            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                                NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                                Navigation.findNavController(view).navigate(actionWithOtp);
                            }
                        }

                    } else if(e.getStatus().getCode().toString().equals("UNKNOWN")) {
                        Toast.makeText(view.getContext(), "Server Error ..!!. Please Try After Some Time.", Toast.LENGTH_SHORT).show();

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                            NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                            Navigation.findNavController(view).navigate(actionWithOtp);
                        }

                    } else if(e.getStatus().getCode().toString().equals("NOT_FOUND")) {
                        Toast.makeText(view.getContext(), "Please Try Again OTP Expired .. !!", Toast.LENGTH_SHORT).show();

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                            NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                            Navigation.findNavController(view).navigate(actionWithOtp);
                        }

                    } else if(e.getStatus().getCode().toString().equals("ABORTED")) {
                        Toast.makeText(view.getContext(), "Please Update The App .. !!. App Is Corrupted", Toast.LENGTH_SHORT).show();

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                            NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                            Navigation.findNavController(view).navigate(actionWithOtp);
                        }

                    } else if(e.getStatus().getCode().toString().equals("INTERNAL")) {
                        Toast.makeText(view.getContext(), "Please Try To SignIn", Toast.LENGTH_SHORT).show();

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                            NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                            Navigation.findNavController(view).navigate(actionWithOtp);
                        }

                    } else {
                        Toast.makeText(view.getContext(), "Unknown Error Occurred", Toast.LENGTH_SHORT).show();

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.SignupConfirmOtpFragment) {
                            NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                            Navigation.findNavController(view).navigate(actionWithOtp);
                        }

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