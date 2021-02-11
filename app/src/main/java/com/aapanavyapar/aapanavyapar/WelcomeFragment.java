package com.aapanavyapar.aapanavyapar;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import java.util.concurrent.TimeUnit;

public class WelcomeFragment extends Fragment {
    Animation txt_anim,bounce;
    ImageView logo;
    TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //After Completion of Animation

        //Animation   ***
        //top = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        txt_anim = AnimationUtils.loadAnimation(this,R.anim.txt_anim);
        bounce = AnimationUtils.loadAnimation(this,R.anim.bounce);


        logo = findViewById(R.id.imageView);
        title = findViewById(R.id.textView2);

        //Applying Animation   ***
        //logo.setAnimation(top);
        title.setAnimation(txt_anim);
        logo.setAnimation(bounce);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // For SignIN
        NavDirections actionToIn = WelcomeFragmentDirections.actionWelcomeFragmentToSigninFragment();
        Navigation.findNavController(view).navigate(actionToIn);

    }
}
