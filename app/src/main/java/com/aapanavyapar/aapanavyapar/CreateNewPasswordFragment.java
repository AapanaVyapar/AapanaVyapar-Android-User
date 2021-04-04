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
import com.aapanavyapar.aapanavyapar.services.SetNewPasswordRequest;
import com.aapanavyapar.aapanavyapar.services.SetNewPasswordResponse;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class CreateNewPasswordFragment extends Fragment {

    public static final String host = "192.168.43.159";
    public static final int port = 4356;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    EditText input_new_password;
    EditText input_confirm_password;
    Button btnConfirm;

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
        return inflater.inflate(R.layout.fragment_create_new_password, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        input_new_password = (EditText) view.findViewById(R.id.create_password);
        input_confirm_password = (EditText) view.findViewById(R.id.confirm_password);
        btnConfirm = (Button) view.findViewById(R.id.next_btn_create_new_password);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetNewPasswordRequest request = SetNewPasswordRequest.newBuilder()

                        .setNewPassword(input_new_password.getText().toString())
                        .setApiKey(MainActivity.API_KEY)
                        .build();

                try {
                    SetNewPasswordResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).setNewPassword(request);

                    Toast.makeText(getContext(), "Success .. !! " + response.getStatus(), Toast.LENGTH_LONG).show();

                    NavDirections actionCreateNewPasswordFragmentToSigninFragment = CreateNewPasswordFragmentDirections.actionCreateNewPasswordFragmentToSigninFragment();
                    Navigation.findNavController(view).navigate(actionCreateNewPasswordFragmentToSigninFragment);


                }catch (StatusRuntimeException e){
                    Log.d("ConfirmOtpFragment", e.getMessage());

                }

            }
        });
    }
}