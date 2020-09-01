package com.example.smweather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface myApi {
    // api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}
    String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    @GET("weather")
    Call<Example> getinfo(@Query("q")String cityname,
                             @Query("appid")String apikey);
}
