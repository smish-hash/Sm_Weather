package com.example.smweather;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class LocationRepository {
    private LocationDao locationDao;
    private LiveData<List<Location>> allLocations;

    public LocationRepository(Application application) {
        LocationDatabase database = LocationDatabase.getInstance(application);
        locationDao = database.locationDao();
        allLocations = locationDao.getAllLocations();
    }

    public void insert(Location location){
        new InsertNoteAsyncTask(locationDao).execute(location);
    }
    public void update(Location location){
        new UpdateNoteAsyncTask(locationDao).execute(location);
    }
    public void delete(Location location){
        new DeleteNoteAsyncTask(locationDao).execute(location);
    }
    public void deleteAllLocations(){
        new DeleteAllNoteAsyncTask(locationDao).execute();
    }
    public LiveData<List<Location>> getAllLocations(){
        return allLocations;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Location, Void, Void>{
        private LocationDao locationDao;

        public  InsertNoteAsyncTask(LocationDao locationDao){
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDao.insertLocation(locations[0]);
            return null;
        }
    }

    private class UpdateNoteAsyncTask extends AsyncTask<Location,Void,Void> {
        private LocationDao locationDao;

        public UpdateNoteAsyncTask(LocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDao.updateLocation(locations[0]);
            return null;
        }
    }

    private class DeleteNoteAsyncTask extends AsyncTask<Location,Void,Void> {
        private LocationDao locationDao;

        public DeleteNoteAsyncTask(LocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(Location... locations) {
            locationDao.deleteLocation(locations[0]);
            return null;
        }
    }

    private class DeleteAllNoteAsyncTask extends AsyncTask<Void,Void,Void> {
        private LocationDao locationDao;

        public DeleteAllNoteAsyncTask(LocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            locationDao.deleteAllLocations();
            return null;
        }
    }
}
