package com.nutrienviroment.nutrigenda.screens.diet;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.diet.Diet;
import com.nutrienviroment.nutrigenda.models.diet.FoodItem;
import com.nutrienviroment.nutrigenda.models.diet.Meal;
import com.nutrienviroment.nutrigenda.services.diet.DietServices;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateDietScreen extends AppCompatActivity {

    private DietServices dietApiService;
    private RecyclerView rvMeals;
    private MealAdapter mealAdapter;
    private Button btnSaveDiet;
    private Button btnCancel;
    private Button btnAddMeal;
    private String userId;
    private List<Meal> mealList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.diet_create_diet_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nutrigendaapi.azure-api.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dietApiService = retrofit.create(DietServices.class);

        rvMeals = findViewById(R.id.rvMeals);
        rvMeals.setLayoutManager(new LinearLayoutManager(this));

        mealAdapter = new MealAdapter(mealList, new MealAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {

            }

            @Override
            public void onAddFoodItemClick(Meal meal) {
                addFoodItem(meal);
            }
        });
        rvMeals.setAdapter(mealAdapter);

        btnSaveDiet = findViewById(R.id.btnSaveDiet);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddMeal = findViewById(R.id.btnAddMeal);

        userId = getIntent().getStringExtra("USER_ID");

        btnAddMeal.setOnClickListener(v -> showAddMealDialog());
        btnSaveDiet.setOnClickListener(v -> saveDiet());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showAddMealDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.meal_modal);

        Spinner spinnerMealType = dialog.findViewById(R.id.spinnerMealType);
        Button btnSaveMeal = dialog.findViewById(R.id.btnSaveMeal);

        String[] mealTypes = {"Café da Manhã", "Lanche", "Almoço", "Lanche da Tarde", "Jantar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mealTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(adapter);

        btnSaveMeal.setOnClickListener(v -> {
            String selectedMealType = (String) spinnerMealType.getSelectedItem();

            if (selectedMealType != null) {
                Meal meal = new Meal(UUID.randomUUID().toString(), selectedMealType, new ArrayList<>(), userId);
                mealList.add(meal);
                mealAdapter.notifyItemInserted(mealList.size() - 1);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Selecione um tipo de refeição", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void addFoodItem(Meal meal) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.food_item_modal);

        EditText etFoodItemName = dialog.findViewById(R.id.etFoodItemName);
        EditText etFoodItemDescription = dialog.findViewById(R.id.etFoodItemDescription);
        Button btnSaveFoodItem = dialog.findViewById(R.id.btnSaveFoodItem);

        btnSaveFoodItem.setOnClickListener(v -> {
            String name = etFoodItemName.getText().toString();
            String description = etFoodItemDescription.getText().toString();

            if (!name.isEmpty() && !description.isEmpty()) {
                FoodItem foodItem = new FoodItem(UUID.randomUUID().toString(), name, description, meal.getId());
                meal.getFoodItems().add(foodItem);
                mealAdapter.notifyDataSetChanged();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void saveDiet() {
        Diet diet = new Diet(UUID.randomUUID().toString(), userId, mealList);

        dietApiService.createDiet(diet).enqueue(new Callback<Diet>() {
            @Override
            public void onResponse(Call<Diet> call, Response<Diet> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateDietScreen.this, "Dieta criada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateDietScreen.this, "Erro ao criar dieta: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("CreateDietScreen", "Erro ao criar dieta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Diet> call, Throwable t) {
                Toast.makeText(CreateDietScreen.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CreateDietScreen", "Falha na comunicação: " + t.getMessage(), t);
            }
        });
    }
}
