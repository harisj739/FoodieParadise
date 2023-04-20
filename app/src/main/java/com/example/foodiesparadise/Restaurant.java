package com.example.foodiesparadise;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.foodiesparadise.db.AppDatabase;

@Entity(tableName = AppDatabase.RESTAURANT_TABLE)
public class Restaurant {

    @PrimaryKey (autoGenerate = true)
    private int mRestaurantId;
    private String mRestaurantName;
    private String mRestaurantLocation;
    private String mRestaurantDescription;
    private String mRestaurantType;

    private int mUserId;

    private boolean mOpen;

    public Restaurant(String restaurantName, String restaurantLocation, String restaurantType, int userId) {
        mRestaurantName = restaurantName;
        mRestaurantLocation = restaurantLocation;
        mRestaurantType = restaurantType;
        mUserId = userId;
    }

    public int getRestaurantId() {
        return mRestaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        mRestaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return mRestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        mRestaurantName = restaurantName;
    }

    public String getRestaurantLocation() {
        return mRestaurantLocation;
    }

    public void setRestaurantLocation(String restaurantLocation) {
        mRestaurantLocation = restaurantLocation;
    }

    public String getRestaurantDescription() {
        return mRestaurantDescription;
    }

    public void setRestaurantDescription(String restaurantDescription) {
        mRestaurantDescription = restaurantDescription;
    }

    public String getRestaurantType() {
        return mRestaurantType;
    }

    public void setRestaurantType(String restaurantType) {
        mRestaurantType = restaurantType;
    }

    public boolean isOpen() {
        return mOpen;
    }

    public void setOpen(boolean open) {
        mOpen = open;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
