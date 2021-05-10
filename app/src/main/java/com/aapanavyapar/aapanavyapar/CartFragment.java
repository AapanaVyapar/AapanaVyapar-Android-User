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

import com.aapanavyapar.adapter.CartProductAdapter;
import com.aapanavyapar.adapter.OrderedProductAdapter;
import com.aapanavyapar.viewData.CartProductData;
import com.aapanavyapar.viewData.OrderedProductData;


public class CartFragment extends Fragment {

    RecyclerView cart_Recycler_View;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cart_Recycler_View = view.findViewById(R.id.cart_recycler_view);
        cart_Recycler_View.setHasFixedSize(true);
        cart_Recycler_View.setLayoutManager(new LinearLayoutManager(getContext()));

        CartProductData[] cart_product_data = new CartProductData[]{
                new CartProductData(R.drawable.logoav,"Rolex Watch","Fashion Wrist Watches","Shop Description"),
                new CartProductData(R.drawable.logoav,"Acer Laptop","Sonam Electronics","Shop Description"),
                new CartProductData(R.drawable.logoav,"Classic Cheeseburger","We Desi","Shop Description"),
                new CartProductData(R.drawable.logoav,"Yonex Badminton Kit","Jalgaon Sports","Shop Description"),
        };

        CartProductAdapter cart_product_adapter = new CartProductAdapter(cart_product_data, getContext());
        cart_Recycler_View.setAdapter(cart_product_adapter);
    }
}