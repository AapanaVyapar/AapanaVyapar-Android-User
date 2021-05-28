package com.aapanavyapar.serviceWrappers;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.aapanavyapar.aapanavyapar.MainActivity;
import com.aapanavyapar.aapanavyapar.services.GetProfileRequest;
import com.aapanavyapar.aapanavyapar.services.GetProfileResponse;
import com.aapanavyapar.aapanavyapar.services.ViewProviderServiceGrpc;
import com.aapanavyapar.dataModel.ViewDataModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GetProfileWrapper {
    ManagedChannel mChannel;
    ViewProviderServiceGrpc.ViewProviderServiceBlockingStub blockingStub;

    private ViewDataModel viewDataModel;

    public GetProfileWrapper(ViewModelStoreOwner owner) {
        mChannel = ManagedChannelBuilder.forTarget(MainActivity.VIEW_SERVICE_ADDRESS).usePlaintext().build();
        blockingStub = ViewProviderServiceGrpc.newBlockingStub(mChannel);

        viewDataModel = new ViewModelProvider(owner).get(ViewDataModel.class);
    }

    public void updateProfile(Context context, String authToken, String refreshToken) {
        int state = 0;
        while(state == 0) {
            GetProfileRequest getProductRequest = GetProfileRequest.newBuilder()
                    .setApiKey(MainActivity.API_KEY)
                    .setToken(authToken)
                    .build();

            try {
                GetProfileResponse getProfileResponse = blockingStub.withDeadlineAfter(5, TimeUnit.MINUTES).getProfile(getProductRequest);
                state = 1;
                mChannel.shutdown();
                viewDataModel.setUserName(getProfileResponse.getUserName());
                if(getProfileResponse.getCartList() != null && getProfileResponse.getCartList().size() == 0)
                    viewDataModel.setCartList(context, (ArrayList<String>) getProfileResponse.getCartList());

                if(getProfileResponse.getFavouriteList() != null && getProfileResponse.getFavouriteList().size() == 0)
                    viewDataModel.setLikeList(context, (ArrayList<String>) getProfileResponse.getFavouriteList());

                if(getProfileResponse.getAddress() != null)
                    viewDataModel.setAddress(context, getProfileResponse.getAddress());

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
