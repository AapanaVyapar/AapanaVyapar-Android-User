package com.aapanavyapar.serviceWrappers;

import android.util.Log;

import androidx.lifecycle.ViewModelStoreOwner;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.ViewProvider;
import com.aapanavyapar.aapanavyapar.services.Category;
import com.aapanavyapar.aapanavyapar.services.GetShopsBySearchRequest;
import com.aapanavyapar.aapanavyapar.services.GetShopsBySearchResponse;
import com.aapanavyapar.aapanavyapar.services.Location;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.viewData.ProductData;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GetShopsBySearchWrapper {

    public static final String DETECTION_CIRCLE = "1000";

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public GetShopsBySearchWrapper(ViewModelStoreOwner owner) {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

    }

    public void getShopsBySearch(String authToken, String refreshToken, String search, Location location, RecycleViewUpdater updater) {

        int state = 0;
        while(state == 0) {
            GetShopsBySearchRequest getShopRequest = GetShopsBySearchRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setSearch(search)
                    .setLocation(location)
                    .setDistanceInMeter(DETECTION_CIRCLE)
                    .build();

            try {
                Iterator<GetShopsBySearchResponse> getShopsBySearchResponseIterator = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).getShopsBySearch(getShopRequest);
                state = 1;
                mChannel.shutdown();

                while (getShopsBySearchResponseIterator.hasNext()) {
                    GetShopsBySearchResponse response = getShopsBySearchResponseIterator.next();

                    ProductData shopData = new ProductData(
                            null,
                            null,
                            response.getShops().getShopId(),
                            response.getShops().getShopName(),
                            null,
                            response.getShops().getPrimaryImage(),
                            null,
                            response.getShops().getCategoryList().toArray(new Category[0]),
                            0,
                            response.getShops().getRating(),
                            response.getShops().getShopkeeper(),
                            response.getShops().getLocation().getLatitude(),
                            response.getShops().getLocation().getLongitude()
                    );
                    Log.d(ViewProvider.TAG, shopData.toString());
                    updater.updateRecycleView(shopData);
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
