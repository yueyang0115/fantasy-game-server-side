package edu.duke.ece.fantasy.json;

import java.util.Map;

public class ShopRequestMessage {
    private int shopID;
    private Map<Integer,Integer> itemMap;
    private String action;//"list""buy""sell"

    public ShopRequestMessage() {
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<Integer, Integer> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<Integer, Integer> itemMap) {
        this.itemMap = itemMap;
    }
}
