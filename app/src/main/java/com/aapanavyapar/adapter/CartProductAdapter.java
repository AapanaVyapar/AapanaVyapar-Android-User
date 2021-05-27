package com.aapanavyapar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.aapanavyapar.R;
import com.aapanavyapar.viewData.CartProductData;
import com.aapanavyapar.viewData.OrderedProductData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder>{
    ArrayList<CartProductData> cartProductDataList;
    Context context;

    public CartProductAdapter(ArrayList<CartProductData> cartProductDataList, Context context) {
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
        final CartProductData cartProductCard = cartProductDataList.get(position);

        Glide.with(this.context)
                .load(cartProductCard.getCartProductImage())
                .centerCrop()
                .fitCenter()
                .into(holder.cartProductImage);

        holder.cartProductName.setText(cartProductCard.getCartProductName());
        holder.cartShopName.setText(cartProductCard.getCartShopName());

    }

    @Override
    public int getItemCount() {
        return cartProductDataList.size();
    }

    public void notifyData(ArrayList<CartProductData> myList) {
        this.cartProductDataList = myList;
        notifyDataSetChanged();
    }

    public void addNewData(CartProductData data) {
        this.cartProductDataList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartProductImage;
        TextView cartProductName;
        TextView cartShopName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductImage = itemView.findViewById(R.id.cartProductImage);
            cartProductName = itemView.findViewById(R.id.cartProductName);
            cartShopName = itemView.findViewById(R.id.cartShopName);
        }
    }
}
