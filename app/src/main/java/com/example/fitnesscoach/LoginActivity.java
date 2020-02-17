package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText nameText;
    private EditText passwordText;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        nameText = findViewById(R.id.usernameInput);
        passwordText = findViewById(R.id.passwordInput);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    username = nameText.getText().toString();
                    password = passwordText.getText().toString();
                    if(username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("password")) {
                        openMenuActivity();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                  "Incorrect login details", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }

    public void openMenuActivity() {
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
    }
}
