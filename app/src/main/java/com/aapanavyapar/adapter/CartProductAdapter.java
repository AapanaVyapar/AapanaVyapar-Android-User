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



public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder>{
    CartProductData[] cartProductDataList;
    Context context;

    public CartProductAdapter(CartProductData[] cartProductDataList, Context context) {
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
        final CartProductData cartProductCard = cartProductDataList[position];
        holder.cartProductImage.setImageResource(cartProductCard.getCart_product_image());
        holder.cartProductName.setText(cartProductCard.getCart_product_name());
        holder.cartShopName.setText(cartProductCard.getCart_shop_name());
        holder.cartShopDesc.setText(cartProductCard.getCart_shop_description());

    }

    @Override
    public int getItemCount() {
        return cartProductDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartProductImage;
        TextView cartProductName;
        TextView cartShopName;
        TextView cartShopDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductImage = itemView.findViewById(R.id.cart_product_image);
            cartProductName = itemView.findViewById(R.id.cart_product_name);
            cartShopName = itemView.findViewById(R.id.cart_shop_name);
            cartShopDesc = itemView.findViewById(R.id.cart_shop_description);
        }
    }
}
