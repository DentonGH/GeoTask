package com.cagriyorguner.geotask.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cagriyorguner.geotask.Models.CustomMenuItem;
import com.cagriyorguner.geotask.Models.User;

//Add database entities
@Database(entities = {CustomMenuItem.class, User.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    //Create database instance
    private static RoomDB database;
    //Define database name
    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context){
        //Check condition
        if (database == null){
            //Initialize database
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    //Create Dao
    public abstract MainDao mainDao();
}
