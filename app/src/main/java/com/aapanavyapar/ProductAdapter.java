package com.aapanavyapar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.aapanavyapar.R;
import com.aapanavyapar.aapanavyapar.ViewProvider;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    ProductData[] product_data;
    Context context;

    public ProductAdapter(ProductData[] product_data, ViewProvider activity) {
        this.product_data = product_data;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.trending_product_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductData product_data_list = product_data[position];
        holder.productImage.setImageResource(product_data_list.getProduct_image());
        holder.productName.setText(product_data_list.getProduct_name());
        holder.shopName.setText(product_data_list.getShop_name());
        holder.shopDesc.setText(product_data_list.getShop_description());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context , product_data_list.getProduct_name(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return product_data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName;
        TextView shopName;
        TextView shopDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            shopName = itemView.findViewById(R.id.shop_name);
            shopDesc = itemView.findViewById(R.id.shop_description);
        }
    }




}
