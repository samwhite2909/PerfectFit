package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class RegisterMeasurements extends AppCompatActivity {

    EditText editTextWeight;
    EditText editTextHeight;
    EditText editTextBodyFatLevel;
    Button registerButton;
    FirebaseFirestore fStore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_measurements);

        editTextWeight = findViewById(R.id.usernameInput);
        editTextHeight = findViewById(R.id.passwordInput);
        editTextBodyFatLevel = findViewById(R.id.nameInput);
        registerButton = findViewById(R.id.registerButton);
        fStore = FirebaseFirestore.getInstance();

        final Calendar calendar = Calendar.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weightString = editTextWeight.getText().toString();
                String heightString = editTextHeight.getText().toString();
                String bodyFatLevelString = editTextBodyFatLevel.getText().toString();

                double weight = Double.parseDouble(weightString);
                double height = Double.parseDouble(heightString);
                double bodyFatLevel = Double.parseDouble(bodyFatLevelString);

                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");
                String gender = getIntent().getStringExtra("gender");
                String ageString = getIntent().getStringExtra("age");

                if (ageString != null){
                    int age = Integer.parseInt(ageString);
                }

                double BMI = weight/(height)*(height);

                String dateJoined = DateFormat.getDateInstance().format(calendar.getTime());
            }
        });
    }
}
