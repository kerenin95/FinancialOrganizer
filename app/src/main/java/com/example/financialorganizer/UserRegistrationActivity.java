package com.example.financialorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText email, password;
    private Button registrationBtn;
    private TextView hasAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registrationBtn = findViewById(R.id.registrationBtn);
        hasAccount = findViewById(R.id.hasAccount);

        hasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}