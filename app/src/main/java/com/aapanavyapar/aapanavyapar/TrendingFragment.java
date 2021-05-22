package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.adapter.ProductAdapter;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.serviceWrappers.GetTrendingProductsWrapper;
import com.aapanavyapar.viewData.ProductData;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;


public class TrendingFragment extends Fragment {

    ChipGroup chipGroup;
    Chip chip;
    RecyclerView recyclerView;
    DataModel dataModel;

    Thread caller;
    ProductAdapter productAdapter;

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
                    Chip chipClick = v.findViewById(v.getId());
                    Toast.makeText(getContext(), chipClick.getText(), Toast.LENGTH_LONG).show();
                }
            });
            chipGroup.addView(chip);

            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            ArrayList<ProductData> productData = new ArrayList<>();
            productAdapter = new ProductAdapter(productData, getContext());
            recyclerView.setAdapter(productAdapter);


            caller = new Thread(new Runnable() {
                @Override
                public void run() {

//                    while (ViewProvider.currentLocation == null) {
//                        Log.d("TrendingFragment", "Waiting For Location");
//                    }
                    if(ViewProvider.currentLocation != null) {
//                        Toast.makeText(getContext(), "Lat : " + String.valueOf(ViewProvider.currentLocation.getLatitude()) + ", Long : " + String.valueOf(ViewProvider.currentLocation.getLongitude()), Toast.LENGTH_SHORT).show();

                        GetTrendingProductsWrapper getTrendingProductsWrapper = new GetTrendingProductsWrapper();
                        boolean b = getTrendingProductsWrapper.GetTrendingProducts(dataModel.getAuthToken(), ViewProvider.currentLocation, new RecycleViewUpdater() {
                            @Override
                            public void updateRecycleView(Object object) {
                                Log.d("TRENDING_FRAGMENT", "Received Update");
                                productAdapter.addNewData((ProductData) object);
                            }
                        });
                        if (!b) {
                            Toast.makeText(getContext(), "Error Occurred While Receiving Data ...!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        caller.start();
        try {
            caller.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        caller.interrupt();
    }

}