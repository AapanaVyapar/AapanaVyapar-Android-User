package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aapanavyapar.dataModel.DataModel;


public class SignupConfirmOtpFragment extends Fragment {

    private DataModel dataModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_signup_confirm_otp, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataModel = new ViewModelProvider(this).get(DataModel.class);
        Toast.makeText(getContext(), dataModel.getAuthToken(), Toast.LENGTH_LONG).show();




        // For SignUp

        //NavDirections actionToUp = WelcomeFragmentDirections.actionWelcomeFragmentToSignupFragment();
        //Navigation.findNavController(view).navigate(actionToUp);




    }

}