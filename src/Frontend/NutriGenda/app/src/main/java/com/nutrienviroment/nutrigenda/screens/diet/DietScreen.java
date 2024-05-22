package com.nutrienviroment.nutrigenda.screens.diet;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.diet.Diet;
import com.nutrienviroment.nutrigenda.models.diet.FoodItem;
import com.nutrienviroment.nutrigenda.models.diet.Meal;
import com.nutrienviroment.nutrigenda.services.diet.DietServices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DietScreen extends AppCompatActivity {
    private static final String TAG = "DietScreen";
    private DietServices apiService;
    private Map<String, Meal> mealMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_screen);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5136")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(DietServices.class);

        String userId = getIntent().getStringExtra("USER_ID");

        if (userId != null && !userId.isEmpty()) {
            fetchDietPlan(userId);
        } else {
            Toast.makeText(DietScreen.this, "Erro: userId não pode ser nulo ou vazio", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Erro: userId não pode ser nulo ou vazio");
        }
    }

    private void fetchDietPlan(String userId) {
        apiService.getDietsByUserId(userId).enqueue(new Callback<List<Diet>>() {
            @Override
            public void onResponse(Call<List<Diet>> call, Response<List<Diet>> response) {
                if (response.isSuccessful()) {
                    List<Diet> diets = response.body();
                    Log.d(TAG, "Dietas recebidas: " + diets);
                    if (diets != null && !diets.isEmpty()) {
                        Diet diet = diets.get(0);
                        Log.d(TAG, "Dieta selecionada: " + diet);
                        if (diet.getMeals() != null) {
                            Log.d(TAG, "Refeições na dieta: " + diet.getMeals());
                            for (Meal meal : diet.getMeals()) {
                                Log.d(TAG, "Refeição: " + meal.getName() + ", Itens: " + meal.getFoodItems());
                            }
                            initializeButtons(diet.getMeals());
                        } else {
                            Log.e(TAG, "Nenhuma refeição encontrada na dieta");
                            Toast.makeText(DietScreen.this, "Nenhuma refeição encontrada na dieta", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Nenhuma dieta ativa encontrada");
                        Toast.makeText(DietScreen.this, "Nenhuma dieta ativa encontrada", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "Dados carregados com sucesso! Dietas: " + diets);
                } else {
                    Log.e(TAG, "Erro ao carregar dados: " + response.message());
                    Toast.makeText(DietScreen.this, "Erro ao carregar dados: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Diet>> call, Throwable t) {
                Log.e(TAG, "Falha na comunicação: " + t.getMessage(), t);
                Toast.makeText(DietScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initializeButtons(List<Meal> meals) {
        if (meals == null) {
            Toast.makeText(this, "A lista de refeições está vazia", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Meal meal : meals) {
            Log.d(TAG, "Inicializando botão para refeição: " + meal.getName());
            mealMap.put(meal.getName(), meal);
        }

        Button btnBreakfast = findViewById(R.id.diet_breakfastButton);
        Button btnSnack = findViewById(R.id.diet_snackButton);
        Button btnLunch = findViewById(R.id.diet_lunchButton);
        Button btnAfternoonSnack = findViewById(R.id.diet_afternoonSnackButton);
        Button btnDinner = findViewById(R.id.diet_dinnerButton);

        setButtonVisibility(btnBreakfast, "Café da Manhã");
        setButtonVisibility(btnSnack, "Lanche");
        setButtonVisibility(btnLunch, "Almoço");
        setButtonVisibility(btnAfternoonSnack, "Lanche da Tarde");
        setButtonVisibility(btnDinner, "Jantar");

        btnBreakfast.setOnClickListener(v -> showFoodItemsModal("Café da Manhã"));
        btnSnack.setOnClickListener(v -> showFoodItemsModal("Lanche"));
        btnLunch.setOnClickListener(v -> showFoodItemsModal("Almoço"));
        btnAfternoonSnack.setOnClickListener(v -> showFoodItemsModal("Lanche da Tarde"));
        btnDinner.setOnClickListener(v -> showFoodItemsModal("Jantar"));
    }


    private void setButtonVisibility(Button button, String mealName) {
        if (mealMap.containsKey(mealName)) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    private void showFoodItemsModal(String mealName) {
        Meal meal = mealMap.get(mealName);
        if (meal == null) {
            Toast.makeText(this, "Nenhuma refeição encontrada para " + mealName, Toast.LENGTH_SHORT).show();
            return;
        }

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.meal_food_items_modal);

        TextView tvMealName = dialog.findViewById(R.id.tvMealName);
        RecyclerView rvFoodItems = dialog.findViewById(R.id.rvFoodItems);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        Log.d(TAG, "Mostrando modal para refeição: " + meal.getName() + ", Itens: " + meal.getFoodItems());
        tvMealName.setText(meal.getName());
        rvFoodItems.setLayoutManager(new LinearLayoutManager(this));
        rvFoodItems.setAdapter(new FoodItemAdapter(meal.getFoodItems()));

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}
