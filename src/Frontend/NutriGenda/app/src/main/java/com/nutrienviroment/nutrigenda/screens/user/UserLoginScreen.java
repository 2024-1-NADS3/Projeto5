package com.nutrienviroment.nutrigenda.screens.user;

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
import com.nutrienviroment.nutrigenda.models.user.TokenResponse;
import com.nutrienviroment.nutrigenda.models.user.User;
import com.nutrienviroment.nutrigenda.screens.diet.DietScreen;
import com.nutrienviroment.nutrigenda.services.user.UserServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLoginScreen extends AppCompatActivity {
    private UserServices apiService;
    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_screen);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nutrigendaapi.azure-api.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(UserServices.class);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String savedToken = sharedPreferences.getString("token", null);
        String savedUserId = sharedPreferences.getString("userId", null);
        if (savedToken != null && savedUserId != null) {
            navigateToDietScreen(savedToken, savedUserId);
            finish();
            return;
        }

        editTextEmail = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextText2);

        Button loginButton = findViewById(R.id.button2);
        loginButton.setOnClickListener(view -> {
            if (validateInput()) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                User user = new User(email, password);
                loginUser(user);
            }
        });
    }

    private void loginUser(User user) {
        Call<TokenResponse> call = apiService.loginUser(user);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    String userId = response.body().getUserId();
                    Toast.makeText(UserLoginScreen.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

                    saveUserCredentials(token, userId);
                    navigateToDietScreen(token, userId);
                    finish();
                } else {
                    String errorMessage = "Erro no login: " + response.message();
                    Toast.makeText(UserLoginScreen.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("Erro", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Toast.makeText(UserLoginScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Erro", "Falha na comunicação", t);
            }
        });
    }

    private void saveUserCredentials(String token, String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("userId", userId);
        editor.apply();
    }

    private void navigateToDietScreen(String token, String userId) {
        Intent intent = new Intent(UserLoginScreen.this, DietScreen.class);
        intent.putExtra("EXTRA_SESSION_TOKEN", token);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private boolean validateInput() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email inválido");
            editTextEmail.requestFocus();
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            editTextPassword.setError("Senha deve ter pelo menos 6 caracteres");
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }
}
