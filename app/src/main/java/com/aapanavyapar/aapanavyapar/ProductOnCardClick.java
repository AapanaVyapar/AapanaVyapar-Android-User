package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.viewData.ProductData;


public class ProductOnCardClick extends Fragment {

    private DataModel dataModel;

    ProductData productData;

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

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        assert getArguments() != null;
        productData = (ProductData) getArguments().getSerializable("dataFill");

    }

}