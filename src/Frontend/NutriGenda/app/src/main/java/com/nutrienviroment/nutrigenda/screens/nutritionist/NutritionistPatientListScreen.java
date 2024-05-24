package com.nutrienviroment.nutrigenda.screens.nutritionist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.user.User;
import com.nutrienviroment.nutrigenda.services.user.UserServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NutritionistPatientListScreen extends AppCompatActivity {

    private UserServices apiService;
    private RecyclerView rvPatientList;
    private SharedPreferences sharedPreferences;
    private String nutritionistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.nutritionist_patient_list_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("NutriPrefs", MODE_PRIVATE);
        nutritionistId = sharedPreferences.getString("USER_ID", null);

        if (nutritionistId == null) {
            Toast.makeText(this, "ID do nutricionista não fornecido.", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nutrigendaapi.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(UserServices.class);

        rvPatientList = findViewById(R.id.rvPatientList);
        rvPatientList.setLayoutManager(new LinearLayoutManager(this));

        fetchPatients(nutritionistId);
    }

    private void fetchPatients(String nutritionistId) {
        apiService.getUsersByNutritionistId(nutritionistId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> patients = response.body();
                    PatientAdapter adapter = new PatientAdapter(patients, patient -> {
                        Intent intent = new Intent(NutritionistPatientListScreen.this, PatientDietManagementScreen.class);
                        intent.putExtra("USER_ID", patient.getId());
                        startActivity(intent);
                    });
                    rvPatientList.setAdapter(adapter);
                } else {
                    Toast.makeText(NutritionistPatientListScreen.this, "Erro ao carregar pacientes", Toast.LENGTH_SHORT).show();
                    Log.e("PatientListScreen", "Erro ao carregar pacientes: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(NutritionistPatientListScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("PatientListScreen", "Falha na comunicação: " + t.getMessage(), t);
            }
        });
    }
}
