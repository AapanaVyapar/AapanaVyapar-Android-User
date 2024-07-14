package com.aapanavyapar.serviceWrappers;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.GetOrdersRequest;
import com.aapanavyapar.aapanavyapar.services.GetOrdersResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.interfaces.RecycleViewUpdater;
import com.aapanavyapar.viewData.OrderedProductData;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GetOrdersWrapper {

    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    public GetOrdersWrapper() {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);
    }

    public boolean GetOrders(String authToken, RecycleViewUpdater updater) {
        GetOrdersRequest ordersRequest = GetOrdersRequest.newBuilder()
                .setApiKey(MainActivity.API_KEY)
                .setToken(authToken)
                .build();

        try {
            Iterator<GetOrdersResponse> getOrdersResponseIterator = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).getOrders(ordersRequest);
            mChannel.shutdown();

            while (getOrdersResponseIterator.hasNext()) {
                GetOrdersResponse response = getOrdersResponseIterator.next();
                OrderedProductData orderedProductData = new OrderedProductData(
                        response.getProductImage(),
                        response.getProductName(),
                        response.getProductId(),
                        String.valueOf(response.getPrice() / 100),
                        String.valueOf(response.getQuantity()),
                        response.getStatus().name(),
                        response.getOrderTimeStamp(),
                        response.getDeliveryTimeStamp()
                );
                updater.updateRecycleView(orderedProductData);
            }
            return true;

        } catch (StatusRuntimeException e) {
            e.printStackTrace();
            mChannel.shutdown();
            return false;

        }
    }
}
