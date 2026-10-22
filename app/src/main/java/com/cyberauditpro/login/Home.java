package com.cyberauditpro.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Pantalla de inicio (Home). Solo se puede ver con una sesión iniciada.
 */
public class Home extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            irAlLogin();
            return;
        }

        TextView txtEmail = findViewById(R.id.txt_email);
        txtEmail.setText(user.getEmail());

        View.OnClickListener enDesarrollo = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "Módulo en desarrollo", Toast.LENGTH_SHORT).show();
            }
        };
        findViewById(R.id.card_audits).setOnClickListener(enDesarrollo);
        findViewById(R.id.card_findings).setOnClickListener(enDesarrollo);
        findViewById(R.id.card_reports).setOnClickListener(enDesarrollo);

        MaterialButton btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                irAlLogin();
            }
        });
    }

    private void irAlLogin() {
        Intent intent = new Intent(Home.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
