package com.example.smweather;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Location.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public static final String DB_NAME = "location_database";
    private static LocationDatabase instance;

    public abstract LocationDao locationDao();

    public static synchronized LocationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    LocationDatabase.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // using call back in line 21 to start with some predefined locations
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void,Void,Void> {
        private LocationDao locationDao;

        public PopulateDBAsyncTask(LocationDatabase db) {
            locationDao = db.locationDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            locationDao.insertLocation(new Location("Ohio"));
            return null;
        }
    }
}
