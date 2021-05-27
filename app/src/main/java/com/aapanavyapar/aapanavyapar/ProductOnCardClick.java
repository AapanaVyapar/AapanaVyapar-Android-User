package com.aapanavyapar.aapanavyapar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.serviceWrappers.GetProductInfo;
import com.aapanavyapar.serviceWrappers.UpdateToken;
import com.aapanavyapar.viewData.ProductData;
import com.bumptech.glide.Glide;


public class ProductOnCardClick extends Fragment {

    ProductData productData;
    Button buyNow;
    ImageView productImage;
    TextView productName;
    TextView price;
    TextView availableStock;
    TextView deliveryCharges;
    TextView shippingInfo;
    TextView description;
    DataModel dataModel;
    ImageView bookmark, addToFavourite;




    public ProductOnCardClick() {
        // Required empty public constructor
        super(R.layout.fragment_product_on_card_click);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);
        return inflater.inflate(R.layout.fragment_product_on_card_click, container, false);

    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        assert getArguments() != null;
        productData = (ProductData) getArguments().getSerializable("dataFill");

        productImage = view.findViewById(R.id.productoncardclick_image);
        productName = view.findViewById(R.id.productoncardclick_productname_text);
        price = view.findViewById(R.id.productoncardclick_price_amount_text);
        availableStock = view.findViewById(R.id.productoncardclick_stockno_text);
        deliveryCharges = view.findViewById(R.id.productoncardclick_deliveryamount_text);
        description = view.findViewById(R.id.productoncardclick_description_text);
        shippingInfo = view.findViewById(R.id.productoncardclick_shipping_info);
        bookmark = view.findViewById(R.id.productoncardclick_bookmark_image);
        addToFavourite = view.findViewById(R.id.productoncardclick_favourite_image);
        int res = 2;
        GetProductInfo getProductInfo = new GetProductInfo();

        while(res == 2){
            res =  getProductInfo.getInfo(dataModel.getAuthToken(),productData.getProductId(),productData.getShopId());
            if(res == 2){
                UpdateToken updateToken = new UpdateToken();
                if(updateToken.GetUpdatedToken(dataModel.getRefreshToken())) {
                    dataModel.setAuthToken(updateToken.getAuthToken());
                }
            }
            else if(res == 0){
                Toast.makeText(getContext(),"Failed To Update",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Glide.with(getContext())
                .load(getProductInfo.getResponse().getImages(0))
                .centerCrop()
                .fitCenter()
                .into(productImage);

        productName.setText(getProductInfo.getResponse().getProductName());
        price.setText(String.valueOf(getProductInfo.getResponse().getPrice()));
        availableStock.setText(String.valueOf(getProductInfo.getResponse().getStock()));
        //deliveryCharges.setText(getProductInfo.getResponse().get());
        shippingInfo.setText(getProductInfo.getResponse().getShippingInfo());
        description.setText(getProductInfo.getResponse().getProductDescription());

        addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavourite = readState();

                if (isFavourite) {
                    bookmark.setBackgroundResource(R.drawable.heart_empty);
                    isFavourite = false;
                    saveState(isFavourite);

                } else {
                    bookmark.setBackgroundResource(R.drawable.heart_filled);
                    isFavourite = true;
                    saveState(isFavourite);

                }
            }
        });
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFavourite = readState();

                if (isFavourite) {
                    bookmark.setBackgroundResource(R.drawable.bookmark_empty);
                    isFavourite = false;
                    saveState(isFavourite);

                } else {
                    bookmark.setBackgroundResource(R.drawable.bookmark_filled);
                    isFavourite = true;
                    saveState(isFavourite);

                }
            }
        });
        buyNow = view.findViewById(R.id.productoncardclick_button_buy);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                Bundle args = new Bundle();
                args.putSerializable("dataFill", productData);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, BuyingDetailsFragment.class, args).addToBackStack(null).commit();
            }
        });

    }
    private void saveState(boolean isFavourite) {
        SharedPreferences aSharedPreferences = this.getSharedPreferences("Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences.edit();
        aSharedPreferencesEdit.putBoolean("State", isFavourite);
        aSharedPreferencesEdit.commit();
    }

    private boolean readState() {
        SharedPreferences aSharedPreferences = this.getSharedPreferences("Favourite", Context.MODE_PRIVATE);
        return aSharedPreferences.getBoolean("State", true);
    }

}