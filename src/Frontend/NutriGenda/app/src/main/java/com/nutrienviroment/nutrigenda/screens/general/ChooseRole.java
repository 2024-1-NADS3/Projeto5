package com.nutrienviroment.nutrigenda.screens.general;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.screens.diet.DietScreen;
import com.nutrienviroment.nutrigenda.screens.nutritionist.NutritionistPatientListScreen;
import com.nutrienviroment.nutrigenda.screens.nutritionist.NutritionistRegisterScreen;
import com.nutrienviroment.nutrigenda.screens.user.UserChooseScreen;

public class ChooseRole extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.general_choose_role);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button patientButton = findViewById(R.id.button6);
        Button nutritionistButton = findViewById(R.id.button7);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        patientButton.setOnClickListener(v -> {
            String userToken = sharedPreferences.getString("token", null);
            String userId = sharedPreferences.getString("userId", null);

            if (userToken != null && userId != null) {
                Intent intent = new Intent(ChooseRole.this, DietScreen.class);
                intent.putExtra("EXTRA_SESSION_TOKEN", userToken);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(ChooseRole.this, UserChooseScreen.class);
                startActivity(intent);
            }
        });

        nutritionistButton.setOnClickListener(v -> {
            String nutriToken = sharedPreferences.getString("token", null);
            String nutriId = sharedPreferences.getString("userId", null);

            if (nutriToken != null && nutriId != null) {
                Intent intent = new Intent(ChooseRole.this, NutritionistPatientListScreen.class);
                intent.putExtra("EXTRA_SESSION_TOKEN", nutriToken);
                intent.putExtra("USER_ID", nutriId);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(ChooseRole.this, NutritionistRegisterScreen.class);
                startActivity(intent);
            }
        });
    }
}
