package com.aapanavyapar.aapanavyapar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;
import android.widget.Toast;

import com.aapanavyapar.aapanavyapar.services.AuthenticationGrpc;
import com.aapanavyapar.aapanavyapar.services.SignInRequest;
import com.aapanavyapar.aapanavyapar.services.SignInResponse;
import com.aapanavyapar.constants.constants;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.validators.validators;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SigninFragment extends Fragment {

    private DataModel dataModel;

    EditText phoneNo;
    Button signIn, signUp;
    EditText password;
    TextView forgotPassword;

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;
    AuthenticationGrpc.AuthenticationStub asyncStub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        mChannel = ManagedChannelBuilder.forTarget(MainActivity.AUTH_SERVICE_ADDRESS).usePlaintext().build();

        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
        asyncStub = AuthenticationGrpc.newStub(mChannel);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        phoneNo = (EditText)view.findViewById(R.id.sign_in_input_phoneNo_port);
        signIn = (Button)view.findViewById(R.id.sign_in);
        signUp = (Button)view.findViewById(R.id.sign_up_in);
        password = (EditText)view.findViewById(R.id.sign_in_input_password_port);
        forgotPassword = (TextView)view.findViewById(R.id.sign_in_forgot_password_port);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionToForgotPassword = SigninFragmentDirections.actionSigninFragmentToForgotPasswordFragment();
                Navigation.findNavController(view).navigate(actionToForgotPassword);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionToUp = SigninFragmentDirections.actionSigninFragmentToSignupFragment();
                Navigation.findNavController(view).navigate(actionToUp);
            }
        });
//        Intent intent  = new Intent(getContext(), ViewProvider.class);
//        startActivity(intent);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validators.validatePhone(phoneNo) && validators.validatePasswordSignIn(password)){
                    SignInRequest request = SignInRequest.newBuilder()
                            .setPhoneNo(phoneNo.getText().toString())
                            .setPassword(password.getText().toString())
                            .setApiKey(MainActivity.API_KEY)
                            .build();


                    try {
                        SignInResponse response = blockingStub.withDeadlineAfter(2, TimeUnit.MINUTES).signIn(request);

                        int []access = {constants.GetNewToken, constants.LogOut, constants.External};

                        dataModel.setTokens(response.getResponseData().getToken(), response.getResponseData().getRefreshToken(), access);

                        Log.d("MainActivity", "Success .. !!");
                        Log.d("MainActivity", "Auth Token : " + response.getResponseData().getToken());
                        Log.d("MainActivity", "Refresh Token : " + response.getResponseData().getRefreshToken());

                        AuthDB authdb = new AuthDB(getContext());
                        SQLiteDatabase db = authdb.getReadableDatabase();

                        authdb.insertData(dataModel.getRefreshToken(),access);

                        Intent intent  = new Intent(getContext(), ViewProvider.class);
                        intent.putExtra("Token",dataModel.getRefreshToken());
                        intent.putExtra("AuthToken",dataModel.getAuthToken());
                        intent.putExtra("Access",access);
                        startActivity(intent);

                    }catch (StatusRuntimeException e){

                        if (e.getStatus().getCode().toString().equals("UNAUTHENTICATED")) {
                            Toast.makeText(view.getContext(),"Please Update Your Application", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("INVALID_ARGUMENT")) {
                            Toast.makeText(view.getContext(), "Please Enter Valid Inputs", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("NOT_FOUND")) {
                            Toast.makeText(view.getContext(), "User Not Exist", Toast.LENGTH_SHORT).show();
                            NavDirections actionToUp = SigninFragmentDirections.actionSigninFragmentToSignupFragment();
                            Navigation.findNavController(view).navigate(actionToUp);

                        } else if(e.getStatus().getCode().toString().equals("DEADLINE_EXCEEDED")) {
                            Toast.makeText(view.getContext(), "Network Error", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("PERMISSION_DENIED")) {
                            Toast.makeText(view.getContext(), "Invalid UserName Or Password", Toast.LENGTH_SHORT).show();

                        } else if(e.getStatus().getCode().toString().equals("INTERNAL")) {
                            Toast.makeText(view.getContext(), "Server Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }  else {
                            Toast.makeText(view.getContext(), "Unknown Error Occurred : " + e.getMessage(), Toast.LENGTH_SHORT).show();

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