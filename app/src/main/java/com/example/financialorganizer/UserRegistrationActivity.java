package com.example.financialorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText email, password;
    private Button registrationBtn;
    private TextView hasAccount;

    private FirebaseAuth fAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registrationBtn = findViewById(R.id.registrationBtn);
        hasAccount = findViewById(R.id.hasAccount);

        fAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        hasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        hasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInput = email.getText().toString();
                String passwordInput = password.getText().toString();

                if (TextUtils.isEmpty(emailInput)){
                    email.setError("Email is required");
                }
                if (TextUtils.isEmpty(passwordInput)){
                    password.setError("Password is required");
                }
                else {

                    progressDialog.setMessage("registration in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    fAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(UserRegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            }else {
                                Toast.makeText(UserRegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }
}