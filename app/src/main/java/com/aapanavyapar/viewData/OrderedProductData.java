package com.aapanavyapar.viewData;

public class OrderedProductData {
    Integer orederedProductImage;
    String orderedProductName;
    String orderedProductId;
    String orderedProductPrice;
    String orderedProductQty;
    String orderedProductStatus;
    String orderedTime;
    String orderedDeliveredTime;

    public OrderedProductData(Integer orederedProductImage, String orderedProductName, String orderedProductId, String orderedProductPrice, String orderedProductQty, String orderedProductStatus, String orderedTime, String orderedDeliveredTime) {
        this.orederedProductImage = orederedProductImage;
        this.orderedProductName = orderedProductName;
        this.orderedProductId = orderedProductId;
        this.orderedProductPrice = orderedProductPrice;
        this.orderedProductQty = orderedProductQty;
        this.orderedProductStatus = orderedProductStatus;
        this.orderedTime = orderedTime;
        this.orderedDeliveredTime = orderedDeliveredTime;
    }

    public Integer getOrederedProductImage() {
        return orederedProductImage;
    }

    public void setOrederedProductImage(Integer orederedProductImage) {
        this.orederedProductImage = orederedProductImage;
    }

    public String getOrderedProductName() {
        return orderedProductName;
    }

    public void setOrderedProductName(String orderedProductName) {
        this.orderedProductName = orderedProductName;
    }

    public String getOrderedProductId() {
        return orderedProductId;
    }

    public void setOrderedProductId(String orderedProductId) {
        this.orderedProductId = orderedProductId;
    }

    public String getOrderedProductPrice() {
        return orderedProductPrice;
    }

    public void setOrderedProductPrice(String orderedProductPrice) {
        this.orderedProductPrice = orderedProductPrice;
    }

    public String getOrderedProductQty() {
        return orderedProductQty;
    }

    public void setOrderedProductQty(String orderedProductQty) {
        this.orderedProductQty = orderedProductQty;
    }

    public String getOrderedProductStatus() {
        return orderedProductStatus;
    }

    public void setOrderedProductStatus(String orderedProductStatus) {
        this.orderedProductStatus = orderedProductStatus;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
    }

    public String getOrderedDeliveredTime() {
        return orderedDeliveredTime;
    }

    public void setOrderedDeliveredTime(String orderedDeliveredTime) {
        this.orderedDeliveredTime = orderedDeliveredTime;
    }
}
