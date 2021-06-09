package com.aapanavyapar.serviceWrappers;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.Category;
import com.aapanavyapar.aapanavyapar.services.GetProductsBySearchRequest;
import com.aapanavyapar.aapanavyapar.services.GetProductsBySearchResponse;
import com.aapanavyapar.aapanavyapar.services.GetTrendingShopsResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.dataModel.ViewDataModel;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.viewData.ProductData;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GetProductsBySearchWrapper {
    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    private ViewDataModel viewDataModel;

    public GetProductsBySearchWrapper(ViewModelStoreOwner owner) {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

        viewDataModel = new ViewModelProvider(owner).get(ViewDataModel.class);
    }

    public void getProductBySearch(String authToken, String refreshToken, String search, RecycleViewUpdater updater) {

        if (viewDataModel.getTrendingShopsMap() == null || viewDataModel.getTrendingShopsMap().size() == 0){
            mChannel.shutdownNow();
            return;
        }

        int state = 0;
        while(state == 0) {
            GetProductsBySearchRequest getProductRequest = GetProductsBySearchRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setSearch(search)
                    .addAllShopIds(viewDataModel.getTrendingShopsMap().keySet())
                    .build();

            try {
                Iterator<GetProductsBySearchResponse> getProductResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).getProductsBySearch(getProductRequest);
                state = 1;
                mChannel.shutdown();

                while (getProductResponse.hasNext()) {
                    GetProductsBySearchResponse response = getProductResponse.next();

                    GetTrendingShopsResponse trendingShopsResponse = viewDataModel.getTrendingShopsMap().get(response.getProducts().getShopId());

                    ProductData productData = new ProductData(
                            response.getProducts().getProductId(),
                            response.getProducts().getProductName(),
                            response.getProducts().getShopId(),
                            trendingShopsResponse.getShops().getShopName(),
                            response.getProducts().getPrimaryImage(),
                            response.getProducts().getPrimaryImage(),
                            response.getProducts().getCategoryList().toArray(new Category[0]),
                            response.getProducts().getCategoryList().toArray(new Category[0]),
                            response.getProducts().getLikes(),
                            trendingShopsResponse.getShops().getRating(),
                            trendingShopsResponse.getShops().getShopkeeper(),
                            trendingShopsResponse.getShops().getLocation().getLatitude(),
                            trendingShopsResponse.getShops().getLocation().getLongitude()
                    );
                    updater.updateRecycleView(productData);
                }

            } catch (StatusRuntimeException e) {
                if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                    UpdateToken updateToken = new UpdateToken();
                    if(updateToken.GetUpdatedToken(refreshToken)) {
                        authToken = updateToken.getAuthToken();
                        state = 0;
                    }else {
                        state = 1;
                        mChannel.shutdown();
                    }
                } else {
                    state = 1;
                    mChannel.shutdown();
                }
            }
        }
    }

}
