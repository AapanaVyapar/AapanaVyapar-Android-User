package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ProductOnCardClick extends Fragment {

    ImageView productImage;
    TextView productName;

    public ProductOnCardClick() {
        // Required empty public constructor
        super(R.layout.fragment_product_on_card_click);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        productImage = (ImageView)view.findViewById(R.id.productoncardclick_image);
        productName = (TextView)view.findViewById(R.id.productoncardclick_productname_text);

//        productImage.setImageResource(getActivity().getIntent().getIntExtra("productImage",0));
//        productName.setText(getActivity().getIntent().getStringExtra("productName"));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_on_card_click, container, false);
    }
}