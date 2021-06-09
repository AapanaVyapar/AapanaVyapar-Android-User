package com.aapanavyapar.aapanavyapar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.aapanavyapar.services.Category;
import com.aapanavyapar.adapter.ProductAdapter;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.dataModel.ViewDataModel;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.serviceWrappers.GetTrendingProductsWrapper;
import com.aapanavyapar.serviceWrappers.GetTrendingShopsWrapper;
import com.aapanavyapar.viewData.ProductData;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TrendingFragment extends Fragment {

    ChipGroup chipGroup;
    Chip chip;
    RecyclerView recyclerView;
    DataModel dataModel;

    Map<String, ArrayList<ProductData>> categoriesMap;
    ArrayList<ProductData> allData;

    public static Thread caller = null;
    ProductAdapter productAdapter;

    String currentActiveChip = "ALL";

    String chipsCollection[] = {
            "ALL",
            "SPORTS_AND_FITNESS",
            "ELECTRIC",
            "DEVOTIONAL",
            "AGRICULTURAL",
            "WOMENS_CLOTHING",
            "WOMENS_ACCESSORIES",
            "MENS_CLOTHING",
            "MENS_ACCESSORIES",
            "HOME_GADGETS",
            "TOYS",
            "ELECTRONIC",
            "DECORATION",
            "FOOD",
            "STATIONERY",
            "BAGS",
            "HARDWARE",
            "FURNITURE",
            "PACKAGING_AND_PRINTING",
            "BEAUTY_AND_PERSONAL_CARE",
            "CHEMICALS",
            "GARDEN",
            "KITCHEN",
            "MACHINERY",

    };

    public TrendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);
        categoriesMap = new HashMap<>();
        allData = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chipGroup = view.findViewById(R.id.chipgroup);
        for (int i = 0; i < chipsCollection.length; i++) {
            chip = new Chip(getContext());
            chip.setText(chipsCollection[i]);
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, R.style.CustomChipStyle);
            chip.setChipDrawable(chipDrawable);
            chip.setId(i);
            chip.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    Chip chipClick = v.findViewById(v.getId());
                    Toast.makeText(getContext(), chipClick.getText(), Toast.LENGTH_LONG).show();

                    currentActiveChip = chipClick.getText().toString();

                    if(chipClick.getText() == "ALL"){
                        productAdapter.notifyData(allData);
                    }else {
                        productAdapter.notifyData(categoriesMap.get(chipClick.getText().toString()));
                    }
                }
            });
            categoriesMap.put(chipsCollection[i], new ArrayList<ProductData>());
            chipGroup.addView(chip);
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productAdapter = new ProductAdapter(allData, getContext());
        recyclerView.setAdapter(productAdapter);


        caller = new Thread(new Runnable() {
            @Override
            public void run() {

                if(ViewProvider.currentLocation != null) {

                    GetTrendingShopsWrapper shopsWrapper = new GetTrendingShopsWrapper(requireActivity());
                    shopsWrapper.GetTrendingShops(dataModel.getAuthToken(), dataModel.getRefreshToken(), ViewProvider.currentLocation);

                    GetTrendingProductsWrapper getTrendingProductsWrapper = new GetTrendingProductsWrapper(requireActivity());
                    getTrendingProductsWrapper.GetTrendingProducts(dataModel.getAuthToken(), dataModel.getRefreshToken(), new RecycleViewUpdater() {
                        @Override
                        public void updateRecycleView(Object object) {
                            ProductData productData = (ProductData) object;

                            allData.add(productData);
                            for(Category productCategory : productData.getProductCategories()){

                                ArrayList<ProductData> dataArrayList = categoriesMap.get(productCategory.name());
                                dataArrayList.add(productData);

                                categoriesMap.put(productCategory.name(), dataArrayList);
                                if(currentActiveChip.equals(productCategory.name())) {
                                    productAdapter.addNewData(productData);
                                }
                            }

//                            if(currentActiveChip.equals("ALL")) {
//                                productAdapter.addNewData(productData);
//                            }
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

}