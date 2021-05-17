package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.AuthenticationGrpc;
import com.aapanavyapar.aapanavyapar.services.NewTokenRequest;
import com.aapanavyapar.aapanavyapar.services.NewTokenResponse;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class UpdateToken {

    ManagedChannel mChannel;
    AuthenticationGrpc.AuthenticationBlockingStub blockingStub;

    String authToken = "";

    public UpdateToken(){
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.AUTH_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = AuthenticationGrpc.newBlockingStub(mChannel);
    }

    public boolean GetUpdatedToken(String refreshToken) {
        NewTokenRequest newTokenRequest = NewTokenRequest.newBuilder()
                .setApiKey(MainActivity.API_KEY)
                .setRefreshToken(refreshToken)
                .build();

        try {
            NewTokenResponse newTokenResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).getNewToken(newTokenRequest);
            this.authToken = newTokenResponse.getToken();

        } catch (StatusRuntimeException e) {
            mChannel.shutdown();
            return false;
        }
        mChannel.shutdown();
        return true;
    }

    public String getAuthToken(){
        return this.authToken;
    }

}
