package com.aapanavyapar.aapanavyapar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.aapanavyapar.services.Location;
import com.aapanavyapar.adapter.ProductAdapter;
import com.aapanavyapar.adapter.SearchedShopAdapter;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.dataModel.ViewDataModel;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.serviceWrappers.GetProductsBySearchWrapper;
import com.aapanavyapar.serviceWrappers.GetShopsBySearchWrapper;
import com.aapanavyapar.serviceWrappers.GetTrendingShopsWrapper;
import com.aapanavyapar.viewData.ProductData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;


public class ProductSearchFragment extends Fragment {

    private GoogleMap googleMap;

    Spinner spinner;
    String[] choice = new String[]{"Product","Shop"};

    ChipGroup chipGroup;
    Chip chip;
    RecyclerView recyclerView;
    DataModel dataModel;
    ViewDataModel viewDataModel;

    RecyclerView.Adapter searchedAdapter;

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
        View rootView = inflater.inflate(R.layout.fragment_product_search, container, false);

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);
        viewDataModel = new ViewModelProvider(requireActivity()).get(ViewDataModel.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapView mapView = (MapView) rootView.findViewById(R.id.ps_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(callback);

        // Inflate the layout for this fragment
        return rootView;
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

        spinner = view.findViewById(R.id.ps_spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, choice);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position,long id){

                switch(position) {
                    case 0:
                        Toast.makeText(getContext(),"Product Search", Toast.LENGTH_LONG).show();

                        ArrayList<ProductData> productDataForProduct = new ArrayList<>();
                        searchedAdapter = new ProductAdapter(productDataForProduct, getContext());
                        recyclerView.setAdapter((ProductAdapter) searchedAdapter);

                        break;
                    case 1:
                        Toast.makeText(getContext(),"Shop Search", Toast.LENGTH_LONG).show();

                        ArrayList<ProductData> productDataForShop = new ArrayList<>();
                        searchedAdapter = new SearchedShopAdapter(productDataForShop, getContext());
                        recyclerView.setAdapter((SearchedShopAdapter) searchedAdapter);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //###
        //###  Product's Cards 
        //### 

        recyclerView = view.findViewById(R.id.ps_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<ProductData> productData = new ArrayList<>();
        searchedAdapter = new ProductAdapter(productData, getContext());
        recyclerView.setAdapter((ProductAdapter) searchedAdapter);

        caller = new Thread(new Runnable() {
            @Override
            public void run() {

                if(ViewProvider.currentLocation != null) {

                    GetTrendingShopsWrapper shopsWrapper = new GetTrendingShopsWrapper(requireActivity());
                    shopsWrapper.GetTrendingShops(dataModel.getAuthToken(), dataModel.getRefreshToken(), ViewProvider.currentLocation);
                }
            }
        });

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem().equals(choice[0])) {
                    ((ProductAdapter) searchedAdapter).makeEmpty();
                    googleMap.clear();

                    String searchString = mSearchView.getQuery().toString();
                    GetProductsBySearchWrapper searchWrapper = new GetProductsBySearchWrapper(requireActivity());
                    searchWrapper.getProductBySearch(dataModel.getAuthToken(), dataModel.getRefreshToken(), searchString, new RecycleViewUpdater() {
                        @Override
                        public void updateRecycleView(Object object) {

                            ProductData pd = (ProductData) object;
                            LatLng point = new LatLng(Double.parseDouble(pd.getShopLatitude()), Double.parseDouble(pd.getShopLongitude()));
                            googleMap.addMarker(new MarkerOptions().position(point).title(pd.getShopName()));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f));

                            ((ProductAdapter) searchedAdapter).addNewData((ProductData) object);
                        }
                    });
                } else {
                    ((SearchedShopAdapter) searchedAdapter).makeEmpty();
                    googleMap.clear();

                    String searchString = mSearchView.getQuery().toString();
                    GetShopsBySearchWrapper searchWrapper = new GetShopsBySearchWrapper(requireActivity());
                    searchWrapper.getShopsBySearch(dataModel.getAuthToken(), dataModel.getRefreshToken(), searchString, Location.newBuilder()
                            .setLatitude(String.valueOf(ViewProvider.currentLocation.getLatitude()))
                            .setLongitude(String.valueOf(ViewProvider.currentLocation.getLatitude()))
                            .build(), new RecycleViewUpdater() {
                        @Override
                        public void updateRecycleView(Object object) {

                            ProductData pd = (ProductData) object;
                            LatLng point = new LatLng(Double.parseDouble(pd.getShopLatitude()), Double.parseDouble(pd.getShopLongitude()));
                            googleMap.addMarker(new MarkerOptions().position(point).title(pd.getShopName()));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f));

                            ((SearchedShopAdapter) searchedAdapter).addNewData((ProductData) object);
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

    OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap gMap) {
            googleMap = gMap;
            Toast.makeText(getContext(), "MAP", Toast.LENGTH_LONG).show();
            if(viewDataModel.getTrendingShopsMap() != null && viewDataModel.getTrendingShopsMap().size() > 0) {
                for(String shopId : viewDataModel.getTrendingShopsMap().keySet()){

                    Location location = viewDataModel.getTrendingShopsMap().get(shopId).getShops().getLocation();
                    LatLng point = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));
                    googleMap.addMarker(new MarkerOptions().position(point).title(viewDataModel.getTrendingShopsMap().get(shopId).getShops().getShopName()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f));
                }
            }
        }
    };
}