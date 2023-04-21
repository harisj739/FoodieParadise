package com.example.foodiesparadise;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foodiesparadise.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = AppDatabase.ORDER_TABLE)
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int mOrderId;
    private int mRestaurantId;
    private int mItemId;
    private int mUserId;
    private int mTotalQuantity;
    private double mTotalCost;

    public Order(int restaurantId, int itemId, int userId) {
        mRestaurantId = restaurantId;
        mItemId = itemId;
        mUserId = userId;
    }

    public int getOrderId() {
        return mOrderId;
    }

    public void setOrderId(int orderId) {
        mOrderId = orderId;
    }

    public int getRestaurantId() {
        return mRestaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        mRestaurantId = restaurantId;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getTotalQuantity() {
        return mTotalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        mTotalQuantity = totalQuantity;
    }

    public double getTotalCost() {
        return mTotalCost;
    }

    public void setTotalCost(double totalCost) {
        mTotalCost = totalCost;
    }
}
