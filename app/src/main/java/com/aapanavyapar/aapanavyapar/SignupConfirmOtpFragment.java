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
import com.aapanavyapar.aapanavyapar.services.ContactConformationRequest;
import com.aapanavyapar.aapanavyapar.services.ContactConformationResponse;
import com.aapanavyapar.dataModel.DataModel;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class SignupConfirmOtpFragment extends Fragment {


    public static final String host = "192.168.8.21";
    public static final int port = 4356;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    Button conformOtp;
    EditText otpText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        return inflater.inflate(R.layout.fragment_signup_confirm_otp, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DataModel dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);


        conformOtp = view.findViewById(R.id.sign_up_confirm_otp_button);
        otpText = view.findViewById(R.id.sign_up_confirm_otp_otp_text);

        conformOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactConformationRequest request = ContactConformationRequest.newBuilder().setOtp(otpText.getText().toString().trim()).setToken(dataModel.getAuthToken()).build();
                try {
                    ContactConformationResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).contactConformation(request);

                    Toast.makeText(getContext(), "Success .. !! " + response.getToken(), Toast.LENGTH_LONG).show();

                }catch (StatusRuntimeException e){
                    Log.d("ConfirmOtpFragment", e.getMessage());

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