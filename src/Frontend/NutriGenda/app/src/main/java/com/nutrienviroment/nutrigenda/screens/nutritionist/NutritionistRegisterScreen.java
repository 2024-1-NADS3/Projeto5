package com.nutrienviroment.nutrigenda.screens.nutritionist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.nutritionist.Nutritionist;
import com.nutrienviroment.nutrigenda.models.user.TokenResponse;
import com.nutrienviroment.nutrigenda.services.nutritionist.NutritionistServices;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NutritionistRegisterScreen extends AppCompatActivity {
    private NutritionistServices apiService;
    private SharedPreferences sharedPreferences;
    private static final List<String> ALLOWED_CRNS = Arrays.asList("123456", "654321", "111222", "333444", "555666");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutritionist_register_screen);

        sharedPreferences = getSharedPreferences("NutriPrefs", MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nutrigendaapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(NutritionistServices.class);

        Button registerButton = findViewById(R.id.nutriRegister_registerButton);
        registerButton.setOnClickListener(view -> {
            String name = ((EditText) findViewById(R.id.nutriRegister_nameEditText)).getText().toString();
            String email = ((EditText) findViewById(R.id.nutriRegister_emailEditText)).getText().toString();
            String password = ((EditText) findViewById(R.id.nutriRegister_passwordEditText)).getText().toString();
            String crn = ((EditText) findViewById(R.id.nutriRegister_crnEditText)).getText().toString();

            if (validateInput(name, email, password, crn)) {
                Nutritionist nutritionist = new Nutritionist(name, email, password, crn);
                registerNutritionist(nutritionist);
            }
        });

        Button alreadyAccountButton = findViewById(R.id.nutriRegister_alreadyAccountButton);
        alreadyAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(NutritionistRegisterScreen.this, NutritionistLoginScreen.class);
            startActivity(intent);
        });
    }

    private boolean validateInput(String name, String email, String password, String crn) {
        if (name.isEmpty()) {
            Toast.makeText(this, "Nome não pode estar vazio", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email não pode estar vazio", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, insira um email válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Senha não pode estar vazia", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "Senha deve ter pelo menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(this, "Senha deve conter pelo menos uma letra maiúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(this, "Senha deve conter pelo menos uma letra minúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            Toast.makeText(this, "Senha deve conter pelo menos um número", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            Toast.makeText(this, "Senha deve conter pelo menos um caractere especial", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (crn.isEmpty()) {
            Toast.makeText(this, "CRN não pode estar vazio", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!ALLOWED_CRNS.contains(crn)) {
            Toast.makeText(this, "CRN inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerNutritionist(Nutritionist nutritionist) {
        Call<Void> call = apiService.registerNutritionist(nutritionist);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(NutritionistRegisterScreen.this, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show();
                    attemptLogin(nutritionist.getEmail(), nutritionist.getPassword());
                } else {
                    Toast.makeText(NutritionistRegisterScreen.this, "Erro no registro: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("NutritionistRegister", "Falha na comunicação: " + t.getMessage(), t);
                Toast.makeText(NutritionistRegisterScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptLogin(String email, String password) {
        Nutritionist nutritionist = new Nutritionist(email, password);
        Call<TokenResponse> call = apiService.loginNutritionist(nutritionist);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    String userId = response.body().getUserId();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("TOKEN", token);
                    editor.putString("USER_ID", userId);
                    editor.apply();

                    Intent intent = new Intent(NutritionistRegisterScreen.this, NutritionistPatientListScreen.class);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("TOKEN", token);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(NutritionistRegisterScreen.this, "Erro no login após registro: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Toast.makeText(NutritionistRegisterScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
