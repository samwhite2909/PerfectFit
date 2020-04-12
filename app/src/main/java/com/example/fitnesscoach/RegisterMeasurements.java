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
    int weightLossReductionCals;
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

                String weightString = editTextWeight.getText().toString();
                String heightString = editTextHeight.getText().toString();
                String bodyFatString = editTextBodyFatLevel.getText().toString();

                if (weightString.isEmpty() || heightString.isEmpty() || bodyFatString.isEmpty()) {
                    Toast.makeText(RegisterMeasurements.this, "Please enter all required measurements", Toast.LENGTH_SHORT).show();
                } else {
                    double weight = Double.parseDouble(weightString);
                    double height = Double.parseDouble(heightString);
                    double bodyFatLevel = Double.parseDouble(bodyFatString);

                    String name = getIntent().getStringExtra("name");
                    String email = getIntent().getStringExtra("email");
                    String gender = getIntent().getStringExtra("gender");
                    String ageString = getIntent().getStringExtra("age");
                    String weightLossAnswer = getIntent().getStringExtra("weightLossAnswer");
                    String supportAnswer = getIntent().getStringExtra("supportAnswer");

                    int age = 0;

                    if (ageString != null) {
                        age = Integer.parseInt(ageString);
                    }

                    double BMI = weight / (height) * (height);
                    double dailyCalLimit = 0;

                    if (gender.equalsIgnoreCase("Male")) {
                        dailyCalLimit = 10 * weight + 6.25 * (height * 100) - 5 * age + 5;
                    }
                    if (gender.equalsIgnoreCase("Female")) {
                        dailyCalLimit = 10 * weight + 6.25 * (height * 100) - 5 * age - 161;
                    }

                    double weeklyCalLimit = dailyCalLimit * 7;

                    double weightLossCals = 0;
                    int weightLossCounter = 7;

                    double calLimitWithReduction = dailyCalLimit;

                    if (weightLossAnswer.equalsIgnoreCase("0.5lb")) {
                        calLimitWithReduction = dailyCalLimit - 250;
                        weightLossReductionCals = 250;
                    }
                    if (weightLossAnswer.equalsIgnoreCase("1lb")) {
                        calLimitWithReduction = dailyCalLimit - 500;
                        weightLossReductionCals = 500;
                    }
                    if (weightLossAnswer.equalsIgnoreCase("1.5lbs")) {
                        calLimitWithReduction = dailyCalLimit - 750;
                        weightLossReductionCals = 750;
                    }

                    if (weightLossAnswer.equalsIgnoreCase("2lbs")) {
                        calLimitWithReduction = dailyCalLimit - 1000;
                        weightLossReductionCals = 1000;
                    }

                    double remainingCalValue = calLimitWithReduction;
                    int score = 0;

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
                    DocumentReference documentReferenceLeaderboard = fStore.collection("leaderboardScore").document(userID);
                    final Map<String, Object> leaderboardPlace = new HashMap<>();
                    leaderboardPlace.put("name", name);
                    leaderboardPlace.put("score", score);
                    documentReferenceLeaderboard.set(leaderboardPlace).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Tag", "onSuccess: leaderboard profile is created for " + userID);
                        }
                    });
                    Intent intent = new Intent(RegisterMeasurements.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
