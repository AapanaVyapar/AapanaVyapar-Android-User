package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class WelcomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //After Completion of Animation

        // For SignUp

        NavDirections actionToUp = WelcomeFragmentDirections.actionWelcomeFragmentToSignupFragment();
        Navigation.findNavController(view).navigate(actionToUp);


        // For SignIN
//        NavDirections actionToIn = WelcomeFragmentDirections.actionWelcomeFragmentToSigninFragment();
//        Navigation.findNavController(view).navigate(actionToIn);

    }
}
