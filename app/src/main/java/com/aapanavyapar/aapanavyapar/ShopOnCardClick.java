package com.aapanavyapar.aapanavyapar;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.aapanavyapar.aapanavyapar.services.GetShopResponse;
import com.aapanavyapar.aapanavyapar.services.OperationalHours;
import com.aapanavyapar.aapanavyapar.services.RatingOfShop;
import com.aapanavyapar.aapanavyapar.services.Ratings;
import com.aapanavyapar.adapter.CommentAdapter;
import com.aapanavyapar.adapter.ProductAdapter;
import com.aapanavyapar.dataModel.DataModel;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.serviceWrappers.AddRatingWrapper;
import com.aapanavyapar.serviceWrappers.GetProductByShopWrapper;
import com.aapanavyapar.serviceWrappers.GetShopDetailsWrapper;
import com.aapanavyapar.viewData.ProductData;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class ShopOnCardClick extends Fragment {

    int normal = 16, selected = 20;
    private GoogleMap googleMap;

    OperationalHours operationalHours;
    public static Thread callerProduct = null;

    ProductAdapter shopProductAdapter;
    CommentAdapter shopCommentAdapter;
    RecyclerView recyclerViewForShop, recyclerViewForComment;

    ImageView shopImage;
    TextView shopName, shopKeeperName, businessInformation;
    RatingBar ratingBar;

    DataModel dataModel;
    ProductData productData;

    EditText ratingCommentBox;
    ImageButton ratingSendComment;
    RatingBar ratingRatingBar;

    TextView day1, day2, day3, day4, day5, day6, day7;
    TextView time;


    public ShopOnCardClick() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        assert getArguments() != null;
        productData = (ProductData) getArguments().getSerializable("dataFill");

        View rootView = inflater.inflate(R.layout.fragment_search_shop_on_card_click, container, false);

        dataModel = new ViewModelProvider(requireActivity()).get(DataModel.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapView mapView = (MapView) rootView.findViewById(R.id.search_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(callback);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

        shopImage = view.findViewById(R.id.searched_shop_image);
        shopName = view.findViewById(R.id.search_shop_name);
        shopKeeperName = view.findViewById(R.id.shopkeeper_name);
        businessInformation = view.findViewById(R.id.business_information_Description);
        ratingBar = view.findViewById(R.id.sreach_shop_ratings);

        ratingCommentBox = view.findViewById(R.id.commentBox);
        ratingRatingBar = view.findViewById(R.id.commentRating);
        ratingSendComment = view.findViewById(R.id.commentButton);

        day1 = view.findViewById(R.id.sun);
        day2 = view.findViewById(R.id.mon);
        day3 = view.findViewById(R.id.tue);
        day4 = view.findViewById(R.id.wed);
        day5 = view.findViewById(R.id.thu);
        day6 = view.findViewById(R.id.fri);
        day7 = view.findViewById(R.id.sat);
        time = view.findViewById(R.id.time);

        GetShopDetailsWrapper detailsWrapper = new GetShopDetailsWrapper();
        detailsWrapper.getShopDetails(dataModel.getAuthToken(), dataModel.getRefreshToken(), productData.getShopId());
        GetShopResponse shopResponse = detailsWrapper.getShopResponse();
        if(shopResponse == null){
            return;
        }

        Glide.with(getContext())
                .load(shopResponse.getPrimaryImage())
                .centerCrop()
                .fitCenter()
                .into(shopImage);

        shopName.setText(shopResponse.getShopName());
        shopKeeperName.setText(shopResponse.getShopKeeperName());
        businessInformation.setText(shopResponse.getBusinessInformation());
        ratingBar.setRating(shopResponse.getTotalRating());

        operationalHours = shopResponse.getOperationalHours();
        time.setText(operationalHours.getSunday(0) + " " + operationalHours.getSunday(1));
        day1.setTextSize(selected);
        day1.setTypeface(day1.getTypeface(), Typeface.BOLD);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView selectedDay = (TextView)v;
                String day = selectedDay.getText().toString();

                day1.setTypeface(day1.getTypeface(), Typeface.NORMAL);
                day2.setTypeface(day2.getTypeface(), Typeface.NORMAL);
                day3.setTypeface(day3.getTypeface(), Typeface.NORMAL);
                day4.setTypeface(day4.getTypeface(), Typeface.NORMAL);
                day5.setTypeface(day5.getTypeface(), Typeface.NORMAL);
                day6.setTypeface(day6.getTypeface(), Typeface.NORMAL);
                day7.setTypeface(day7.getTypeface(), Typeface.NORMAL);

                day1.setTextSize(normal);
                day2.setTextSize(normal);
                day3.setTextSize(normal);
                day4.setTextSize(normal);
                day5.setTextSize(normal);
                day6.setTextSize(normal);
                day7.setTextSize(normal);

                selectedDay.setTypeface(selectedDay.getTypeface(), Typeface.BOLD);
                selectedDay.setTextSize(selected);

                Toast.makeText(getContext(), "Selected : " + day, Toast.LENGTH_SHORT).show();

                switch(day){
                    case "Sun":
                        time.setText(operationalHours.getSunday(0) + " " + operationalHours.getSunday(1));
                        break;
                    case "Mon":
                        time.setText(operationalHours.getMonday(0) + " " + operationalHours.getMonday(1));
                        break;
                    case "Tue":
                        time.setText(operationalHours.getTuesday(0) + " " + operationalHours.getTuesday(1));
                        break;
                    case "Wed":
                        time.setText(operationalHours.getWednesday(0) + " " + operationalHours.getWednesday(1));
                        break;
                    case "Thu":
                        time.setText(operationalHours.getThursday(0) + " " + operationalHours.getThursday(1));
                        break;
                    case "Fri":
                        time.setText(operationalHours.getFriday(0) + " " + operationalHours.getFriday(1));
                        break;
                    case "Sat":
                        time.setText(operationalHours.getSaturday(0) + " " + operationalHours.getSaturday(1));
                        break;
                }

            }
        };

        day1.setOnClickListener(onClickListener);
        day2.setOnClickListener(onClickListener);
        day3.setOnClickListener(onClickListener);
        day4.setOnClickListener(onClickListener);
        day5.setOnClickListener(onClickListener);
        day6.setOnClickListener(onClickListener);
        day7.setOnClickListener(onClickListener);


        recyclerViewForComment = view.findViewById(R.id.search_shop_comment_recyclerview);
        recyclerViewForComment.setHasFixedSize(true);
        recyclerViewForComment.setLayoutManager(new LinearLayoutManager(getContext()));

        shopCommentAdapter = new CommentAdapter(new ArrayList<>(shopResponse.getRatingsList()), getContext());
        recyclerViewForComment.setAdapter(shopCommentAdapter);


        recyclerViewForShop = view.findViewById(R.id.recyclerview_for_searchshop_products);
        recyclerViewForShop.setHasFixedSize(true);
        recyclerViewForShop.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<ProductData> productDataList = new ArrayList<>();
        shopProductAdapter = new ProductAdapter(productDataList, getContext());
        recyclerViewForShop.setAdapter(shopProductAdapter);

        callerProduct = new Thread(new Runnable() {
            @Override
            public void run() {
                if(ViewProvider.currentLocation != null) {

                    GetProductByShopWrapper getProductByShopWrapper = new GetProductByShopWrapper(requireActivity());
                    getProductByShopWrapper.GetTrendingProducts(dataModel.getAuthToken(), dataModel.getRefreshToken(), productData.getShopId(), new RecycleViewUpdater() {
                        @Override
                        public void updateRecycleView(Object object) {
                            shopProductAdapter.addNewData((ProductData) object);
                        }
                    });
                }
            }
        });

        ratingSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRatingWrapper ratingWrapper = new AddRatingWrapper();
                Toast.makeText(getContext(), "Rating :" + ratingRatingBar.getRating(), Toast.LENGTH_LONG).show();
                ratingWrapper.addRating(dataModel.getAuthToken(), dataModel.getRefreshToken(), productData.getShopId(), ratingCommentBox.getText().toString(), Math.round(ratingRatingBar.getRating()));
                ratingCommentBox.setText("");
            }
        });

        if(callerProduct != null) {
            callerProduct.start();
            try {
                callerProduct.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap gMap) {
            googleMap = gMap;
            Toast.makeText(getContext(), "MAP", Toast.LENGTH_LONG).show();

            LatLng point = new LatLng(Double.parseDouble(productData.getShopLatitude()), Double.parseDouble(productData.getShopLongitude()));
            googleMap.addMarker(new MarkerOptions().position(point).title(productData.getShopName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f));

        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if(callerProduct != null)
            callerProduct.interrupt();
    }
}