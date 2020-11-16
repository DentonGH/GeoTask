package com.cagriyorguner.geotask.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "table_name")
public class CustomMenuItem implements Serializable {
    //Create id column
    @PrimaryKey(autoGenerate = true)
    private int ID;

    //Create userId column
    @ColumnInfo(name = "userId")
    private int userId;

    //Create taskName column
    @ColumnInfo(name = "taskName")
    private String taskName;

    //Create locationName column
    @ColumnInfo(name = "locationName")
    private String locationName;

    //Create description column
    @ColumnInfo(name = "description")
    private String description;

    //Create latitude column
    @ColumnInfo(name = "latitude")
    private double latitude;

    //Create latitude column
    @ColumnInfo(name = "longitude")
    private double longitude;

    public CustomMenuItem() {
    }

    public CustomMenuItem(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //getters and setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
