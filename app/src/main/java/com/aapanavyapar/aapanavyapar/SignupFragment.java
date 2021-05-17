package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.aapanavyapar.aapanavyapar.services.AuthenticationGrpc;
import com.aapanavyapar.aapanavyapar.services.SignUpRequest;
import com.aapanavyapar.aapanavyapar.services.SignUpResponse;
import com.aapanavyapar.constants.constants;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.validators.validators;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SignupFragment extends Fragment {

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;


    private DataModel dataModel;

    Button signUp;
    EditText mail;
    EditText password;
    EditText phone;
    EditText userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        mChannel = ManagedChannelBuilder.forTarget(MainActivity.AUTH_SERVICE_ADDRESS).usePlaintext().build();

        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUp = (Button)view.findViewById(R.id.signup);
        userName = (EditText)view.findViewById(R.id.sign_up_input_username);
        mail = (EditText)view.findViewById(R.id.sign_up_input_mail_id);
        password = (EditText)view.findViewById(R.id.sign_up_input_pass);
        phone = (EditText)view.findViewById(R.id.sign_up_input_phno);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validators.validateUserName(userName) && validators.validateMail(mail) && validators.validatePasswordSignUp(password)){
                    SignUpRequest request = SignUpRequest.newBuilder()
                            .setEmail(mail.getText().toString())
                            .setUsername(userName.getText().toString())
                            .setPhoneNo(phone.getText().toString())
                            .setPassword(password.getText().toString())
                            .setApiKey(MainActivity.API_KEY)
                            .build();
                    try{
                        SignUpResponse response = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).signup(request);
                        Log.d("MainActivity", "Success .. !!");
                        Log.d("MainActivity", "Auth Token : " + response.getResponseData().getToken());
                        Log.d("MainActivity", "Refresh Token : " + response.getResponseData().getRefreshToken());

                        int []access = {constants.GetNewToken, constants.ResendOTP, constants.ConformContact};

                        dataModel.setTokens(response.getResponseData().getToken(), response.getResponseData().getRefreshToken(), access);

                        NavDirections actionWithOtp = SignupFragmentDirections.actionSignupFragmentToSignupConfirmOtpFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);

                    }
                    catch(StatusRuntimeException e){

                        if(e.getStatus().getCode().toString().equals("UNAUTHENTICATED")){
                        Toast.makeText(view.getContext(),"Please Update Your Application", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("INVALID_ARGUMENT")){
                            Toast.makeText(view.getContext(), "Please Enter Valid Inputs", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("NOT_FOUND")) {
                            Toast.makeText(view.getContext(), "User Not Exist", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("ALREADY_EXISTS")) {
                            if(e.getMessage().equals("ALREADY_EXISTS: User Already Exist")){
                                Toast.makeText(view.getContext(), "User Already Exist Please Try With Another Mobile Number", Toast.LENGTH_SHORT).show();

                            } else{
                                Toast.makeText(view.getContext(), "Please Enter Otp", Toast.LENGTH_SHORT).show();
                                NavDirections actionWithOtp = SignupFragmentDirections.actionSignupFragmentToSignupConfirmOtpFragment();
                                Navigation.findNavController(view).navigate(actionWithOtp);

                            }

                        }  else if(e.getStatus().getCode().toString().equals("INTERNAL")) {
                            Toast.makeText(view.getContext(), "Server Error", Toast.LENGTH_SHORT).show();

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