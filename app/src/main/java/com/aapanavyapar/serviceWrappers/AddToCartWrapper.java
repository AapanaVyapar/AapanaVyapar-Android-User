package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.AddToCartProductRequest;
import com.aapanavyapar.aapanavyapar.services.AddToCartProductResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class AddToCartWrapper {

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public AddToCartWrapper() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

    }

    public boolean addToCart(String authToken, String refreshToken, String productId) {

        while (true) {
            AddToCartProductRequest addToCartProductRequest = AddToCartProductRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setProductId(productId)
                    .build();

            try {
                AddToCartProductResponse addToCartProductResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).addToCartProduct(addToCartProductRequest);
                mChannel.shutdown();
                return addToCartProductResponse.getStatus();

            } catch (StatusRuntimeException e) {
                if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                    UpdateToken updateToken = new UpdateToken();
                    if (updateToken.GetUpdatedToken(refreshToken)) {
                        authToken = updateToken.getAuthToken();

                    } else {
                        mChannel.shutdown();
                        return false;

                    }

                } else {
                    mChannel.shutdown();
                    return false;

                }
            }
        }
    }
}