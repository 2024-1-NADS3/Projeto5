package com.nutrienviroment.nutrigenda.screens.diet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.diet.Meal;

import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<Meal> mealList;
    private OnMealClickListener onMealClickListener;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
        void onAddFoodItemClick(Meal meal);
    }

    public MealAdapter(List<Meal> mealList, OnMealClickListener onMealClickListener) {
        this.mealList = mealList != null ? mealList : new ArrayList<>();
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.tvMealName.setText(meal.getName());
        holder.itemView.setOnClickListener(v -> onMealClickListener.onMealClick(meal));

        FoodItemAdapter foodItemAdapter = new FoodItemAdapter(meal.getFoodItems());
        holder.rvFoodItems.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvFoodItems.setAdapter(foodItemAdapter);

        holder.btnAddFoodItem.setOnClickListener(v -> onMealClickListener.onAddFoodItemClick(meal));
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {

        TextView tvMealName;
        RecyclerView rvFoodItems;
        Button btnAddFoodItem;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            rvFoodItems = itemView.findViewById(R.id.rvFoodItems);
            btnAddFoodItem = itemView.findViewById(R.id.btnAddFoodItem);
        }
    }
}
