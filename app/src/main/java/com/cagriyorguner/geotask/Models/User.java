package com.cagriyorguner.geotask.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "table_name2")
public class User implements Serializable {
    //Create id column
    @PrimaryKey(autoGenerate = true)
    private int ID;

    //Create taskName column
    @ColumnInfo(name = "username")
    private String username;

    //Create password column
    @ColumnInfo(name = "password")
    private String password;

    //Create isAuthenticated column
    @ColumnInfo(name = "isAuthenticated")
    private boolean isAuthenticated;

    //getters and setters

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
}
