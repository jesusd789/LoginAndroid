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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * Pantalla de registro de nuevos usuarios con Firebase Authentication.
 */
public class Register extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword, tilConfirm;
    private TextInputEditText editEmail, editPassword, editConfirm;
    private MaterialButton btnRegister;
    private ProgressBar progress;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        tilConfirm = findViewById(R.id.til_confirm);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        editConfirm = findViewById(R.id.edit_confirm);
        btnRegister = findViewById(R.id.btn_register);
        progress = findViewById(R.id.progress);
        TextView goLogin = findViewById(R.id.txt_go_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // vuelve a la pantalla de login
            }
        });
    }

    private void registrar() {
        String email = editEmail.getText() == null ? "" : editEmail.getText().toString().trim();
        String password = editPassword.getText() == null ? "" : editPassword.getText().toString();
        String confirm = editConfirm.getText() == null ? "" : editConfirm.getText().toString();

        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirm.setError(null);

        if (email.isEmpty()) {
            tilEmail.setError("Ingresa tu correo electrónico");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("El correo no es válido");
            return;
        }
        if (password.length() < 6) {
            tilPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        if (!password.equals(confirm)) {
            tilConfirm.setError("Las contraseñas no coinciden");
            return;
        }

        mostrarCargando(true);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    mostrarCargando(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Register.this, mensajeDeError(task.getException()), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String mensajeDeError(Exception e) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            return "Ese correo ya está registrado";
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            return "La contraseña es muy débil";
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return "El correo no es válido";
        } else if (e instanceof FirebaseNetworkException) {
            return "Sin conexión a internet";
        }
        return "No se pudo crear la cuenta: " + (e != null ? e.getLocalizedMessage() : "error desconocido");
    }

    private void mostrarCargando(boolean cargando) {
        progress.setVisibility(cargando ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!cargando);
    }
}
