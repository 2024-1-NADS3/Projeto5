package com.nutrienviroment.nutrigenda.services.user;

import com.nutrienviroment.nutrigenda.models.user.TokenResponse;
import com.nutrienviroment.nutrigenda.models.user.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServices {
    @POST("api/user")
    Call<Void> registerUser(@Body User user);

    @POST("api/user/login")
    Call<TokenResponse> loginUser(@Body User user);

    @GET("api/user/{id}")
    Call<User> getUserById(@Path("id") String userId);

    @GET("api/user/nutritionist/{nutritionistId}")
    Call<List<User>> getUsersByNutritionistId(@Path("nutritionistId") String nutritionistId);
}
