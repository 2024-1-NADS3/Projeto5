package com.nutrienviroment.nutrigenda.screens.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.nutritionist.Nutritionist;
import com.nutrienviroment.nutrigenda.models.user.TokenResponse;
import com.nutrienviroment.nutrigenda.models.user.User;
import com.nutrienviroment.nutrigenda.screens.general.ChooseRole;
import com.nutrienviroment.nutrigenda.services.nutritionist.NutritionistServices;
import com.nutrienviroment.nutrigenda.services.user.UserServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRegisterScreen extends AppCompatActivity {
    private UserServices apiService;
    private NutritionistServices nutritionistService;
    private Spinner nutritionistSpinner;
    private EditText editTextName, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_screen);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nutrigendaapi.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(UserServices.class);
        nutritionistService = retrofit.create(NutritionistServices.class);
        nutritionistSpinner = findViewById(R.id.spinnerNutritionist);
        editTextName = findViewById(R.id.editTextText7);
        editTextEmail = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextText2);

        loadNutritionists();

        Button registerButton = findViewById(R.id.button2);
        registerButton.setOnClickListener(view -> {
            if (validateInput()) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                Nutritionist selectedNutritionist = (Nutritionist) nutritionistSpinner.getSelectedItem();

                User user = new User(name, email, password, selectedNutritionist.getId());
                registerUser(user);
            }
        });
    }

    private void loadNutritionists() {
        Call<List<Nutritionist>> call = nutritionistService.getNutritionists();
        call.enqueue(new Callback<List<Nutritionist>>() {
            @Override
            public void onResponse(Call<List<Nutritionist>> call, Response<List<Nutritionist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayAdapter<Nutritionist> adapter = new ArrayAdapter<>(UserRegisterScreen.this,
                            android.R.layout.simple_spinner_item, response.body());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    nutritionistSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(UserRegisterScreen.this, "Erro ao carregar nutricionistas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Nutritionist>> call, Throwable t) {
                Toast.makeText(UserRegisterScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(User user) {
        Call<Void> call = apiService.registerUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserRegisterScreen.this, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show();
                    loginUser(user.getEmail(), user.getPassword());
                } else {
                    Toast.makeText(UserRegisterScreen.this, "Erro no registro: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserRegisterScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        User loginUser = new User(null, email, password, null);
        Call<TokenResponse> call = apiService.loginUser(loginUser);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenResponse tokenResponse = response.body();
                    saveUserCredentials(tokenResponse);
                    navigateToMainScreen();
                } else {
                    Toast.makeText(UserRegisterScreen.this, "Erro ao fazer login: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Toast.makeText(UserRegisterScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserCredentials(TokenResponse tokenResponse) {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .putString("token", tokenResponse.getToken())
                .putString("userId", tokenResponse.getUserId())
                .apply();
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(UserRegisterScreen.this, ChooseRole.class);
        startActivity(intent);
        finish();
    }

    private boolean validateInput() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Nutritionist selectedNutritionist = (Nutritionist) nutritionistSpinner.getSelectedItem();

        if (name.isEmpty()) {
            editTextName.setError("Nome é obrigatório");
            editTextName.requestFocus();
            return false;
        }

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

        if (selectedNutritionist == null) {
            Toast.makeText(this, "Selecione um nutricionista", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
