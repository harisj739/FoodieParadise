package com.example.foodiesparadise.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foodiesparadise.Item;
import com.example.foodiesparadise.Order;
import com.example.foodiesparadise.Restaurant;
import com.example.foodiesparadise.User;
import com.example.foodiesparadise.db.typeConverters.DateTypeConverter;

@Database(entities = {User.class, Restaurant.class, Item.class, Order.class}, version = 1)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "RESTAURANT_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String RESTAURANT_TABLE = "RESTAURANT_TABLE";
    public static final String ITEM_TABLE = "ITEM_TABLE";
    public static final String ORDER_TABLE = "ORDER_TABLE";

    public abstract UserDAO getUserDAO();
    public abstract RestaurantDAO getRestaurantDAO();
    public abstract ItemDAO getItemDAO();
//    public abstract OrderDAO getOrderDAO();
}
