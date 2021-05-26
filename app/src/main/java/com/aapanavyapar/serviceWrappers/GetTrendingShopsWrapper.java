package com.aapanavyapar.serviceWrappers;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.GetTrendingShopsRequest;
import com.aapanavyapar.aapanavyapar.services.GetTrendingShopsResponse;
import com.aapanavyapar.aapanavyapar.services.Location;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.dataModel.ViewDataModel;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GetTrendingShopsWrapper {

    public static final String DETECTION_CIRCLE = "1000";

    ManagedChannel mShopChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStubForShop;

    private ViewDataModel viewDataModel;

    public GetTrendingShopsWrapper(ViewModelStoreOwner owner) {
        mShopChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStubForShop = ViewProviderServiceGrpc.newBlockingStub(mShopChannel);
        viewDataModel = new ViewModelProvider(owner).get(ViewDataModel.class);
    }

    public void GetTrendingShops(String authToken, String refreshToken, android.location.Location location) {
        int state = 0;
        while(state == 0) {
            GetTrendingShopsRequest trendingShopsRequest = GetTrendingShopsRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setLocation(Location.newBuilder()
                            .setLongitude(String.valueOf(location.getLongitude()))
                            .setLatitude(String.valueOf(location.getLatitude()))
                            .build())
                    .setDistanceInMeter(DETECTION_CIRCLE)
                    .build();

            Log.d("GetTrendingProducts", "Creating Shop Data Request");
            try {
                Iterator<GetTrendingShopsResponse> trendingShopsResponse = blockingStubForShop.withDeadlineAfter(5, TimeUnit.MINUTES).getTrendingShops(trendingShopsRequest);
                while (trendingShopsResponse.hasNext()) {
                    GetTrendingShopsResponse shopsResponse = trendingShopsResponse.next();
                    viewDataModel.addShopToMap(shopsResponse);
                    Log.d("GetTrendingProducts", "Getting Shop Data");
                }
                state = 1;
                mShopChannel.shutdown();
                Log.d("GetTrendingProducts", "Done with Shop Data");

            } catch (StatusRuntimeException e) {
                e.printStackTrace();
                if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                    UpdateToken updateToken = new UpdateToken();
                    if(updateToken.GetUpdatedToken(refreshToken)) {
                        authToken = updateToken.getAuthToken();
                        state = 0;
                    }else {
                        state = 1;
                        mShopChannel.shutdownNow();
                    }
                } else {
                    state = 1;
                    mShopChannel.shutdownNow();
                }
            }
        }
    }
}
