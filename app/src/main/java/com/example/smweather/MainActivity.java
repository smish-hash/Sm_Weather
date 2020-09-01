package com.example.smweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView edCity;

    TextView tvCity, tvTemp, tvHumidity;
    public static String openWeatherapiKey = "aaca20adf03bcc0485693a76344f9e08";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_location, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_locations) {
            // TODO: 12-08-2020 Handle location clicks here
            Intent intent = new Intent(MainActivity.this, LocationsActivity.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCity = findViewById(R.id.tvcity);
        tvTemp = findViewById(R.id.tvtemp);
        tvHumidity = findViewById(R.id.tvhumidity);
    }

    public void getWeather() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(myApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myApi api = retrofit.create(myApi.class);

        Call<Example> exampleCall = api.getinfo(tvCity.getText().toString().trim(), openWeatherapiKey);

        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Invalid City", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 429) {
                    Toast.makeText(MainActivity.this, "Your account is temporary blocked due to exceeding of requests limitation of your subscription type.", Toast.LENGTH_SHORT).show();
                } else if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Error code: " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    Example data = response.body();
                    Main main = data.getMain();
                    Double temp = main.getTemp() - 273.15;
                    Integer humidity = main.getHumidity();
                    tvTemp.setText("Current temp: " + String.format("%.2f", temp) + "ºC");
                    tvHumidity.setText("Humidity: " + humidity + "%");


                    tvTemp.setVisibility(View.VISIBLE);
                    tvHumidity.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String trialValueReceived = data.getStringExtra("trialValue");
                tvCity.setText(trialValueReceived);
                getWeather();
            }
            if (resultCode == RESULT_CANCELED) {
                tvCity.setText(tvCity.getText().toString().trim());
                getWeather();
            }
        }
    }
}