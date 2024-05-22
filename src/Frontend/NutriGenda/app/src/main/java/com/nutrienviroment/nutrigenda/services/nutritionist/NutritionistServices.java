package com.nutrienviroment.nutrigenda.services.nutritionist;

import com.nutrienviroment.nutrigenda.models.nutritionist.Nutritionist;
import com.nutrienviroment.nutrigenda.models.user.TokenResponse;
import com.nutrienviroment.nutrigenda.models.user.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NutritionistServices {

    @POST("api/nutritionist")
    Call<Void> registerNutritionist(@Body Nutritionist nutritionist);

    @POST("api/nutritionist/login")
    Call<TokenResponse> loginNutritionist(@Body Nutritionist  nutritionist);

    @GET("api/nutritionist")
    Call<List<Nutritionist>> getNutritionists();
}
