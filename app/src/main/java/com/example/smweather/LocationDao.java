package com.example.smweather;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDao {

    @Insert
    void insertLocation(Location location);

    @Update
    void updateLocation(Location location);

    @Delete
    void deleteLocation(Location location);

    @Query("DELETE FROM location_table")
    void deleteAllLocations();

    @Query("SELECT * FROM location_table")
    LiveData<List<Location>> getAllLocations();
}
