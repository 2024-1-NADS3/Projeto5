package com.nutrienviroment.nutrigenda.models.diet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("foodItems")
    private List<FoodItem> foodItems;

    @SerializedName("dietId")
    public String dietId;
}

