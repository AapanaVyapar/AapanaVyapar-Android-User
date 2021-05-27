package com.aapanavyapar.dataModel;

import androidx.lifecycle.ViewModel;

import com.aapanavyapar.aapanavyapar.services.GetTrendingShopsResponse;

import java.util.HashMap;
import java.util.Map;

public class ViewDataModel extends ViewModel {
    Map<String, GetTrendingShopsResponse> trendingShopsMap;

    public ViewDataModel(){
        this.trendingShopsMap = new HashMap<String, GetTrendingShopsResponse>();
    }

    public void addShopToMap(GetTrendingShopsResponse response) {
        trendingShopsMap.put(response.getShops().getShopId(), response);
    }

    public Map<String, GetTrendingShopsResponse> getTrendingShopsMap() {
        return trendingShopsMap;
    }
}
