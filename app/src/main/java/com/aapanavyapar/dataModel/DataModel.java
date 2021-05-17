package com.aapanavyapar.dataModel;

import androidx.lifecycle.ViewModel;

public class DataModel extends ViewModel {
    String authToken = "", refreshToken = "", passToken = "";
    int []access;

    public void setTokens(String auth, String ref, int []tokenIdentifier){
        this.authToken = auth;
        this.refreshToken = ref;
        this.access = tokenIdentifier;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setPassToken(String passToken) {
        this.passToken = passToken;
    }

    public void setAccess(int []access) {
        this.access = access;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPassToken() {
        return passToken;
    }

    public boolean CanWeUseTokenForThis(int method){
        if(this.access == null) {
            return false;
        }

        for (int i: this.access) {
            if(i == method){
                return true;
            }
        }
        return false;
    }

}
