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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class ForgotPasswordFragment extends Fragment {

    private DataModel dataModel;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    EditText input_phone;
    Button btnSendOtp;

    //Button send_OTP;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        mChannel = ManagedChannelBuilder.forTarget(MainActivity.AUTH_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        input_phone = (EditText)view.findViewById(R.id.forgot_phone_number);
        btnSendOtp = (Button)view.findViewById(R.id.get_OTP);
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validators.validatePhone(input_phone)) {
                    ForgetPasswordRequest request = ForgetPasswordRequest.newBuilder()
                            .setPhoNo(input_phone.getText().toString())
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

                        dataModel.setTokens(response.getResponseData().getToken(), response.getResponseData().getRefreshToken(), access);

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordFragment) {
                            NavDirections actionForgotPasswordFragmentToForgotPasswordConfirmOtp = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotPasswordConfirmOtpFragment();
                            Navigation.findNavController(view).navigate(actionForgotPasswordFragmentToForgotPasswordConfirmOtp);
                        }

                    } catch (StatusRuntimeException e) {
                        if(e.getStatus().getCode().toString().equals("UNAUTHENTICATED")){
                            Toast.makeText(view.getContext(),"Please Update Your Application", Toast.LENGTH_SHORT).show();

                            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordFragment) {
                                NavDirections actionForgotPasswordFragmentToForgotPasswordConfirmOtp = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSigninFragment();
                                Navigation.findNavController(view).navigate(actionForgotPasswordFragmentToForgotPasswordConfirmOtp);
                            }

                        } else if(e.getStatus().getCode().toString().equals("INVALID_ARGUMENT")){
                            Toast.makeText(view.getContext(), "Please Enter Valid Inputs", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("PERMISSION_DENIED")) {
                            Toast.makeText(view.getContext(), "User Not Exist", Toast.LENGTH_SHORT).show();

                            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordFragment) {
                                NavDirections actionForgotPasswordFragmentToForgotPasswordConfirmOtp = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSignupFragment();
                                Navigation.findNavController(view).navigate(actionForgotPasswordFragmentToForgotPasswordConfirmOtp);
                            }

                        } else if(e.getStatus().getCode().toString().equals("INTERNAL")) {
                            Toast.makeText(view.getContext(), "Server Error", Toast.LENGTH_SHORT).show();

                            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordFragment) {
                                NavDirections actionForgotPasswordFragmentToForgotPasswordConfirmOtp = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSigninFragment();
                                Navigation.findNavController(view).navigate(actionForgotPasswordFragmentToForgotPasswordConfirmOtp);
                            }

                        } else if(e.getStatus().getCode().toString().equals("ALREADY_EXISTS")) {
                            if(dataModel.getAuthToken() == null || dataModel.getAuthToken().isEmpty()){
                                Toast.makeText(view.getContext(), "Please Try Again After An Hour ..!!", Toast.LENGTH_SHORT).show();

                                if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordFragment) {
                                    NavDirections actionForgotPasswordFragmentToForgotPasswordConfirmOtp = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSigninFragment();
                                    Navigation.findNavController(view).navigate(actionForgotPasswordFragmentToForgotPasswordConfirmOtp);
                                }

                            }else {
                                Toast.makeText(view.getContext(), "Please Enter OTP", Toast.LENGTH_SHORT).show();

                                if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordFragment) {
                                    NavDirections actionForgotPasswordFragmentToForgotPasswordConfirmOtp = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotPasswordConfirmOtpFragment();
                                    Navigation.findNavController(view).navigate(actionForgotPasswordFragmentToForgotPasswordConfirmOtp);
                                }
                            }
                        } else {
                            Toast.makeText(view.getContext(), "Unknown Error Occurred", Toast.LENGTH_SHORT).show();

                            if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.ForgotPasswordFragment) {
                                NavDirections actionForgotPasswordFragmentToForgotPasswordConfirmOtp = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSigninFragment();
                                Navigation.findNavController(view).navigate(actionForgotPasswordFragmentToForgotPasswordConfirmOtp);
                            }
                        }
                        Log.d("ForgotPasswordFragment", e.toString());

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