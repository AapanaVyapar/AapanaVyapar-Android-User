package com.aapanavyapar.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.aapanavyapar.ProductOnCardClick;
import com.aapanavyapar.aapanavyapar.R;
import com.aapanavyapar.aapanavyapar.TrendingFragment;
import com.aapanavyapar.viewData.ProductData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ArrayList<ProductData> productDataList;
    Context context;

    public ProductAdapter(ArrayList<ProductData> productData, Context activity) {
        this.productDataList = productData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_data_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductData productCardData = productDataList.get(position);

        Glide.with(this.context)
                .load(productCardData.getProductImage())
                .centerCrop()
                .fitCenter()
                .into(holder.productImage);

        holder.productName.setText(productCardData.getProductName());
        holder.shopName.setText(productCardData.getShopName());
        holder.productLikes.setText(String.valueOf(productCardData.getProductLikes()));
        holder.ratingBar.setRating((float) productCardData.getShopRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrendingFragment.caller.interrupt();
                Toast.makeText(context , productCardData.getProductName(),Toast.LENGTH_LONG).show();
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                Bundle args = new Bundle();
                args.putSerializable("dataFill", productCardData);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,ProductOnCardClick.class,args).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    public void notifyData(ArrayList<ProductData> myList) {
        this.productDataList = myList;
        notifyDataSetChanged();
    }

    public void makeEmpty() {
        this.productDataList.clear();
        notifyDataSetChanged();
    }

    public void addNewData(ProductData data) {
        this.productDataList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName;
        TextView productLikes;
        TextView shopName;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productLikes = itemView.findViewById(R.id.product_likes);
            shopName = itemView.findViewById(R.id.shopName);
            ratingBar = itemView.findViewById(R.id.shop_rating_bar);
        }
    }
}
