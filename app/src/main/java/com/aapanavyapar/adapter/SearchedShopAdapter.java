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
import com.aapanavyapar.aapanavyapar.ProductSearchFragment;
import com.aapanavyapar.aapanavyapar.R;
import com.aapanavyapar.viewData.ProductData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchedShopAdapter extends RecyclerView.Adapter<SearchedShopAdapter.ViewHolder> {

    ArrayList<ProductData> shopDataList;
    Context context;

    public SearchedShopAdapter(ArrayList<ProductData> shopData, Context activity) {
        this.shopDataList = shopData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shop_data_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductData shopCardData = shopDataList.get(position);

        Glide.with(this.context)
                .load(shopCardData.getProductImage()).centerCrop().fitCenter().into(holder.shopImage);


        holder.shopName.setText(shopCardData.getShopName());
        holder.shopKeeperName.setText(shopCardData.getShopName());
        holder.ratingBar.setRating((float) shopCardData.getShopRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductSearchFragment.caller.interrupt();
                Toast.makeText(context , shopCardData.getProductName(),Toast.LENGTH_LONG).show();
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                Bundle args = new Bundle();
                args.putSerializable("dataFill", shopCardData);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,ProductOnCardClick.class,args).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopDataList.size();
    }

    public void notifyData(ArrayList<ProductData> myList) {
        this.shopDataList = myList;
        notifyDataSetChanged();
    }

    public void addNewData(ProductData data) {
        this.shopDataList.add(data);
        notifyDataSetChanged();
    }

    public void makeEmpty() {
        this.shopDataList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView shopImage;
        TextView shopName;
        TextView shopKeeperName;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopImage = itemView.findViewById(R.id.shopImage);
            shopName = itemView.findViewById(R.id.shopName);
            shopKeeperName = itemView.findViewById(R.id.shop_keeper_name);
            ratingBar = itemView.findViewById(R.id.shop_rating_bar);
        }
    }
}
