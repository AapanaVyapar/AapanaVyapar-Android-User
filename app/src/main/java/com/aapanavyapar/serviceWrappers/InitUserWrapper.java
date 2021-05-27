package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.InitUserRequest;
import com.aapanavyapar.aapanavyapar.services.InitUserResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class InitUserWrapper {

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public InitUserWrapper() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);
    }

    public boolean CreateUser(String authToken) {
        InitUserRequest initUserRequest = InitUserRequest.newBuilder()
                .setApiKey(MainActivity.API_KEY)
                .setToken(authToken)
                .build();

        try {
            InitUserResponse initUserResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).initUser(initUserRequest);
            mChannel.shutdown();
            return initUserResponse.getStatus();

        } catch (StatusRuntimeException e) {
            mChannel.shutdown();
            return false;
        }
    }

}
