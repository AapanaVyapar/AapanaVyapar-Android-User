package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.RemoveFromCartProductRequest;
import com.aapanavyapar.aapanavyapar.services.RemoveFromCartProductResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class RemoveFromCartWrapper {

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public RemoveFromCartWrapper() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

    }

    public boolean removeFromCart(String authToken, String refreshToken, String productId) {

        while (true) {
            RemoveFromCartProductRequest removeFromCartProductRequest = RemoveFromCartProductRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setProductId(productId)
                    .build();

            try {
                RemoveFromCartProductResponse removeFromCartProductResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).removeFromCartProduct(removeFromCartProductRequest);
                mChannel.shutdown();
                return removeFromCartProductResponse.getStatus();

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
