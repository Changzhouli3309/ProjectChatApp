package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailETlogin, passwordETlogin;
    Button loginBtn;
    TextView registerText;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailETlogin = findViewById(R.id.editTextTextEmailAddress);
        passwordETlogin = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        registerText = findViewById(R.id.textViewToRegister);

        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> {
            String email = emailETlogin.getText().toString();
            String password = passwordETlogin.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText( LoginActivity.this, "Please Fill All Fields",Toast.LENGTH_SHORT).show();
            } else {
                loginNow(email, password);
            }
        });

        registerText.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(i);
        });
    }

    private void loginNow(String email, String password){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }else {
                Toast.makeText( LoginActivity.this, "Login Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}