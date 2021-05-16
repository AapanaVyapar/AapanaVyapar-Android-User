package com.aapanavyapar.viewData;

public class ProductData {
    Integer product_image;
    String product_name;
    String shop_name;


    public ProductData(Integer product_image, String product_name, String shop_name) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.shop_name = shop_name;

    }

    public Integer getProduct_image() {
        return product_image;
    }

    public void setProduct_image(Integer product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }


}
