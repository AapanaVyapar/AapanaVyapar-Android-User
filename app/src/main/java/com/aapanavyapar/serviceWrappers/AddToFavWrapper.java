package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.AddToLikeProductRequest;
import com.aapanavyapar.aapanavyapar.services.AddToLikeProductResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class AddToFavWrapper {

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public AddToFavWrapper() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

    }

    public boolean addToFav(String authToken, String refreshToken, String productId) {

        while (true) {
            AddToLikeProductRequest addToLikeProductRequest = AddToLikeProductRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setProductId(productId)
                    .build();

            try {
                AddToLikeProductResponse addToLikeProductResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).addToLikeProduct(addToLikeProductRequest);
                mChannel.shutdown();
                return addToLikeProductResponse.getStatus();

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
