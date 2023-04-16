package com.example.foodiesparadise.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foodiesparadise.User;
import com.example.foodiesparadise.db.typeConverters.DateTypeConverter;

@Database(entities = {User.class}, version = 1)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "RESTAURANT_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";

    public abstract UserDAO getUserDAO();
}
