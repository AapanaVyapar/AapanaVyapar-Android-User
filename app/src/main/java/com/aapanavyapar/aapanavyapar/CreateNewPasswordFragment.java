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
import com.aapanavyapar.aapanavyapar.services.SetNewPasswordRequest;
import com.aapanavyapar.aapanavyapar.services.SetNewPasswordResponse;
import com.aapanavyapar.dataModel.DataModel;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class CreateNewPasswordFragment extends Fragment {

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

        mChannel = ManagedChannelBuilder.forTarget(MainActivity.AUTH_SERVICE_ADDRESS).usePlaintext().build();
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
                    SetNewPasswordResponse response = blockingStub.withDeadlineAfter(1, TimeUnit.MINUTES).setNewPassword(request);

                    Toast.makeText(getContext(), "Success .. !! " + response.getStatus(), Toast.LENGTH_LONG).show();

                    dataModel.setPassToken("");

                    if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.CreateNewPasswordFragment) {
                        NavDirections actionCreateNewPasswordFragmentToSigninFragment = CreateNewPasswordFragmentDirections.actionCreateNewPasswordFragmentToSigninFragment();
                        Navigation.findNavController(view).navigate(actionCreateNewPasswordFragmentToSigninFragment);
                    }



                } catch (StatusRuntimeException e) {
                    Log.d("NewPasswordFragment", e.toString());
                    Log.d("NewPasswordFragment", e.getStatus().getCode().toString());
                    if (e.getStatus().getCode().toString().equals("INVALID_ARGUMENT")) {
                        Toast.makeText(getContext(), "Please Enter Stronger Password .. !! ", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(view.getContext(), "Please Try Again .. !!", Toast.LENGTH_SHORT).show();

                        if (Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId() == R.id.CreateNewPasswordFragment) {
                            NavDirections actionCreateNewPasswordFragmentToSigninFragment = CreateNewPasswordFragmentDirections.actionCreateNewPasswordFragmentToSigninFragment();
                            Navigation.findNavController(view).navigate(actionCreateNewPasswordFragmentToSigninFragment);
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