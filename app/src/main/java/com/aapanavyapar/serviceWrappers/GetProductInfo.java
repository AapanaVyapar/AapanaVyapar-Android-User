package com.aapanavyapar.serviceWrappers;

import android.widget.Toast;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.GetProductRequest;
import com.aapanavyapar.aapanavyapar.services.GetProductResponse;
import com.aapanavyapar.aapanavyapar.services.InitUserRequest;
import com.aapanavyapar.aapanavyapar.services.InitUserResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GetProductInfo {
    GetProductResponse response;

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public GetProductInfo() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);
    }


    public int getInfo(String authToken, String productId, String shopId) {
        GetProductRequest getProductRequest = GetProductRequest.newBuilder()
                .setApiKey(MainActivity.API_KEY)
                .setToken(authToken)
                .setProductId(productId)
                .setShopId(shopId)
                .build();

        try {
            GetProductResponse getProductResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).getProduct(getProductRequest);
            mChannel.shutdown();
            response = getProductResponse;
            return 1;

        } catch (StatusRuntimeException e) {
            mChannel.shutdown();
            if (e.getMessage().equals("UNAUTHENTICATED: Request With Invalid Token")) {
                return 2;
            }


            return 0;
        }
    }

    public GetProductResponse getResponse() {
        return response;
    }
}
