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
import com.aapanavyapar.aapanavyapar.services.NewTokenRequest;
import com.aapanavyapar.aapanavyapar.services.NewTokenResponse;
import com.aapanavyapar.aapanavyapar.services.SetNewPasswordRequest;
import com.aapanavyapar.aapanavyapar.services.SetNewPasswordResponse;
import com.aapanavyapar.constants.constants;
import com.aapanavyapar.dataModel.DataModel;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class CreateNewPasswordFragment extends Fragment {

    public static final String host = MainActivity.IPAddress;
    public static final int port = 4356;

    private DataModel dataModel;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    EditText input_new_password;
    EditText input_confirm_password;
    Button btnConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

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
        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        input_new_password = (EditText) view.findViewById(R.id.create_password);
        input_confirm_password = (EditText) view.findViewById(R.id.confirm_password);
        btnConfirm = (Button) view.findViewById(R.id.next_btn_create_new_password);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetNewPasswordRequest request = SetNewPasswordRequest.newBuilder()
                        .setNewPassword(input_new_password.getText().toString())
                        .setNewPassToken(dataModel.getPassToken())
                        .setApiKey(MainActivity.API_KEY)
                        .build();

                try {
                    SetNewPasswordResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).setNewPassword(request);

                    Toast.makeText(getContext(), "Success .. !! " + response.getStatus(), Toast.LENGTH_LONG).show();

                    dataModel.setPassToken("");

                    NavDirections actionCreateNewPasswordFragmentToSigninFragment = CreateNewPasswordFragmentDirections.actionCreateNewPasswordFragmentToSigninFragment();
                    Navigation.findNavController(view).navigate(actionCreateNewPasswordFragmentToSigninFragment);


                } catch (StatusRuntimeException e) {
                    if (e.getStatus().getCode().toString().equals("InvalidArgument")) {
                        Toast.makeText(getContext(), "Please Enter Stronger Password .. !! ", Toast.LENGTH_LONG).show();

                    } else if (e.getStatus().getCode().toString().equals("Unauthenticated")) {
                        if (e.getMessage().equals("Request With Invalid Token")) {
                            Toast.makeText(view.getContext(), "Update Refresh Token", Toast.LENGTH_SHORT).show();
                            NewTokenRequest newTokenRequest = NewTokenRequest.newBuilder()
                                    .setApiKey(MainActivity.API_KEY)
                                    .setRefreshToken(dataModel.getRefreshToken())
                                    .build();

                            try {
                                NewTokenResponse newTokenResponse = blockingStub.getNewToken(newTokenRequest);

                                int[] access = {constants.GetNewToken, constants.ResendOTP, constants.ForgetPassword};
                                dataModel.setAccess(access);
                                dataModel.setAuthToken(newTokenResponse.getToken());

                                SetNewPasswordRequest setNewPasswordRequest = SetNewPasswordRequest.newBuilder()
                                        .setNewPassword(input_new_password.getText().toString())
                                        .setNewPassToken(dataModel.getPassToken())
                                        .setApiKey(MainActivity.API_KEY)
                                        .build();

                                SetNewPasswordResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).setNewPassword(setNewPasswordRequest);
                                if (response.getStatus()) {
                                    Toast.makeText(getContext(), "Success .. !! " + response, Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getContext(), "Fail To Update Password Please Try Again .. !! ", Toast.LENGTH_LONG).show();

                                }

                                NavDirections actionCreateNewPasswordFragmentToSigninFragment = CreateNewPasswordFragmentDirections.actionCreateNewPasswordFragmentToSigninFragment();
                                Navigation.findNavController(view).navigate(actionCreateNewPasswordFragmentToSigninFragment);

                            } catch (StatusRuntimeException e1) {
                                Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                                NavDirections actionCreateNewPasswordFragmentToSigninFragment = CreateNewPasswordFragmentDirections.actionCreateNewPasswordFragmentToSigninFragment();
                                Navigation.findNavController(view).navigate(actionCreateNewPasswordFragmentToSigninFragment);

                            }

                        } else {
                            Toast.makeText(view.getContext(), "Please Update Your Application", Toast.LENGTH_SHORT).show();

                            NavDirections actionCreateNewPasswordFragmentToSigninFragment = CreateNewPasswordFragmentDirections.actionCreateNewPasswordFragmentToSigninFragment();
                            Navigation.findNavController(view).navigate(actionCreateNewPasswordFragmentToSigninFragment);

                        }

                        Log.d("ConfirmOtpFragment", e.getMessage());

                    }else {
                        Toast.makeText(view.getContext(), "Server Error Please Try After Some Time ..!!", Toast.LENGTH_SHORT).show();

                        NavDirections actionCreateNewPasswordFragmentToSigninFragment = CreateNewPasswordFragmentDirections.actionCreateNewPasswordFragmentToSigninFragment();
                        Navigation.findNavController(view).navigate(actionCreateNewPasswordFragmentToSigninFragment);

                    }
                }
            }
        });
    }
}