package com.aapanavyapar.aapanavyapar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;


import com.aapanavyapar.adapter.ProductAdapter;
import com.aapanavyapar.dataModel.DataModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.aapanavyapar.viewData.ProductData;

import java.io.InputStream;


public class TrendingFragment extends Fragment {

    ChipGroup chipGroup;
    Chip chip;
    RecyclerView recycler_View;
//    RatingBar rating_Bar;
    DataModel dataModel;


    public TrendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);


        return inflater.inflate(R.layout.fragment_trending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getContext(),dataModel.getRefreshToken().toString(),Toast.LENGTH_SHORT).show();
        String arr[] = {"Food", "Clothes", "Electronics", "Devotional", "Sports", "Cosmetics"};

        chipGroup = view.findViewById(R.id.chipgroup);
        for (int i = 0; i < arr.length; i++) {
            chip = new Chip(getContext());
            chip.setText(arr[i]);
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, R.style.CustomChipStyle);
            chip.setChipDrawable(chipDrawable);
            chip.setId(i);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                  Chip chipClick = (Chip) v;
                    Chip chipClick = v.findViewById(v.getId());
                    Toast.makeText(getContext(), chipClick.getText(), Toast.LENGTH_LONG).show();
                }
            });


            chipGroup.addView(chip);

            recycler_View = view.findViewById(R.id.recycler_view);
            recycler_View.setHasFixedSize(true);
            recycler_View.setLayoutManager(new LinearLayoutManager(getContext()));

            ProductData[] product_data = new ProductData[]{
                    new ProductData(R.drawable.logoav,"Rolex Women's Watch","Fashion Wrist Watches"),
                    new ProductData(R.drawable.logoav,"Acer Laptop","Sonam Electronics"),
                    new ProductData(R.drawable.logoav,"Classic Cheeseburger","We Desi"),
                    new ProductData(R.drawable.logoav,"Yonex Badminton Kit","Jalgaon Sports"),
            };

            ProductAdapter product_adapter = new ProductAdapter(product_data, getContext());
            recycler_View.setAdapter(product_adapter);

        }


    }

}