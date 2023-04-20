package com.example.foodiesparadise.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodiesparadise.Restaurant;
import com.example.foodiesparadise.User;

import java.util.List;

@Dao
public interface RestaurantDAO {
    @Insert
    void insert(Restaurant... restaurants);

    @Update
    void update(Restaurant... restaurants);

    @Delete
    void delete(Restaurant restaurant);

    @Query("SELECT * FROM " + AppDatabase.RESTAURANT_TABLE)
    List<Restaurant> getAllRestaurants();

    @Query("SELECT * FROM " + AppDatabase.RESTAURANT_TABLE + " WHERE mRestaurantName = :restaurantName")
    Restaurant getRestaurantByName(String restaurantName);

    @Query("SELECT * FROM " + AppDatabase.RESTAURANT_TABLE + " WHERE mRestaurantId = :restaurantId")
    Restaurant getRestaurantByRestaurantId(int restaurantId);

    @Query("SELECT * FROM " + AppDatabase.RESTAURANT_TABLE + " WHERE mUserId = :userId")
    Restaurant getRestaurantByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.RESTAURANT_TABLE + " WHERE mRestaurantType = :restaurantType")
    Restaurant getRestaurantByType(String restaurantType);

    @Query("SELECT * FROM " + AppDatabase.RESTAURANT_TABLE + " WHERE mRestaurantLocation = :restaurantLocation")
    Restaurant getRestaurantByLocation(String restaurantLocation);

}
