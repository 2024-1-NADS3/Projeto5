package com.nutrienviroment.nutrigenda.screens.nutritionist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.diet.Diet;
import com.nutrienviroment.nutrigenda.models.user.User;
import com.nutrienviroment.nutrigenda.screens.chat.ChatScreen;
import com.nutrienviroment.nutrigenda.screens.diet.CreateDietScreen;
import com.nutrienviroment.nutrigenda.services.diet.DietServices;
import com.nutrienviroment.nutrigenda.services.user.UserServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PatientDietManagementScreen extends AppCompatActivity {

    private DietServices dietApiService;
    private UserServices userApiService;
    private TextView tvPatientName;
    private TextView tvDietDetails;
    private Button btnCreateDiet;
    private Button btnDeleteDiet;
    private Button btnAskSuggestions;
    private String userId;
    private Diet currentDiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.patient_diet_management_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nutrigendaapi.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dietApiService = retrofit.create(DietServices.class);
        userApiService = retrofit.create(UserServices.class);

        tvPatientName = findViewById(R.id.tvPatientName);
        tvDietDetails = findViewById(R.id.tvDietDetails);
        btnCreateDiet = findViewById(R.id.btnCreateDiet);
        btnDeleteDiet = findViewById(R.id.btnDeleteDiet);
        btnAskSuggestions = findViewById(R.id.btnAskSuggestions);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        userId = getIntent().getStringExtra("USER_ID");

        fetchPatientDetails(userId);
        fetchDietDetails(userId);

        btnCreateDiet.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDietManagementScreen.this, CreateDietScreen.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        btnDeleteDiet.setOnClickListener(v -> deleteDiet(currentDiet.getId()));

        btnAskSuggestions.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDietManagementScreen.this, ChatScreen.class);
            startActivity(intent);
        });
    }

    private void fetchPatientDetails(String userId) {
        userApiService.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User patient = response.body();
                    tvPatientName.setText(patient.getName());
                } else {
                    Toast.makeText(PatientDietManagementScreen.this, "Erro ao carregar detalhes do paciente", Toast.LENGTH_SHORT).show();
                    Log.e("DietManagementScreen", "Erro ao carregar detalhes do paciente: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(PatientDietManagementScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DietManagementScreen", "Falha na comunicação: " + t.getMessage(), t);
            }
        });
    }

    private void fetchDietDetails(String userId) {
        dietApiService.getDietsByUserId(userId).enqueue(new Callback<List<Diet>>() {
            @Override
            public void onResponse(Call<List<Diet>> call, Response<List<Diet>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    currentDiet = response.body().get(0);
                    tvDietDetails.setText("Dieta ativa encontrada");
                    btnCreateDiet.setVisibility(View.GONE);
                    btnDeleteDiet.setVisibility(View.VISIBLE);
                } else {
                    tvDietDetails.setText("Nenhuma dieta ativa");
                    btnCreateDiet.setVisibility(View.VISIBLE);
                    btnDeleteDiet.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Diet>> call, Throwable t) {
                Toast.makeText(PatientDietManagementScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DietManagementScreen", "Falha na comunicação: " + t.getMessage(), t);
            }
        });
    }

    private void deleteDiet(String dietId) {
        dietApiService.deleteDiet(dietId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PatientDietManagementScreen.this, "Dieta deletada com sucesso", Toast.LENGTH_SHORT).show();
                    fetchDietDetails(userId);
                } else {
                    Toast.makeText(PatientDietManagementScreen.this, "Erro ao deletar dieta", Toast.LENGTH_SHORT).show();
                    Log.e("DietManagementScreen", "Erro ao deletar dieta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PatientDietManagementScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DietManagementScreen", "Falha na comunicação: " + t.getMessage(), t);
            }
        });
    }
}
