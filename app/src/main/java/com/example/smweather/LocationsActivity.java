package com.example.smweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.List;

public class LocationsActivity extends AppCompatActivity {

    private LocationViewModel locationViewModel;

    AutoCompleteTextView trialedCity;
    String[] trialCityNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        setTitle("Locations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        trialedCity = findViewById(R.id.aedCity);
        trialCityNames = getResources().getStringArray(R.array.sample_city_names);
        ArrayAdapter<String> sampleCityListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, trialCityNames);
        trialedCity.setAdapter(sampleCityListAdapter);

        trialedCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trialedCity.setCursorVisible(true);
            }
        });

        RecyclerView locationRecyclerView = findViewById(R.id.location_recycler_view);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationRecyclerView.setHasFixedSize(true);

        final LocationAdapter adapter = new LocationAdapter();
        locationRecyclerView.setAdapter(adapter);

        locationViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(LocationViewModel.class);
        locationViewModel.getAllLocations().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                adapter.notifyDataSetChanged();
                adapter.submitList(locations);
            }
        });

        adapter.setOnLocationClickListener(new LocationAdapter.OnLocationClickListener(){
            @Override
            public void onLocationClick(Location location) {
//                Intent intent = new Intent(LocationsActivity.this, MainActivity.class);
//                intent.putExtra(MainActivity.EXTRA_LOCATION,location.getStrlocation());
//                startActivityForResult(intent, SELECT_LOCATION_REQUEST);

                String trialvalue = location.getStrlocation();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("trialValue",trialvalue);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

        // TODO: 12-08-2020 swipe gesture to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                locationViewModel.delete(adapter.getLocationAt(viewHolder.getAdapterPosition()));
                Toast.makeText(LocationsActivity.this, "Location deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(locationRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(LocationViewModel.class);

    }

    public void trialdata(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(myApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myApi trialapi = retrofit.create(myApi.class);

        Call<Example> exampleCall = trialapi.getinfo(trialedCity.getText().toString().trim(),MainActivity.openWeatherapiKey);
        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.code() == 404) {
                    Toast.makeText(LocationsActivity.this, "Invalid City", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 429) {
                    Toast.makeText(LocationsActivity.this, "Your account is temporary blocked due to exceeding of requests limitation of your subscription type.", Toast.LENGTH_SHORT).show();
                } else if (!response.isSuccessful()) {
                    Toast.makeText(LocationsActivity.this, "Error code: " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    String trialvalue = trialedCity.getText().toString().trim();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("trialValue",trialvalue);

                    Location location = new Location(trialvalue);
                    locationViewModel.insert(location);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(LocationsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}