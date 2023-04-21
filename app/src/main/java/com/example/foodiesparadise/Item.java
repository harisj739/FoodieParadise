package com.example.foodiesparadise;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foodiesparadise.db.AppDatabase;

@Entity(tableName = AppDatabase.ITEM_TABLE)
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int mItemId;
    private int mRestaurantId;
    private int mUserId;
    private String mItemName;
    private String mItemType;
    private String mItemDescription;
    private Double mItemCost;
    private int mItemQuantity;
    private boolean mAvailable;

    public Item(int restaurantId, int userId, String itemName, String itemType, Double itemCost) {
        mRestaurantId = restaurantId;
        mUserId = userId;
        mItemName = itemName;
        mItemType = itemType;
        mItemCost = itemCost;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public int getRestaurantId() {
        return mRestaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        mRestaurantId = restaurantId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        mItemName = itemName;
    }

    public String getItemType() {
        return mItemType;
    }

    public void setItemType(String itemType) {
        mItemType = itemType;
    }

    public String getItemDescription() {
        return mItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        mItemDescription = itemDescription;
    }

    public Double getItemCost() {
        return mItemCost;
    }

    public void setItemCost(Double itemCost) {
        mItemCost = itemCost;
    }

    public int getItemQuantity() {
        return mItemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        mItemQuantity = itemQuantity;
    }

    public boolean isAvailable() {
        return mAvailable;
    }

    public void setAvailable(boolean available) {
        mAvailable = available;
    }
}
