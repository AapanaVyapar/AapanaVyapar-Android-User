package com.aapanavyapar.aapanavyapar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aapanavyapar.aapanavyapar.services.Location;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.dataModel.ViewDataModel;
import com.aapanavyapar.serviceWrappers.AddToCartWrapper;
import com.aapanavyapar.serviceWrappers.AddToFavWrapper;
import com.aapanavyapar.serviceWrappers.GetProductInfo;
import com.aapanavyapar.serviceWrappers.RemoveFromCartWrapper;
import com.aapanavyapar.serviceWrappers.RemoveFromFavWrapper;
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
    TextView offer;
    CheckBox bookmark, addToFavourite;

    DataModel dataModel;
    ViewDataModel viewDataModel;

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

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);
        viewDataModel = new ViewModelProvider(requireActivity()).get(ViewDataModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_on_card_click, container, false);

    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        assert getArguments() != null;
        ProfileDB g = new ProfileDB(getContext());
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
        offer = view.findViewById(R.id.productoncardclick_offer);

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
        shippingInfo.setText(getProductInfo.getResponse().getShippingInfo());
        description.setText(getProductInfo.getResponse().getProductDescription());
        offer.setText(String.valueOf(getProductInfo.getResponse().getOffer()) + " %");

        addToFavourite.setChecked(viewDataModel.IsProductInLikeList(productData.getProductId()));
        bookmark.setChecked(viewDataModel.IsProductInCartList(productData.getProductId()));

        addToFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    boolean res = new AddToFavWrapper().addToFav(dataModel.getAuthToken(), dataModel.getRefreshToken(), productData.getProductId());
                    if(res)
                        viewDataModel.addToLike(getContext(), productData.getProductId());
                } else {
                    boolean res = new RemoveFromFavWrapper().removeFromFav(dataModel.getAuthToken(), dataModel.getRefreshToken(), productData.getProductId());
                    if(res)
                        viewDataModel.DeleteFromLikeList(getContext(),productData.getProductId());
                }
            }
        });

        bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    boolean res = new AddToCartWrapper().addToCart(dataModel.getAuthToken(), dataModel.getRefreshToken(), productData.getProductId());
                    Toast.makeText(getContext(), "Result : " + res, Toast.LENGTH_LONG).show();
                    viewDataModel.addToCart(getContext(), productData.getProductId());
                } else {
                    boolean res = new RemoveFromCartWrapper().removeFromCart(dataModel.getAuthToken(), dataModel.getRefreshToken(), productData.getProductId());
                    Toast.makeText(getContext(), "Result : " + res, Toast.LENGTH_LONG).show();
                    viewDataModel.DeleteFromCartList(getContext(),productData.getProductId());
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
}