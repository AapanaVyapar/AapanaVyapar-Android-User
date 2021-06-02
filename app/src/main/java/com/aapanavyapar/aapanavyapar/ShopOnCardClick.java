package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapanavyapar.adapter.ProductAdapter;
import com.aapanavyapar.viewData.ProductData;

import java.util.ArrayList;


public class ShopOnCardClick extends Fragment {


    ProductAdapter productAdapter;
    RecyclerView recyclerView ;

    public ShopOnCardClick() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<ProductData> productData = new ArrayList<>();
        productAdapter = new ProductAdapter(productData, getContext());
        recyclerView.setAdapter(productAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_shop_on_card_click, container, false);
    }
}