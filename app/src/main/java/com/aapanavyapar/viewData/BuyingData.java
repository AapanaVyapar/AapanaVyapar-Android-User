package com.aapanavyapar.viewData;

import java.io.Serializable;

public class BuyingData implements Serializable {
    String productId;
    String authToken;
    String refreshToken;
    String shopId;

    String fullName;
    String houseDetails;
    String streetDetails;

    String landmark;
    String pinCode;
    String city;
    String state;
    String country;
    String phoneNo;

    int quantity;

    public BuyingData(String productId, String authToken, String refreshToken, String shopId, String fullName, String houseDetails, String streetDetails, String landmark, String pinCode, String city, String state, String country, String phoneNo, int quantity) {
        this.productId = productId;
        this.authToken = authToken;
        this.refreshToken = refreshToken;
        this.shopId = shopId;
        this.fullName = fullName;
        this.houseDetails = houseDetails;
        this.streetDetails = streetDetails;
        this.landmark = landmark;
        this.pinCode = pinCode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.phoneNo = phoneNo;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHouseDetails() {
        return houseDetails;
    }

    public void setHouseDetails(String houseDetails) {
        this.houseDetails = houseDetails;
    }

    public String getStreetDetails() {
        return streetDetails;
    }

    public void setStreetDetails(String streetDetails) {
        this.streetDetails = streetDetails;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


}
