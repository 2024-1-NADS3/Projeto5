package com.nutrienviroment.nutrigenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nutrienviroment.nutrigenda.ui.authentication.register.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verifica o status de login do usuário
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Se não estiver logado, redireciona para a tela de registro
        if (currentUser == null) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish(); // Encerra MainActivity para que o usuário não possa voltar a ela pressionando o botão Voltar
        } else {
            // O usuário já está logado. Você pode direcioná-lo para a tela principal do app.
            // Por exemplo: mostrar a tela principal ou realizar outras checagens.
        }
    }
}