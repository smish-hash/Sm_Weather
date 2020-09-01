package com.example.smweather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_table")
public class Location {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String strlocation;

    public Location(String strlocation) {
        this.strlocation = strlocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrlocation() {
        return strlocation;
    }
}
