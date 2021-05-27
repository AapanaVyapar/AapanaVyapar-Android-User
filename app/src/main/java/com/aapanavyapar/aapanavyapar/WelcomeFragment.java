package com.aapanavyapar.aapanavyapar;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

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

        logo = view.findViewById(R.id.imageView);
        title = view.findViewById(R.id.textView2);

        Animation animation = new Animation() {
            @Override
            public void initialize(int width, int height, int parentWidth, int parentHeight) {
                super.initialize(width, height, parentWidth, parentHeight);

                txt_anim = AnimationUtils.loadAnimation(getContext(), R.anim.txt_anim);
                bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                setDuration(2000);
            }
        };

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                title.setAnimation(txt_anim);
                logo.setAnimation(bounce);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                 //For SignIN
                NavDirections actionToIn = WelcomeFragmentDirections.actionWelcomeFragmentToSigninFragment();
                Navigation.findNavController(view).navigate(actionToIn);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        view.setAnimation(animation);

    }

}
