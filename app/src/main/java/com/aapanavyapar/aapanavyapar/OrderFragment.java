package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.aapanavyapar.adapter.OrderedProductAdapter;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.serviceWrappers.GetOrdersWrapper;
import com.aapanavyapar.viewData.OrderedProductData;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    ImageButton cartImageButton;
    RecyclerView orderRecyclerView;
    DataModel dataModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartImageButton = view.findViewById(R.id.cart_imagebutton);
        cartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CartFragment.class,null).addToBackStack(null).commit();
            }
        });

        orderRecyclerView = view.findViewById(R.id.orderd_recycler_view);
        orderRecyclerView.setHasFixedSize(true);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<OrderedProductData> orderedProductData = new ArrayList<>();
        OrderedProductAdapter orderedProductAdapter = new OrderedProductAdapter(orderedProductData, getContext());
        orderRecyclerView.setAdapter(orderedProductAdapter);

        GetOrdersWrapper wrapper = new GetOrdersWrapper();
        wrapper.GetOrders(dataModel.getAuthToken(), new RecycleViewUpdater() {
            @Override
            public void updateRecycleView(Object object) {
                orderedProductAdapter.addNewData((OrderedProductData) object);
            }
        });
    }
}