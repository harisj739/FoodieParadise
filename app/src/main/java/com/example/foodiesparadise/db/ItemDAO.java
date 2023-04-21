package com.example.foodiesparadise.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodiesparadise.Item;
import com.example.foodiesparadise.Restaurant;

import java.util.List;

@Dao
public interface ItemDAO {
    @Insert
    void insert(Item... items);

    @Update
    void update(Item... items);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mRestaurantId = :restaurantId")
    List<Item> getAllItemsByRestaurantId(int restaurantId);

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mUserId = :userId")
    List<Item> getAllItemsByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mItemName = :itemName")
    Item getItemByName(String itemName);

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mItemId = :itemId")
    Item getItemByItemId(int itemId);

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mItemType = :itemType")
    Item getItemsByType(String itemType);
}
