package com.aapanavyapar.aapanavyapar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.adapter.SearchedProductAdapter;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.serviceWrappers.GetTrendingProductsWrapper;
import com.aapanavyapar.serviceWrappers.GetTrendingShopsWrapper;
import com.aapanavyapar.viewData.ProductData;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;


public class ProductSearchFragment extends Fragment {

    ChipGroup chipGroup;
    Chip chip;
    RecyclerView recyclerView;
    DataModel dataModel;

    SearchedProductAdapter searchedProductAdapter;

    public static Thread caller;

    SearchView mSearchView;

    public ProductSearchFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_search, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchView = view.findViewById(R.id.ps_search_title);

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showInputMethod(view.findFocus());
                }
            }
        });

        //###
        //###   Chips 
        //### 

        Toast.makeText(getContext(), dataModel.getRefreshToken(),Toast.LENGTH_SHORT).show();
        String[] arr = {"Food", "Clothes", "Electronics", "Devotional", "Sports", "Cosmetics","Hardware"};

        chipGroup = view.findViewById(R.id.ps_chipgroup);
        for (int i = 0; i < arr.length; i++) {
            chip = new Chip(getContext());
            chip.setText(arr[i]);
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, R.style.CustomChipStyle);
            chip.setChipDrawable(chipDrawable);
            chip.setId(i);
            chip.setOnClickListener(v -> {
                Chip chipClick = v.findViewById(v.getId());
                Toast.makeText(getContext(), chipClick.getText(), Toast.LENGTH_LONG).show();
            });
            chipGroup.addView(chip);
        }
        //###
        //###  Product's Cards 
        //### 

        recyclerView = view.findViewById(R.id.ps_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<ProductData> productData = new ArrayList<>();
        searchedProductAdapter = new SearchedProductAdapter(productData, getContext());
        recyclerView.setAdapter(searchedProductAdapter);

        caller = new Thread(new Runnable() {
            @Override
            public void run() {

//                    while (ViewProvider.currentLocation == null) {
//                        Log.d("TrendingFragment", "Waiting For Location");
//                    }
                if(ViewProvider.currentLocation != null) {

                    GetTrendingShopsWrapper shopsWrapper = new GetTrendingShopsWrapper(requireActivity());
                    shopsWrapper.GetTrendingShops(dataModel.getAuthToken(), dataModel.getRefreshToken(), ViewProvider.currentLocation);

                    GetTrendingProductsWrapper getTrendingProductsWrapper = new GetTrendingProductsWrapper(requireActivity());
                    getTrendingProductsWrapper.GetTrendingProducts(dataModel.getAuthToken(), dataModel.getRefreshToken(), new RecycleViewUpdater() {
                        @Override
                        public void updateRecycleView(Object object) {
                            Log.d("TRENDING_FRAGMENT", "Received Update");
                            searchedProductAdapter.addNewData((ProductData) object);
                        }
                    });
                }
            }
        });
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

    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }
}