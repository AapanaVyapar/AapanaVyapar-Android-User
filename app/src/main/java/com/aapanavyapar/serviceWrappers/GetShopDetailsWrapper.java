package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.GetShopRequest;
import com.aapanavyapar.aapanavyapar.services.GetShopResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GetShopDetailsWrapper {
    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    GetShopResponse shopResponse = null;

    public GetShopDetailsWrapper() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);
    }

    public void updateProfile(String authToken, String refreshToken, String shopId) {
        int state = 0;
        while(state == 0) {
            GetShopRequest getShopRequest = GetShopRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setShopId(shopId)
                    .build();

            try {
                GetShopResponse getShopResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).getShop(getShopRequest);
                state = 1;
                mChannel.shutdown();
                this.shopResponse = getShopResponse;

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

    public GetShopResponse getShopResponse() {
        return this.shopResponse;
    }

}
