package com.aapanavyapar.serviceWrappers;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.Address;
import com.aapanavyapar.aapanavyapar.services.UpdateAddressRequest;
import com.aapanavyapar.aapanavyapar.services.UpdateAddressResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.dataModel.ViewDataModel;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class UpdateAddressWrapper {
    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    private ViewDataModel viewDataModel;

    public UpdateAddressWrapper(ViewModelStoreOwner owner) {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

        viewDataModel = new ViewModelProvider(owner).get(ViewDataModel.class);
    }

    public void updateAddress(Context context, String authToken, String refreshToken, Address address) {
        int state = 0;
        while(state == 0) {
            UpdateAddressRequest updateAddressRequest = UpdateAddressRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .setAddress(address)
                    .build();

            try {
                UpdateAddressResponse updateAddressResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).updateAddress(updateAddressRequest);
                state = 1;
                mChannel.shutdown();
                if(updateAddressResponse.getStatus()){
                    viewDataModel.setAddress(context, address);
                }

            } catch (StatusRuntimeException e) {
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
