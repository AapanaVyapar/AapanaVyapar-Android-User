package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.RemoveFromLikeProductRequest;
import com.aapanavyapar.aapanavyapar.services.RemoveFromLikeProductResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class RemoveFromFavWrapper {

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public RemoveFromFavWrapper() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

    }

    public boolean removeFromFav(String authToken, String refreshToken, String productId) {

        while (true) {
            RemoveFromLikeProductRequest removeFromCartProductRequest = RemoveFromLikeProductRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setProductId(productId)
                    .build();

            try {
                RemoveFromLikeProductResponse removeFromLikeProductResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).removeFromLikeProduct(removeFromCartProductRequest);
                mChannel.shutdown();
                return removeFromLikeProductResponse.getStatus();

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
