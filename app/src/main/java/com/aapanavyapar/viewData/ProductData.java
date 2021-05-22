package com.aapanavyapar.viewData;

import com.aapanavyapar.aapanavyapar.services.Category;
import com.aapanavyapar.aapanavyapar.services.Location;

import java.io.Serializable;

public class ProductData implements Serializable {
    String productId = "";
    String productName = "";

    String shopId = "";
    String shopName = "";

    String productImage = "";
    String shopPrimaryImage = "";

    Category[] productCategories;
    Category[] shopCategories;

    long productLikes;

    double shopRating;
    String shopKeeper = "";

    Location shopLocation;

    public ProductData(String productId, String productName, String shopId, String shopName, String productImage, String shopPrimaryImage, Category[] productCategories, Category[] shopCategories, long productLikes, double shopRating, String shopKeeper, Location location) {
        this.productId = productId;
        this.productName = productName;
        this.shopId = shopId;
        this.shopName = shopName;
        this.productImage = productImage;
        this.shopPrimaryImage = shopPrimaryImage;
        this.productCategories = productCategories;
        this.shopCategories = shopCategories;
        this.productLikes = productLikes;
        this.shopRating = shopRating;
        this.shopKeeper = shopKeeper;
        this.shopLocation = location;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getShopPrimaryImage() {
        return shopPrimaryImage;
    }

    public void setShopPrimaryImage(String shopPrimaryImage) {
        this.shopPrimaryImage = shopPrimaryImage;
    }

    public Category[] getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(Category[] productCategories) {
        this.productCategories = productCategories;
    }

    public Category[] getShopCategories() {
        return shopCategories;
    }

    public void setShopCategories(Category[] shopCategories) {
        this.shopCategories = shopCategories;
    }

    public long getProductLikes() {
        return productLikes;
    }

    public void setProductLikes(long productLikes) {
        this.productLikes = productLikes;
    }

    public double getShopRating() {
        return shopRating;
    }

    public void setShopRating(double shopRating) {
        this.shopRating = shopRating;
    }

    public String getShopKeeper() {
        return shopKeeper;
    }

    public void setShopKeeper(String shopKeeper) {
        this.shopKeeper = shopKeeper;
    }

    public Location getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(Location shopLocation) {
        this.shopLocation = shopLocation;
    }
}
