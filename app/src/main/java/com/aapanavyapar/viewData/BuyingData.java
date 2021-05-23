package com.aapanavyapar.viewData;

import com.aapanavyapar.aapanavyapar.services.Address;

import java.io.Serializable;

public class BuyingData implements Serializable {
    String productId = "";
    String authToken = "";
    String refreshToken = "";
    String shopId = "";
    Address address = null;
    int quantity = 1;

    public BuyingData(String authToken, String refreshToken, String productId, String shopId, Address address, int quantity) {
        this.authToken = authToken;
        this.refreshToken = refreshToken;
        this.productId = productId;
        this.shopId = shopId;
        this.address = address;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAuthToken() {
        return authToken;
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
