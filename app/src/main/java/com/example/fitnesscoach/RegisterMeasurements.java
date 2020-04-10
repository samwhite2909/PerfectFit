package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterMeasurements extends AppCompatActivity {

    EditText editTextWeight;
    EditText editTextHeight;
    EditText editTextBodyFatLevel;
    Button registerButton;
    FirebaseFirestore fStore;
    FirebaseAuth mFirebaseAuth;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_measurements);

        editTextWeight = findViewById(R.id.weightInput);
        editTextHeight = findViewById(R.id.heightInput);
        editTextBodyFatLevel = findViewById(R.id.bodyFatInput);
        registerButton = findViewById(R.id.registerButton);
        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        final Calendar calendar = Calendar.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double weight = Double.parseDouble(editTextWeight.getText().toString());
                double height = Double.parseDouble(editTextHeight.getText().toString());
                double bodyFatLevel = Double.parseDouble(editTextBodyFatLevel.getText().toString());

                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");
                String gender = getIntent().getStringExtra("gender");
                String ageString = getIntent().getStringExtra("age");
                String weightLossAnswer = getIntent().getStringExtra("weightLossAnswer");
                String supportAnswer = getIntent().getStringExtra("supportAnswer");

                int age = 0;

                if (ageString != null){
                    age = Integer.parseInt(ageString);
                }

                double BMI = weight/(height)*(height);
                double dailyCalLimit = 0;

                if (gender.equalsIgnoreCase("Male")){
                    dailyCalLimit = 10*weight + 6.25*(height*100)-5*age + 5;
                }
                if(gender.equalsIgnoreCase("Female")){
                    dailyCalLimit = 10*weight + 6.25*(height*100)-5*age - 161;
                }

                double weeklyCalLimit = dailyCalLimit*7;

                double calLimitWithReduction = dailyCalLimit;

                if(weightLossAnswer.equalsIgnoreCase("0.5lb")){
                    calLimitWithReduction = dailyCalLimit - 250;
                }
                if(weightLossAnswer.equalsIgnoreCase("1lb")){
                    calLimitWithReduction = dailyCalLimit - 500;
                }
                if(weightLossAnswer.equalsIgnoreCase("1.5lbs")){
                    calLimitWithReduction = dailyCalLimit - 750;
                }

                if(weightLossAnswer.equalsIgnoreCase("2lbs")){
                    calLimitWithReduction = dailyCalLimit - 1000;
                }

                double remainingCalValue = calLimitWithReduction;

                String dateJoined = DateFormat.getDateInstance().format(calendar.getTime());

                userID = mFirebaseAuth.getCurrentUser().getUid();
                 DocumentReference documentReference = fStore.collection("users").document(userID);
                 final Map<String, Object> user = new HashMap<>();
                 user.put("name", name);
                 user.put("email", email);
                 user.put("gender", gender);
                 user.put("age", age);
                 user.put("weightLossAnswer", weightLossAnswer);
                 user.put("supportAnswer", supportAnswer);
                 user.put("weight", weight);
                 user.put("height", height);
                 user.put("bodyFatLevel", bodyFatLevel);
                 user.put("BMI", BMI);
                 user.put("dailyCalLimit", dailyCalLimit);
                 user.put("weeklyCalLimit", weeklyCalLimit);
                 user.put("calLimitWithReduction", calLimitWithReduction);
                 user.put("remainingCalValue", remainingCalValue);
                 user.put("dateJoined", dateJoined);
                 user.put("lastLoginDate", dateJoined);
                 documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                Log.d("Tag", "onSuccess: user profile is created for " + userID);
                }
                });
                Intent intent  = new Intent(RegisterMeasurements.this, MenuActivity.class);
                startActivity(intent);
                //Toast.makeText(RegisterMeasurements.this, "Pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
