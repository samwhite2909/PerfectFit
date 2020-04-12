package com.example.fitnesscoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextName;
    Button registerButton;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.usernameInput);
        editTextPassword = findViewById(R.id.passwordInput);
        editTextName = findViewById(R.id.nameInput);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                final String name = editTextName.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Enter a username", Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Enter an email", Toast.LENGTH_SHORT).show();
                }
                else if(password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Enter a password", Toast.LENGTH_SHORT).show();
                }

                else if(!(email.isEmpty() && password.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Sign up unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intent  = new Intent(RegisterActivity.this, RegisterQuestions.class);
                                intent.putExtra("email", email);
                                intent.putExtra("name", name);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
