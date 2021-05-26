package com.aapanavyapar.serviceWrappers;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.Category;
import com.aapanavyapar.aapanavyapar.services.GetTrendingProductsByShopRequest;
import com.aapanavyapar.aapanavyapar.services.GetTrendingProductsByShopResponse;
import com.aapanavyapar.aapanavyapar.services.GetTrendingShopsRequest;
import com.aapanavyapar.aapanavyapar.services.GetTrendingShopsResponse;
import com.aapanavyapar.aapanavyapar.services.Location;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.dataModel.ViewDataModel;
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

    public static final String DETECTION_CIRCLE = "1000";

    ManagedChannel mProductChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStubForProduct;

    private ViewDataModel viewDataModel;

    public GetTrendingProductsWrapper(ViewModelStoreOwner owner) {
        mProductChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStubForProduct = ViewProviderServiceGrpc.newBlockingStub(mProductChannel);

        viewDataModel = new ViewModelProvider(owner).get(ViewDataModel.class);
    }

    public void GetTrendingProducts(String authToken, String refreshToken, RecycleViewUpdater updater) {
        int state = 0;
        while(state == 0) {
            Log.d("GetTrendingProducts", "Done with Shop Data");

            GetTrendingProductsByShopRequest getTrendingProductsByShopRequest = GetTrendingProductsByShopRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .addAllShopId(viewDataModel.getTrendingShopsMap().keySet())
                    .build();

            try {
                Log.d("GetTrendingProducts", "Creating Products By Shop Data Request");
                Iterator<GetTrendingProductsByShopResponse> getTrendingProductsByShopResponse = blockingStubForProduct.withDeadlineAfter(5, TimeUnit.MINUTES).getTrendingProductsByShop(getTrendingProductsByShopRequest);
                while (getTrendingProductsByShopResponse.hasNext()) {
                    GetTrendingProductsByShopResponse productsByShopResponse = getTrendingProductsByShopResponse.next();

                    GetTrendingShopsResponse shopFromMap = viewDataModel.getTrendingShopsMap().get(productsByShopResponse.getCategoryData().getShopId());

                    ProductData productData = null;
                    if (viewDataModel.getTrendingShopsMap() != null) {
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
                                shopFromMap.getShops().getLocation().getLatitude(),
                                shopFromMap.getShops().getLocation().getLongitude()
                        );
                    }
                    Log.d("GetTrendingProducts", "Packed Product Data");
                    if (productData != null) {
                        updater.updateRecycleView(productData);
                    }
                    Log.d("GetTrendingProducts", "Sending Product Data");
                }
                state = 1;
                mProductChannel.shutdown();
            } catch (StatusRuntimeException e) {
                e.printStackTrace();
                if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                    UpdateToken updateToken = new UpdateToken();
                    if(updateToken.GetUpdatedToken(refreshToken)) {
                        authToken = updateToken.getAuthToken();
                        state = 0;
                    }else {
                        state = 1;
                        mProductChannel.shutdownNow();
                    }
                } else {
                    state = 1;
                    mProductChannel.shutdownNow();
                }
            }
        }
    }
}
