package com.aapanavyapar.viewData;

public class CartProductData {
    Integer cart_product_image;
    String cart_product_name;
    String cart_shop_name;
    String cart_shop_description;

    public CartProductData(Integer cart_product_image, String cart_product_name, String cart_shop_name, String cart_shop_description) {
        this.cart_product_image = cart_product_image;
        this.cart_product_name = cart_product_name;
        this.cart_shop_name = cart_shop_name;
        this.cart_shop_description = cart_shop_description;
    }

    public Integer getCart_product_image() {
        return cart_product_image;
    }

    public void setCart_product_image(Integer cart_product_image) {
        this.cart_product_image = cart_product_image;
    }

    public String getCart_product_name() {
        return cart_product_name;
    }

    public void setCart_product_name(String cart_product_name) {
        this.cart_product_name = cart_product_name;
    }

    public String getCart_shop_name() {
        return cart_shop_name;
    }

    public void setCart_shop_name(String cart_shop_name) {
        this.cart_shop_name = cart_shop_name;
    }

    public String getCart_shop_description() {
        return cart_shop_description;
    }

    public void setCart_shop_description(String cart_shop_description) {
        this.cart_shop_description = cart_shop_description;
    }
}
