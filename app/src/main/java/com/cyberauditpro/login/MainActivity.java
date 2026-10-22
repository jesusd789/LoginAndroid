package com.cyberauditpro.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 * Pantalla de inicio de sesión (pantalla principal de la app).
 * Usa Firebase Authentication con correo y contraseña.
 */
public class MainActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText editEmail, editPassword;
    private MaterialButton btnLogin;
    private ProgressBar progress;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        progress = findViewById(R.id.progress);
        TextView goRegister = findViewById(R.id.txt_go_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Si ya hay una sesión activa, entra directo al Home
        if (auth.getCurrentUser() != null) {
            irAlHome();
        }
    }

    private void iniciarSesion() {
        String email = editEmail.getText() == null ? "" : editEmail.getText().toString().trim();
        String password = editPassword.getText() == null ? "" : editPassword.getText().toString();

        tilEmail.setError(null);
        tilPassword.setError(null);

        if (email.isEmpty()) {
            tilEmail.setError("Ingresa tu correo electrónico");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("El correo no es válido");
            return;
        }
        if (password.isEmpty()) {
            tilPassword.setError("Ingresa tu contraseña");
            return;
        }

        mostrarCargando(true);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    mostrarCargando(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        irAlHome();
                    } else {
                        Exception e = task.getException();
                        Toast.makeText(MainActivity.this, mensajeDeError(e), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String mensajeDeError(Exception e) {
        if (e instanceof FirebaseAuthInvalidUserException
                || e instanceof FirebaseAuthInvalidCredentialsException) {
            return "Correo o contraseña incorrectos";
        } else if (e instanceof FirebaseNetworkException) {
            return "Sin conexión a internet";
        }
        return "No se pudo iniciar sesión: " + (e != null ? e.getLocalizedMessage() : "error desconocido");
    }

    private void mostrarCargando(boolean cargando) {
        progress.setVisibility(cargando ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!cargando);
    }

    private void irAlHome() {
        Intent intent = new Intent(MainActivity.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
