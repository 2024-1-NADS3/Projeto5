package com.nutrienviroment.nutrigenda.ui.authentication.register;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.data.repository.AuthenticationService;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        findViewById(R.id.btnRegister).setOnClickListener(view -> {
            String email = etRegisterEmail.getText().toString().trim();
            String password = etRegisterPassword.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                viewModel.register(email, password);
            } else {
                Toast.makeText(RegisterActivity.this, "Email e senha são obrigatórios.", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.registrationSuccess.observe(this, success -> {
            if (success) {
                Toast.makeText(RegisterActivity.this, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show();
                // Navegar para a próxima tela
            }
        });

        viewModel.registrationError.observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Erro ao registrar: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
