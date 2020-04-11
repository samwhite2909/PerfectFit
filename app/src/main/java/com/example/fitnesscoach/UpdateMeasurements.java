package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UpdateMeasurements extends AppCompatActivity {

    EditText editTextWeight;
    EditText editTextHeight;
    EditText editTextBodyFatLevel;
    Button updateButton;
    String userID;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;
    double age;
    String gender;
    String weightLossAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_measurements);

        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        editTextWeight = findViewById(R.id.weightInput);
        editTextHeight = findViewById(R.id.heightInput);
        editTextBodyFatLevel = findViewById(R.id.bodyFatInput);
        updateButton = findViewById(R.id.updateButton);

        userID = mFirebaseAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                double weightForView = documentSnapshot.getDouble("weight");
                double heightForView = documentSnapshot.getDouble("height");
                double fatLevelForView = documentSnapshot.getDouble("bodyFatLevel");
                String weightText = Double.toString(weightForView);
                String heightText = Double.toString(heightForView);
                String fatLevelText = Double.toString(fatLevelForView);

                editTextWeight.setText(weightText);
                editTextHeight.setText(heightText);
                editTextBodyFatLevel.setText(fatLevelText);

                double oldBMI = documentSnapshot.getDouble("BMI");
                double oldDailyCalLimit = documentSnapshot.getDouble("dailyCalLimit");
                double oldWeeklyCalLimit = documentSnapshot.getDouble("weeklyCalLimit");
                double oldCalLimitWithReduction = documentSnapshot.getDouble("calLimitWithReduction");

                gender = documentSnapshot.getString("gender");
                age = documentSnapshot.getDouble("age");
                weightLossAnswer = documentSnapshot.getString("weightLossAnswer");

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newWeightString = editTextWeight.getText().toString();
                        String newHeightString = editTextHeight.getText().toString();
                        String newFatLevelString = editTextBodyFatLevel.getText().toString();

                        double newWeight = Double.parseDouble(newWeightString);
                        double newHeight = Double.parseDouble(newHeightString);
                        double newFatLevel = Double.parseDouble(newFatLevelString);

                        double newBMI = newWeight/(newHeight)*(newHeight);

                        double newDailyCalLimit = 0;

                        if (gender.equalsIgnoreCase("Male")){
                            newDailyCalLimit = 10*newWeight + 6.25*(newHeight*100)-5*age + 5;
                        }
                        if(gender.equalsIgnoreCase("Female")){
                            newDailyCalLimit = 10*newWeight + 6.25*(newHeight*100)-5*age - 161;
                        }

                        double newWeeklyCalLimit = newDailyCalLimit*7;

                        double newCalLimitWithReduction = newDailyCalLimit;

                        if(weightLossAnswer.equalsIgnoreCase("0.5lb")){
                            newCalLimitWithReduction = newDailyCalLimit - 250;
                        }
                        if(weightLossAnswer.equalsIgnoreCase("1lb")){
                            newCalLimitWithReduction = newDailyCalLimit - 500;
                        }
                        if(weightLossAnswer.equalsIgnoreCase("1.5lbs")){
                            newCalLimitWithReduction = newDailyCalLimit  - 750;
                        }

                        if(weightLossAnswer.equalsIgnoreCase("2lbs")){
                            newCalLimitWithReduction = newDailyCalLimit  - 1000;
                        }

                        documentReference.update("weight", newWeight);
                        documentReference.update("height", newHeight);
                        documentReference.update("bodyFatLevel", newFatLevel);

                        documentReference.update("BMI", newBMI);
                        documentReference.update("dailyCalLimit", newDailyCalLimit);
                        documentReference.update("weeklyCalLimit", newWeeklyCalLimit);
                        documentReference.update("calLimitWithReduction", newCalLimitWithReduction);
                        documentReference.update("remainingCalValue", newCalLimitWithReduction);

                        Intent i = new Intent(UpdateMeasurements.this, MenuActivity.class);
                        startActivity(i);
                    }
                });

            }
        });

    }
}
