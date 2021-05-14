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


public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.ViewHolder> {
    OrderedProductData[] orderedProductDataList;
    Context context;

    public OrderedProductAdapter(OrderedProductData[] orderedProductDataList, Context context) {
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
        final OrderedProductData orderedProductCard = orderedProductDataList[position];
        holder.ordered_Product_Image.setImageResource(orderedProductCard.getOrderedProductImage());
        holder.ordered_Product_Name.setText(orderedProductCard.getOrderedProductName());
        holder.ordered_Product_Id.setText(orderedProductCard.getOrderedProductId());
        holder.ordered_Product_Price.setText(orderedProductCard.getOrderedProductPrice());
        holder.ordered_Product_Qty.setText(orderedProductCard.getOrderedProductQty());
        holder.ordered_Product_Status.setText(orderedProductCard.getOrderedProductStatus());
        holder.ordered_Time.setText(orderedProductCard.getOrderedTime());
        holder.ordered_Delivered_Time.setText(orderedProductCard.getOrderedDeliveredTime());

    }

    @Override
    public int getItemCount() {
        return orderedProductDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ordered_Product_Image;
        TextView ordered_Product_Name;
        TextView ordered_Product_Id;
        TextView ordered_Product_Price;
        TextView ordered_Product_Qty;
        TextView ordered_Product_Status;
        TextView ordered_Time;
        TextView ordered_Delivered_Time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ordered_Product_Image = itemView.findViewById(R.id.order_product_imageview);
            ordered_Product_Name = itemView.findViewById(R.id.order_product_name);
            ordered_Product_Id = itemView.findViewById(R.id.order_id);
            ordered_Product_Price = itemView.findViewById(R.id.order_product_price);
            ordered_Product_Qty = itemView.findViewById(R.id.order_product_qty);
            ordered_Product_Status = itemView.findViewById(R.id.order_status);
            ordered_Time = itemView.findViewById(R.id.order_on);
            ordered_Delivered_Time = itemView.findViewById(R.id.order_delivered_on);
        }
    }
}
