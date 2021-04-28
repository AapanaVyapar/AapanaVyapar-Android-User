package com.aapanavyapar.adapter;

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
import com.aapanavyapar.viewData.ProductData;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ProductData[] productDataList;
    Context context;

    public ProductAdapter(ProductData[] product_data, Context activity) {
        this.productDataList = product_data;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_data_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductData productCard = productDataList[position];
        holder.productImage.setImageResource(productCard.getProduct_image());
        holder.productName.setText(productCard.getProduct_name());
        holder.shopName.setText(productCard.getShop_name());
        holder.shopDesc.setText(productCard.getShop_description());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context , productCard.getProduct_name(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDataList.length;
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
