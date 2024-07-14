package com.aapanavyapar.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.aapanavyapar.ProductOnCardClick;
import com.aapanavyapar.aapanavyapar.ProductSearchFragment;
import com.aapanavyapar.aapanavyapar.R;
import com.aapanavyapar.aapanavyapar.TrendingFragment;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.dataModel.ViewDataModel;
import com.aapanavyapar.serviceWrappers.AddToCartWrapper;
import com.aapanavyapar.serviceWrappers.RemoveFromCartWrapper;
import com.aapanavyapar.viewData.ProductData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ArrayList<ProductData> productDataList;
    Context context;
    DataModel dataModel;
    ViewDataModel viewDataModel;

    public ProductAdapter(ArrayList<ProductData> productData, Context activity) {
        this.productDataList = productData;
        this.context = activity;
        dataModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(DataModel.class);
        viewDataModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ViewDataModel.class);
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
                .load(productCardData.getProductImage()).apply(new RequestOptions().override(1000, 1000)).centerCrop().into(holder.productImage);

        holder.productName.setText(productCardData.getProductName());
        holder.shopName.setText(productCardData.getShopName());
        holder.productLikes.setText(String.valueOf(productCardData.getProductLikes()));
        holder.ratingBar.setRating((float) productCardData.getShopRating());

        holder.addToCart.setChecked(viewDataModel.IsProductInCartList(productCardData.getProductId()));

        holder.addToCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    boolean res = new AddToCartWrapper().addToCart(dataModel.getAuthToken(), dataModel.getRefreshToken(), productCardData.getProductId());
                    if(res) {
                        viewDataModel.addToCart(context, productCardData.getProductId());
                        holder.addToCart.setChecked(viewDataModel.IsProductInCartList(productCardData.getProductId()));
                    }
                } else {
                    boolean res = new RemoveFromCartWrapper().removeFromCart(dataModel.getAuthToken(), dataModel.getRefreshToken(), productCardData.getProductId());
                    if(res) {
                        viewDataModel.DeleteFromCartList(context, productCardData.getProductId());
                        holder.addToCart.setChecked(viewDataModel.IsProductInCartList(productCardData.getProductId()));
                    }
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if(TrendingFragment.caller != null)
                TrendingFragment.caller.interrupt();
            if(ProductSearchFragment.caller != null)
                ProductSearchFragment.caller.interrupt();

            Toast.makeText(context , productCardData.getProductName(),Toast.LENGTH_LONG).show();
            AppCompatActivity activity = (AppCompatActivity)v.getContext();
            Bundle args = new Bundle();
            args.putSerializable("dataFill", productCardData);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,ProductOnCardClick.class,args).addToBackStack(null).commit();
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
        CheckBox addToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productLikes = itemView.findViewById(R.id.product_likes);
            shopName = itemView.findViewById(R.id.shopName);
            ratingBar = itemView.findViewById(R.id.shop_rating_bar);
            addToCart = itemView.findViewById(R.id.add_to_cart);
        }
    }
}
