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
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.validators.validators;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SignupFragment extends Fragment {

    public static final String host = "192.168.43.189";
    public static final int port = 4356;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;


    private DataModel dataModel;

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
        userName = (EditText)view.findViewById(R.id.sign_up_input_username);
        mail = (EditText)view.findViewById(R.id.sign_up_input_mail_id);
        password = (EditText)view.findViewById(R.id.sign_up_input_pass);
        phone = (EditText)view.findViewById(R.id.sign_up_input_phno);
        pinCode = (EditText)view.findViewById(R.id.sign_up_input_pincode);

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
                            .setApiKey(MainActivity.API_KEY)
                            .build();
                    try{
                        SignUpResponse response = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).signup(request);
                        Log.d("MainActivity", "Success .. !!");
                        Toast.makeText(view.getContext(), response.getResponseData().getToken(), Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "Auth Token : " + response.getResponseData().getToken());
                        Toast.makeText(view.getContext(), response.getResponseData().getRefreshToken(), Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "Refresh Token : " + response.getResponseData().getRefreshToken());

                        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);
                        dataModel.setTokens(response.getResponseData().getToken(), response.getResponseData().getRefreshToken());

                        NavDirections actionWithOtp = SignupFragmentDirections.actionSignupFragmentToSignupConfirmOtpFragment();
                        Navigation.findNavController(view).navigate(actionWithOtp);

                    }
                    catch(StatusRuntimeException e){
                        Log.d("MainActivity", e.getMessage());

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