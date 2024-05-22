package com.nutrienviroment.nutrigenda.screens.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.nutrienviroment.nutrigenda.R;

public class UserChooseScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_choose_screen);

        Button createAccountButton = findViewById(R.id.button);
        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserChooseScreen.this, UserRegisterScreen.class);
            startActivity(intent);
        });

        Button alreadyAccountButton = findViewById(R.id.button1);
        alreadyAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserChooseScreen.this, UserLoginScreen.class);
            startActivity(intent);
        });
    }
}
