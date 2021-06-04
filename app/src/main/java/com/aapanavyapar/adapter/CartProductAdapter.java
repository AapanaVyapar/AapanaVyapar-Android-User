package com.aapanavyapar.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.aapanavyapar.ProductOnCardClick;
import com.aapanavyapar.aapanavyapar.ProductSearchFragment;
import com.aapanavyapar.aapanavyapar.R;
import com.aapanavyapar.aapanavyapar.TrendingFragment;
import com.aapanavyapar.viewData.ProductData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder>{
    ArrayList<ProductData> cartProductDataList;
    Context context;

    public CartProductAdapter(ArrayList<ProductData> cartProductDataList, Context context) {
        this.cartProductDataList = cartProductDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_data,parent,false);
        CartProductAdapter.ViewHolder viewHolder = new CartProductAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductData cartProductCard = cartProductDataList.get(position);

        Glide.with(this.context)
                .load(cartProductCard.getProductImage())
                .centerCrop()
                .fitCenter()
                .into(holder.cartProductImage);

        holder.cartProductName.setText(cartProductCard.getProductName());

        holder.itemView.setOnClickListener(v -> {
            if(TrendingFragment.caller != null)
                TrendingFragment.caller.interrupt();
            if(ProductSearchFragment.caller != null)
                ProductSearchFragment.caller.interrupt();

            AppCompatActivity activity = (AppCompatActivity)v.getContext();
            Bundle args = new Bundle();
            args.putSerializable("dataFill", cartProductCard);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ProductOnCardClick.class,args).addToBackStack(null).commit();
        });

    }

    @Override
    public int getItemCount() {
        return cartProductDataList.size();
    }

    public void notifyData(ArrayList<ProductData> myList) {
        this.cartProductDataList = myList;
        notifyDataSetChanged();
    }

    public void addNewData(ProductData data) {
        this.cartProductDataList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartProductImage;
        TextView cartProductName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductImage = itemView.findViewById(R.id.cartProductImage);
            cartProductName = itemView.findViewById(R.id.cartProductName);
        }
    }
}
