package com.aapanavyapar.dataModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.aapanavyapar.aapanavyapar.ProfileDB;
import com.aapanavyapar.aapanavyapar.services.Address;
import com.aapanavyapar.aapanavyapar.services.GetTrendingShopsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewDataModel extends ViewModel {
    Map<String, GetTrendingShopsResponse> trendingShopsMap;
    ArrayList<String> cartList, likeList;
    Address address;
    String userName;

    public ViewDataModel(){
        this.trendingShopsMap = new HashMap<>();
        this.cartList = new ArrayList<>();
        this.likeList = new ArrayList<>();
    }

    public void addShopToMap(GetTrendingShopsResponse response) {
        trendingShopsMap.put(response.getShops().getShopId(), response);
    }

    public Map<String, GetTrendingShopsResponse> getTrendingShopsMap() {
        return trendingShopsMap;
    }

    public void setCartList(Context context, ArrayList<String> cartList) {
        ProfileDB profileDB = new ProfileDB(context);
        profileDB.clearCartTable();
        for(String pid: cartList) {
            profileDB.insertToCartTable(pid);
        }
        this.cartList = cartList;
    }

    public void addToCart(Context context, String productId) {
        ProfileDB profileDB = new ProfileDB(context);
        if(profileDB.insertToCartTable(productId)){
            cartList.add(productId);
        }
    }

    public ArrayList<String> getCartList() {
        return cartList;
    }

    public boolean IsProductInCartList(String productId) {
        return cartList.contains(productId);
    }

    public void DeleteFromCartList(Context context, String productId) {
        ProfileDB profileDB = new ProfileDB(context);
        boolean b = profileDB.deleteFromCartTable(productId);
        if (b) {
            if (cartList.contains(productId)) {
                cartList.remove(productId);
            }
        }
    }

    public void setLikeList(Context context, ArrayList<String> likeList) {
        ProfileDB profileDB = new ProfileDB(context);
        profileDB.clearCartTable();
        for(String pid: likeList) {
            profileDB.insertToFavTable(pid);
        }
        this.likeList = likeList;
    }

    public void addToLike(Context context, String productId) {
        ProfileDB profileDB = new ProfileDB(context);
        if(profileDB.insertToFavTable(productId)){
            likeList.add(productId);
        }
    }

    public ArrayList<String> getLikeList() {
        return likeList;
    }

    public boolean IsProductInLikeList(String productId) {
        return likeList.contains(productId);
    }

    public void DeleteFromLikeList(Context context, String productId) {
        ProfileDB profileDB = new ProfileDB(context);
        boolean b = profileDB.deleteFromFavTable(productId);
        if (b) {
            if (likeList.contains(productId)) {
                likeList.remove(productId);
            }
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Context context, Address address) {
        ProfileDB profileDB = new ProfileDB(context);
        boolean i= profileDB.insert_Data(
                this.userName,
                address.getFullName(),
                address.getHouseDetails(),
                address.getStreetDetails(),
                address.getLandMark(),
                address.getPinCode(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPhoneNo()
        );
        if(i){
            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Not Successful", Toast.LENGTH_SHORT).show();
        }
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
