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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText nameET, passwordET, emailET;
    Button registerBtn;
    TextView loginText;

    FirebaseAuth auth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameET = findViewById(R.id.editTextUserName);
        passwordET = findViewById(R.id.editTextPassword);
        emailET = findViewById(R.id.editTextEmailAddress);
        registerBtn = findViewById(R.id.buttonRegister);
        loginText = findViewById(R.id.textViewToLogin);

        auth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(v -> {
            String username = nameET.getText().toString();
            String password = passwordET.getText().toString();
            String email = emailET.getText().toString();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
                Toast.makeText( RegisterActivity.this, "Please Fill All Fields",Toast.LENGTH_SHORT).show();
            }else {
                registerNow(username, password, email);
            }

        });

        loginText.setOnClickListener(v -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        });
    }

    private void registerNow(String username, String password, String email){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();

                        myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("id",userid);
                        hashMap.put("username",username);
                        hashMap.put("imageURl","default");

                        myRef.setValue(hashMap).addOnCompleteListener(task1 -> {

                            if (task1.isSuccessful()){
                                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }

                        });

                    }else {
                        Toast.makeText( RegisterActivity.this, "Invalid Email or Password",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}