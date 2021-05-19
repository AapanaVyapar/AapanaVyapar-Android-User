package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.aapanavyapar.adapter.OrderedProductAdapter;
import com.aapanavyapar.adapter.ProductAdapter;
import com.aapanavyapar.viewData.OrderedProductData;
import com.aapanavyapar.viewData.ProductData;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    ImageButton cartImageButton;
    RecyclerView ordered_Recycler_View;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,CartFragment.class,null).addToBackStack(null).commit();
//                NavDirections OrderFragmentToCartFragment = OrderFragment.actionOrderFragmentToCartFragment();
//                Navigation.findNavController(view).navigate(OrderFragmentToCartFragment);
            }
        });

        ordered_Recycler_View = view.findViewById(R.id.orderd_recycler_view);
        ordered_Recycler_View.setHasFixedSize(true);
        ordered_Recycler_View.setLayoutManager(new LinearLayoutManager(getContext()));

        OrderedProductData[] ordered_product_data = new OrderedProductData[]{
                new OrderedProductData(R.drawable.logoav,"Rolex Women's Watch","1234","2000","01","abc","10:00","12:00"),
                new OrderedProductData(R.drawable.logoav,"Acer Laptop","5678","45000","01","abc","12:00","01:00"),
                new OrderedProductData(R.drawable.logoav,"Classic Cheeseburger","9807","200","02","abc","03:00","05:00"),
                new OrderedProductData(R.drawable.logoav,"Yonex Badminton Kit","1256","5000","01","abc","04:00","05:00"),
        };

        OrderedProductAdapter ordered_product_adapter = new OrderedProductAdapter(ordered_product_data, getContext());
        ordered_Recycler_View.setAdapter(ordered_product_adapter);

//        orderRecyclerView.setHasFixedSize(true);
//        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}