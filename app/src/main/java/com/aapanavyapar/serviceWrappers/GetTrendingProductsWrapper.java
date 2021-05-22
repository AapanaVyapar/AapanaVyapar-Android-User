package com.aapanavyapar.serviceWrappers;

import android.util.Log;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.Category;
import com.aapanavyapar.aapanavyapar.services.GetTrendingProductsByShopRequest;
import com.aapanavyapar.aapanavyapar.services.GetTrendingProductsByShopResponse;
import com.aapanavyapar.aapanavyapar.services.GetTrendingShopsRequest;
import com.aapanavyapar.aapanavyapar.services.GetTrendingShopsResponse;
import com.aapanavyapar.aapanavyapar.services.Location;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.viewData.ProductData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GetTrendingProductsWrapper {

    public static final String DETECTION_CIRCLE = "100";

    ManagedChannel mShopChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStubForShop;

    ManagedChannel mProductChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStubForProduct;

    Map<String, GetTrendingShopsResponse> trendingShopsMap = new HashMap<String, GetTrendingShopsResponse>();

    public GetTrendingProductsWrapper() {
        mShopChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStubForShop = ViewProviderServiceGrpc.newBlockingStub(mShopChannel);

        mProductChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStubForProduct = ViewProviderServiceGrpc.newBlockingStub(mProductChannel);

    }

    public boolean GetTrendingProducts(String authToken, android.location.Location location, RecycleViewUpdater updater) {
        GetTrendingShopsRequest trendingShopsRequest = GetTrendingShopsRequest.newBuilder()
                .setApiKey(MainActivity.API_KEY)
                .setToken(authToken)
                .setLocation(Location.newBuilder()
                        .setLongitude(String.valueOf(location.getLongitude()))
                        .setLatitude(String.valueOf(location.getLatitude()))
                        .build())
                .setDistanceInMeter("1000")
                .build();

        Log.d("GetTrendingProducts", "Creating Shop Data Request");
        try {
            Iterator<GetTrendingShopsResponse> trendingShopsResponse = blockingStubForShop.withDeadlineAfter(5, TimeUnit.MINUTES).getTrendingShops(trendingShopsRequest);
            while (trendingShopsResponse.hasNext()){
                GetTrendingShopsResponse shopsResponse = trendingShopsResponse.next();
                trendingShopsMap.put(shopsResponse.getShops().getShopId(), shopsResponse);
                Log.d("GetTrendingProducts", "Getting Shop Data");
            }
            mShopChannel.shutdown();
            Log.d("GetTrendingProducts", "Done with Shop Data");

//            List<String> l = new ArrayList<String>(map.keySet());

            GetTrendingProductsByShopRequest getTrendingProductsByShopRequest = GetTrendingProductsByShopRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .addAllShopId(trendingShopsMap.keySet())
                    .build();

            Log.d("GetTrendingProducts", "Creating Products By Shop Data Request");
            Iterator<GetTrendingProductsByShopResponse> getTrendingProductsByShopResponse = blockingStubForProduct.withDeadlineAfter(5, TimeUnit.MINUTES).getTrendingProductsByShop(getTrendingProductsByShopRequest);
            while (getTrendingProductsByShopResponse.hasNext()) {
                GetTrendingProductsByShopResponse productsByShopResponse = getTrendingProductsByShopResponse.next();

                GetTrendingShopsResponse shopFromMap = trendingShopsMap.get(productsByShopResponse.getCategoryData().getShopId());

                ProductData productData = null;
                if (shopFromMap != null) {
                    productData = new ProductData(
                            productsByShopResponse.getCategoryData().getProductId(),
                            productsByShopResponse.getCategoryData().getProductName(),
                            productsByShopResponse.getCategoryData().getShopId(),
                            shopFromMap.getShops().getShopName(),
                            productsByShopResponse.getCategoryData().getPrimaryImage(),
                            shopFromMap.getShops().getPrimaryImage(),
                            productsByShopResponse.getCategoryData().getCategoryList().toArray(new Category[0]),
                            shopFromMap.getShops().getCategoryList().toArray(new Category[0]),
                            productsByShopResponse.getCategoryData().getLikes(),
                            shopFromMap.getShops().getRating(),
                            shopFromMap.getShops().getShopkeeper(),
                            shopFromMap.getShops().getLocation()
                    );
                }
                Log.d("GetTrendingProducts", "Packed Product Data");
                if(productData != null){
                    updater.updateRecycleView(productData);
                }
                Log.d("GetTrendingProducts", "Sending Product Data");
            }
            mProductChannel.shutdown();
            return true;

        } catch (StatusRuntimeException e) {
            e.printStackTrace();
            mShopChannel.shutdownNow();
            mProductChannel.shutdownNow();
            return false;
        }
    }
}
