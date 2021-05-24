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
import com.aapanavyapar.viewData.OrderedProductData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.ViewHolder> {
    ArrayList<OrderedProductData> orderedProductDataList;
    Context context;

    public OrderedProductAdapter(ArrayList<OrderedProductData> orderedProductDataList, Context context) {
        this.orderedProductDataList = orderedProductDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderedProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_data,parent,false);
        OrderedProductAdapter.ViewHolder viewHolder = new OrderedProductAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedProductAdapter.ViewHolder holder, int position) {
        final OrderedProductData orderedProductCard = orderedProductDataList.get(position);

        Glide.with(this.context)
                .load(orderedProductCard.getOrderedProductImage())
                .centerCrop()
                .fitCenter()
                .into(holder.orderedProductImage);

        holder.orderedProductName.setText(orderedProductCard.getOrderedProductName());
        holder.orderedProductId.setText(orderedProductCard.getOrderedProductId());
        holder.orderedProductPrice.setText(orderedProductCard.getOrderedProductPrice());
        holder.orderedProductQty.setText(orderedProductCard.getOrderedProductQty());
        holder.orderedProductStatus.setText(orderedProductCard.getOrderedProductStatus());
        holder.orderedTime.setText(orderedProductCard.getOrderedTime());
        holder.orderedDeliveredTime.setText(orderedProductCard.getOrderedDeliveredTime());

    }

    @Override
    public int getItemCount() {
        return orderedProductDataList.size();
    }

    public void notifyData(ArrayList<OrderedProductData> myList) {
        this.orderedProductDataList = myList;
        notifyDataSetChanged();
    }

    public void addNewData(OrderedProductData data) {
        this.orderedProductDataList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView orderedProductImage;
        TextView orderedProductName;
        TextView orderedProductId;
        TextView orderedProductPrice;
        TextView orderedProductQty;
        TextView orderedProductStatus;
        TextView orderedTime;
        TextView orderedDeliveredTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderedProductImage = itemView.findViewById(R.id.order_product_imageview);
            orderedProductName = itemView.findViewById(R.id.order_product_name);
            orderedProductId = itemView.findViewById(R.id.order_id);
            orderedProductPrice = itemView.findViewById(R.id.order_product_price);
            orderedProductQty = itemView.findViewById(R.id.order_product_qty);
            orderedProductStatus = itemView.findViewById(R.id.order_status);
            orderedTime = itemView.findViewById(R.id.order_on);
            orderedDeliveredTime = itemView.findViewById(R.id.order_delivered_on);
        }
    }
}
