package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.GetCartRequest;
import com.aapanavyapar.aapanavyapar.services.GetCartResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.viewData.CartProductData;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GetCartWrapper {

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public GetCartWrapper() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

    }

    public void getCart(String authToken, String refreshToken, RecycleViewUpdater updater) {
        int state = 0;
        while (state == 0) {
            GetCartRequest getCartRequest = GetCartRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .build();

            try {
                Iterator<GetCartResponse> getCartResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).getCart(getCartRequest);
                while(getCartResponse.hasNext()) {
                    GetCartResponse response = getCartResponse.next();
                    CartProductData cartProductData = new CartProductData(
                            response.getProducts().getPrimaryImage(),
                            response.getProducts().getShopId(),
                            response.getProducts().getProductName(),
                            "",
                            response.getProducts().getLikes()
                    );
                    updater.updateRecycleView(cartProductData);
                }
                state = 1;
                mChannel.shutdown();

            } catch (StatusRuntimeException e) {
                if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                    UpdateToken updateToken = new UpdateToken();
                    if (updateToken.GetUpdatedToken(refreshToken)) {
                        authToken = updateToken.getAuthToken();
                        state = 0;

                    } else {
                        mChannel.shutdown();
                        state = 1;

                    }

                } else {
                    mChannel.shutdown();
                    state = 1;

                }
            }
        }
    }

}
