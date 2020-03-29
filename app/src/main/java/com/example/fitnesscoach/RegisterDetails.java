package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterDetails extends AppCompatActivity {

    RadioGroup rg;
    RadioButton rb;
    String gender;
    EditText editTextAge;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        editTextAge = findViewById(R.id.ageInput);
        rg = findViewById(R.id.genderGroup);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");

                String age = editTextAge.getText().toString();


                Intent intent  = new Intent(RegisterDetails.this, RegisterMeasurements.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("age", age);
                intent.putExtra("gender", gender);
                startActivity(intent);
            }
        });
    }

    public void rbClick(View v){
        int radioButtonID =  rg.getCheckedRadioButtonId();
        rb = findViewById(radioButtonID);
        gender = rb.getText().toString();
    }
}
