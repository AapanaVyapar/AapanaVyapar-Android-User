package com.aapanavyapar.dataModel;

import androidx.lifecycle.ViewModel;

public class DataModel extends ViewModel {
    String authToken, refreshToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setTokens(String auth, String ref){
        this.authToken = auth;
        this.refreshToken = ref;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
