package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.RateShopRequest;
import com.aapanavyapar.aapanavyapar.services.RateShopResponse;
import com.aapanavyapar.aapanavyapar.services.Ratings;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class AddRatingWrapper {
    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public AddRatingWrapper() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

    }

    public void addRating(String authToken, String refreshToken, String shopId, String comment, int rating) {

        int state = 0;
        while(state == 0) {
            RateShopRequest rateShopRequest = RateShopRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setComment(comment)
                    .setRatings(Ratings.forNumber(rating))
                    .setShopId(shopId)
                    .build();

            try {
                RateShopResponse rateShopResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).rateShop(rateShopRequest);
                state = 1;
                mChannel.shutdown();

            } catch (StatusRuntimeException e) {
                e.printStackTrace();
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
