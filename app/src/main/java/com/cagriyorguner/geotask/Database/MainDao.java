package com.cagriyorguner.geotask.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cagriyorguner.geotask.Models.CustomMenuItem;
import com.cagriyorguner.geotask.Models.User;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {

    /////////////// FOR TABLE1 (CustomMenuItem)

    //Insert query
    @Insert(onConflict = REPLACE)
    void insertCustomMenuItem(CustomMenuItem customMenuItem);

    //Delete query
    @Delete
    void deleteCustomMenuItem(CustomMenuItem customMenuItem);

    //Delete all query
    @Delete
    void resetCustomMenuItem(List<CustomMenuItem> customMenuItem);

    //Get all data query
    @Query("SELECT * FROM table_name WHERE userId = :sUserId")
    List<CustomMenuItem> getAllCustomMenuItems(int sUserId);

    /////////////// FOR TABLE2 (User)

    //Insert query
    @Insert(onConflict = REPLACE)
    void insertUser(User user);

    //Delete query
    @Delete
    void deleteUser(User user);

    //Delete all query
    @Delete
    void resetUser(List<User> user);

    //Get all data query
    @Query("SELECT * FROM table_name2")
    List<User> getAllUsers();

    //Get authenticated user
    @Query("SELECT * FROM table_name2 WHERE isAuthenticated = :sIsAuthenticated")
    User getAuthenticatedUser(boolean sIsAuthenticated);

    //Get username/password matched user's id
    @Query("SELECT ID FROM table_name2 WHERE username = :sUsername AND password = :sPassword")
    int getToBeLoggedInUserId(String sUsername, String sPassword);

    //Get user if there is anyone with this username
    @Query("SELECT * FROM table_name2 WHERE username = :sUsername")
    User getUserWithThisUsername(String sUsername);

    //Authenticate user
    @Query("UPDATE table_name2 SET isAuthenticated = :sIsAuthenticated WHERE ID = :sID")
    void authenticateUser(boolean sIsAuthenticated, int sID);

}
