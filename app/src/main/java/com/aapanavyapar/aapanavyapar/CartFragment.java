package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aapanavyapar.adapter.CartProductAdapter;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.serviceWrappers.GetCartWrapper;
import com.aapanavyapar.viewData.ProductData;

import java.util.ArrayList;


public class CartFragment extends Fragment {

    RecyclerView cartRecyclerView;
    Thread caller;

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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        cartRecyclerView.setHasFixedSize(true);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<ProductData> cartProductData = new ArrayList<>();
        CartProductAdapter cartProductAdapter = new CartProductAdapter(cartProductData, getContext());
        cartRecyclerView.setAdapter(cartProductAdapter);

        caller = new Thread(new Runnable() {
            @Override
            public void run() {

                GetCartWrapper cartWrapper = new GetCartWrapper();
                cartWrapper.getCart(dataModel.getAuthToken(), dataModel.getRefreshToken(), new RecycleViewUpdater() {
                    @Override
                    public void updateRecycleView(Object object) {
                        Log.d("CART_FRAGMENT", "Received Update");
                        cartProductAdapter.addNewData((ProductData) object);
                    }
                });

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        caller.interrupt();
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