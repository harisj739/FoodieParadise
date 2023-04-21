package com.example.foodiesparadise.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodiesparadise.Item;
import com.example.foodiesparadise.Order;
import com.example.foodiesparadise.Restaurant;

import java.util.List;

@Dao
public interface OrderDAO {
    void insert(Order... orders);

    @Update
    void update(Order... orders);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM " + AppDatabase.ORDER_TABLE + " WHERE mRestaurantId = :restaurantId")
    List<Order> getAllOrdersByRestaurantId(int restaurantId);

    @Query("SELECT * FROM " + AppDatabase.ORDER_TABLE + " WHERE mUserId = :userId")
    List<Order> getAllOrdersByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.ORDER_TABLE + " WHERE mUserId = :userId")
    Order getOrderByUserId(int userId);
}
