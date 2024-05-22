package com.nutrienviroment.nutrigenda.screens.nutritionist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.nutritionist.Nutritionist;
import com.nutrienviroment.nutrigenda.models.user.TokenResponse;
import com.nutrienviroment.nutrigenda.services.nutritionist.NutritionistServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NutritionistLoginScreen extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private NutritionistServices apiService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutritionist_login_screen);
        sharedPreferences = getSharedPreferences("NutriPrefs", MODE_PRIVATE);
        checkIfAlreadyLoggedIn();
        initializeUI();
        setupRetrofit();
    }

    private void checkIfAlreadyLoggedIn() {
        String token = sharedPreferences.getString("TOKEN", null);
        if (token != null) {
            String userId = sharedPreferences.getString("USER_ID", null);
            Intent intent = new Intent(NutritionistLoginScreen.this, NutritionistPatientListScreen.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("TOKEN", token);
            startActivity(intent);
            finish();
        }
    }

    private void initializeUI() {
        emailEditText = findViewById(R.id.nutriLogin_emailEditText);
        passwordEditText = findViewById(R.id.nutriLogin_passwordEditText);
        Button loginButton = findViewById(R.id.nutriLogin_loginButton);

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (validateInput(email, password)) {
                attemptLogin(email, password);
            }
        });
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5136")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(NutritionistServices.class);
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

                    Toast.makeText(NutritionistLoginScreen.this, "Login bem-sucedido!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NutritionistLoginScreen.this, NutritionistPatientListScreen.class);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("TOKEN", token);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(NutritionistLoginScreen.this, "Erro no login: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Toast.makeText(NutritionistLoginScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email e senha são obrigatórios", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
