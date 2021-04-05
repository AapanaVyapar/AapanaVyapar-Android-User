package com.aapanavyapar.dataModel;

import androidx.lifecycle.ViewModel;

public class DataModel extends ViewModel {
    String authToken, refreshToken, passToken;
    String tokenIdentifier;

    public String getAuthToken() {
        return authToken;
    }

    public void setTokens(String auth, String ref, String tokenIdentifier){
        this.authToken = auth;
        this.refreshToken = ref;
        this.tokenIdentifier = tokenIdentifier;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setPassToken(String passToken) {
        this.passToken = passToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
