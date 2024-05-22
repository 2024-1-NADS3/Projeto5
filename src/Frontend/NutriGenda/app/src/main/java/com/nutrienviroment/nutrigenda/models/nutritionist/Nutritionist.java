package com.nutrienviroment.nutrigenda.models.nutritionist;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nutritionist {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String Name;

    @SerializedName("email")
    private String Email;

    @SerializedName("password")
    private String Password;

    @SerializedName("crn")
    private String Crn;

    public Nutritionist(String email, String password) {
        this.Email = email;
        this.Password = password;
    }

    public Nutritionist(String name, String email, String password, String crn) {
        this.Name = name;
        this.Email = email;
        this.Password = password;
        this.Crn = crn;
    }

    @Override
    public String toString() {
        Log.d("NutritionistToString", "Returning name: " + Name);
        return Name != null ? Name : "Unnamed";
    }
}


