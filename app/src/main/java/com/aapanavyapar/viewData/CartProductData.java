package com.aapanavyapar.viewData;

public class CartProductData {
    String cartProductImage;
    String cartProductName;
    String cartShopName;
    long likes;
    String cartShopId;

    public CartProductData(String cartProductImage, String cartShopId, String cartProductName, String cartShopName, long likes) {
        this.cartProductImage = cartProductImage;
        this.cartProductName = cartProductName;
        this.cartShopName = cartShopName;
        this.likes = likes;
        this.cartShopId = cartShopId;
    }

    public String getCartProductImage() {
        return cartProductImage;
    }

    public void setCartProductImage(String cartProductImage) {
        this.cartProductImage = cartProductImage;
    }

    public String getShopId() {
        return cartShopId;
    }

    public void setCartShopId(String shopId) {
        this.cartShopId = shopId;
    }

    public String getCartProductName() {
        return cartProductName;
    }

    public void setCartProductName(String cartProductName) {
        this.cartProductName = cartProductName;
    }

    public String getCartShopName() {
        return cartShopName;
    }

    public void setCartShopName(String cartShopName) {
        this.cartShopName = cartShopName;
    }

    public long getCartLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }
}
