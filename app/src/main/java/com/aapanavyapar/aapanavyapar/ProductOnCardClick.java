package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.aapanavyapar.viewData.ProductData;


public class ProductOnCardClick extends Fragment {

    ProductData productData;
    Button buyNow;

    public ProductOnCardClick() {
        // Required empty public constructor
        super(R.layout.fragment_product_on_card_click);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_on_card_click, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){

        assert getArguments() != null;
        productData = (ProductData) getArguments().getSerializable("dataFill");

        buyNow = view.findViewById(R.id.productoncardclick_button_buy);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                Bundle args = new Bundle();
                args.putSerializable("dataFill", productData);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, BuyingDetailsFragment.class, args).addToBackStack(null).commit();
            }
        });

    }

}