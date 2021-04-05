package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.aapanavyapar.aapanavyapar.services.AuthenticationGrpc;
import com.aapanavyapar.aapanavyapar.services.ContactConformationRequest;
import com.aapanavyapar.aapanavyapar.services.ContactConformationResponse;
import com.aapanavyapar.aapanavyapar.services.NewTokenRequest;
import com.aapanavyapar.aapanavyapar.services.NewTokenResponse;
import com.aapanavyapar.constants.constants;
import com.aapanavyapar.dataModel.DataModel;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class SignupConfirmOtpFragment extends Fragment {


    public static final String host = MainActivity.IPAddress;
    public static final int port = 4356;

    private DataModel dataModel;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    Button conformOtp;
    EditText otpText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        mChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        return inflater.inflate(R.layout.fragment_signup_confirm_otp, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        conformOtp = view.findViewById(R.id.forgot_password_confirm_otp_button);
        otpText = view.findViewById(R.id.forgot_password_confirm_otp_text);

        conformOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!dataModel.CanWeUseTokenForThis(constants.ConformContact)){
                    Toast.makeText(getContext(), "Please Try Again ..!!", Toast.LENGTH_LONG).show();

                    NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                    Navigation.findNavController(view).navigate(actionWithOtp);
                }

                ContactConformationRequest request = ContactConformationRequest.newBuilder()
                        .setOtp(otpText.getText().toString().trim())
                        .setToken(dataModel.getAuthToken())
                        .setApiKey(MainActivity.API_KEY)
                        .build();
                try {
                    ContactConformationResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).contactConformation(request);

                    Toast.makeText(getContext(), "Success .. !! " + response.getToken(), Toast.LENGTH_LONG).show();

                    int []access = {constants.GetNewToken, constants.LogOut, constants.GetNewToken, constants.External};
                    dataModel.setTokens(response.getToken(), response.getRefreshToken(), access);


                }catch (StatusRuntimeException e){
                    if (e.getStatus().getCode().toString().equals("INVALID_ARGUMENTS")) {
                        Toast.makeText(view.getContext(), "Invalid OTP Try Again ..!!", Toast.LENGTH_SHORT).show();

                    } else if (e.getStatus().getCode().toString().equals("UNAUTHENTICATED")) {
                        if (e.getMessage().equals("Request With Invalid Token")) {
                            Toast.makeText(view.getContext(), "Update Refresh Token", Toast.LENGTH_SHORT).show();
                            NewTokenRequest newTokenRequest = NewTokenRequest.newBuilder()
                                    .setApiKey(MainActivity.API_KEY)
                                    .setRefreshToken(dataModel.getRefreshToken())
                                    .build();

                            try {
                                NewTokenResponse response = blockingStub.getNewToken(newTokenRequest);

                                int []access = {constants.GetNewToken, constants.LogOut, constants.External};
                                dataModel.setAccess(access);
                                dataModel.setAuthToken(response.getToken());

                                ContactConformationRequest reRequest = ContactConformationRequest.newBuilder()
                                        .setOtp(otpText.getText().toString().trim())
                                        .setToken(dataModel.getAuthToken())
                                        .setApiKey(MainActivity.API_KEY)
                                        .build();

                                ContactConformationResponse reResponse = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).contactConformation(reRequest);
                                Toast.makeText(getContext(), "Success .. !! " + response.getToken(), Toast.LENGTH_LONG).show();


                            }catch (StatusRuntimeException e1){
                                Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                                NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                                Navigation.findNavController(view).navigate(actionWithOtp);

                            }

                        } else {
                            Toast.makeText(view.getContext(), "Please Update Your Application", Toast.LENGTH_SHORT).show();

                            NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                            Navigation.findNavController(view).navigate(actionWithOtp);

                        }

                    } else if(e.getStatus().getCode().toString().equals("UNKNOWN")) {
                        Toast.makeText(view.getContext(), "Server Error ..!!. Please Try After Some Time.", Toast.LENGTH_SHORT).show();

                        NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);

                    } else if(e.getStatus().getCode().toString().equals("NOT_FOUND")) {
                        Toast.makeText(view.getContext(), "Please Try Again OTP Expired .. !!", Toast.LENGTH_SHORT).show();

                        NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);

                    } else if(e.getStatus().getCode().toString().equals("ABORTED")) {
                        Toast.makeText(view.getContext(), "Please Update The App .. !!. App Is Corrupted", Toast.LENGTH_SHORT).show();

                        NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);

                    } else if(e.getStatus().getCode().toString().equals("INTERNAL")) {
                        Toast.makeText(view.getContext(), "Please Try To SignIn", Toast.LENGTH_SHORT).show();

                        NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);

                    } else {
                        Toast.makeText(view.getContext(), "Unknown Error Occurred", Toast.LENGTH_SHORT).show();

                        NavDirections actionWithOtp = SignupConfirmOtpFragmentDirections.actionSignupConfirmOtpFragmentToSignupFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);

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