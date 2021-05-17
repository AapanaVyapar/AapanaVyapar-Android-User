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
import com.aapanavyapar.serviceWrappers.UpdateToken;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class ForgotPasswordConfirmOtpFragment extends Fragment {

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

        mChannel = ManagedChannelBuilder.forTarget(MainActivity.AUTH_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_confirm_otp, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        input_Otp = (EditText) view.findViewById(R.id.signup_forgot_password_confirm_otp_text);
        btnNext = (Button) view.findViewById(R.id.signup_forgot_password_confirm_otp_button);
        resend_Otp = (TextView) view.findViewById(R.id.resend_otp);

        resend_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dataModel.CanWeUseTokenForThis(constants.ResendOTP)){
                    Toast.makeText(getContext(), "Please Try Again ..!!", Toast.LENGTH_LONG).show();

                    if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
                        NavDirections actionWithOtp = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToForgotPasswordFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);
                    }
                }

                ResendOTPRequest request = ResendOTPRequest.newBuilder()
                        .setToken(dataModel.getAuthToken())
                        .setApiKey(MainActivity.API_KEY)
                        .build();

                try{
                    ResendOTPResponse response = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).resendOTP(request);
                    Log.d("ConfirmOtpFragment", String.valueOf(response.getResponse().getNumber()));
                    Log.d("ConfirmOtpFragment", String.valueOf(response.getTimeToWaitForNextRequest()));

                    ((TextView)view.findViewById(R.id.resend_otp)).setEnabled(false);
                    timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(response.getTimeToWaitForNextRequest().getSeconds()), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d("Timer", String.valueOf(millisUntilFinished));
                            ((TextView) view.findViewById(R.id.resend_otp)).setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
                        }

                        @Override
                        public void onFinish() {
                            ((TextView) view.findViewById(R.id.resend_otp)).setText("resend OTP");
                            ((TextView) view.findViewById(R.id.resend_otp)).setEnabled(true);

                        }
                    }.start();

                }
                catch (StatusRuntimeException e){
                    Log.d("Resend OTP ERROR : ", e.getMessage());

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

                                ResendOTPResponse reResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).resendOTP(reRequest);
                                ((TextView) view.findViewById(R.id.resend_otp)).setEnabled(false);
                                timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(reResponse.getTimeToWaitForNextRequest().getSeconds()), 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        Log.d("Timer", String.valueOf(millisUntilFinished));

                                        Toast.makeText(getContext(), String.valueOf(millisUntilFinished), Toast.LENGTH_LONG).show();
                                        ((TextView) view.findViewById(R.id.resend_otp)).setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
                                    }

                                    @Override
                                    public void onFinish() {
                                        ((TextView) view.findViewById(R.id.resend_otp)).setText("resend OTP");
                                        ((TextView) view.findViewById(R.id.resend_otp)).setEnabled(true);

                                    }
                                }.start();

                                Log.d("ConfirmOtpFragment", String.valueOf(reResponse.getResponse().getNumber()));

                                Toast.makeText(getContext(), "Success .. !! " + reResponse.getResponse().getNumber(), Toast.LENGTH_LONG).show();


                            } catch (StatusRuntimeException e1) {
                                Log.d("On Resend ERROR : ", e.getMessage());
                                Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                                if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
                                    NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToForgotPasswordFragment();
                                    Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);
                                }

                            }
                        }else {
                            Log.d("ERROR : ", "Unable To Update Token");
                            Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
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

                    if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
                        NavDirections actionWithOtp = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToCreateNewPasswordFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);
                    }
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

                    if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
                        NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToCreateNewPasswordFragment();
                        Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);
                    }

                } catch (StatusRuntimeException e) {
                    Log.d("ConfirmOtpFragment1", e.getMessage());

                    if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                        Toast.makeText(view.getContext(), "Update Refresh Token", Toast.LENGTH_SHORT).show();

                        UpdateToken updateToken = new UpdateToken();
                        if(updateToken.GetUpdatedToken(dataModel.getRefreshToken())) {
                            dataModel.setAuthToken(updateToken.getAuthToken());

                            try {
                                ConformForgetPasswordOTPRequest reRequest = ConformForgetPasswordOTPRequest.newBuilder()
                                        .setOtp(input_Otp.getText().toString())
                                        .setToken(dataModel.getAuthToken())
                                        .setApiKey(MainActivity.API_KEY)
                                        .build();
                                ConformForgetPasswordOTPResponse reResponse = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).conformForgetPasswordOTP(reRequest);

                                Toast.makeText(getContext(), "Success .. !! " + reResponse.getNewPassToken(), Toast.LENGTH_LONG).show();

                                dataModel.setPassToken(reResponse.getNewPassToken());

                                if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
                                    NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToCreateNewPasswordFragment();
                                    Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);
                                }

                            } catch (StatusRuntimeException e1) {
                                Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                                if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
                                    NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToForgotPasswordFragment();
                                    Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);
                                }

                            }
                        } else {
                            Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
                                NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToForgotPasswordFragment();
                                Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);
                            }
                        }

                    } else if (e.getStatus().getCode().toString().equals("INTERNAL")) {
                        Toast.makeText(view.getContext(), "Server Error ..!!. Please Try After Some Time.", Toast.LENGTH_SHORT).show();

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
                            NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToSigninFragment();
                            Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);
                        }

                    } else if (e.getStatus().getCode().toString().equals("PERMISSION_DENIED")) {
                        Toast.makeText(view.getContext(), "Invalid OTP Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(view.getContext(), "Please Update Your App ..!!", Toast.LENGTH_SHORT).show();

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordConfirmOtpFragment) {
                            NavDirections actionForgotPasswordConfirmOtpToCreateNewPasswordFragment = ForgotPasswordConfirmOtpFragmentDirections.actionForgotPasswordConfirmOtpFragmentToSigninFragment();
                            Navigation.findNavController(view).navigate(actionForgotPasswordConfirmOtpToCreateNewPasswordFragment);
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