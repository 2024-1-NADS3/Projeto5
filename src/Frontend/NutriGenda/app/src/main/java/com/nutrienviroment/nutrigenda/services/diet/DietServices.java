package com.nutrienviroment.nutrigenda.services.diet;

import com.nutrienviroment.nutrigenda.models.diet.Diet;
import com.nutrienviroment.nutrigenda.models.diet.Meal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DietServices {
    @GET("api/diet")
    Call<List<Diet>> getAllDiets();

    @GET("api/diet/{id}")
    Call<Diet> getDietById(@Path("id") String dietId);

    @POST("api/diet")
    Call<Diet> createDiet(@Body Diet dietDto);

    @PUT("api/diet/{id}")
    Call<Void> updateDiet(@Path("id") String dietId, @Body Diet dietDto);

    @DELETE("api/diet/{id}")
    Call<Void> deleteDiet(@Path("id") String dietId);

    @GET("api/diet/user/{userId}")
    Call<List<Diet>> getDietsByUserId(@Path("userId") String userId);
}

