package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ExerciseAddedActivty extends AppCompatActivity {

    Button menuButton;
    Button addMoreButton;
    FirebaseAuth mFirebaseAuth;
    String userID;
    FirebaseFirestore fStore;
    double newRemainingCalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_added_activty);

        final String caloriesBurned = getIntent().getStringExtra("caloriesBurned");

        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null) {
            userID = mFirebaseAuth.getCurrentUser().getUid();

            final DocumentReference documentReference = fStore.collection("users").document(userID);

            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        double remainingCalValueNum = documentSnapshot.getDouble("remainingCalValue");
                        double caloriesBurnedDouble = Double.parseDouble(caloriesBurned);
                        newRemainingCalValue = remainingCalValueNum + caloriesBurnedDouble;
                    }
                }
            });


            menuButton = findViewById(R.id.menuButton);
            addMoreButton = findViewById(R.id.addMoreExercisesButton);

            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    documentReference.update("remainingCalValue", newRemainingCalValue);
                    startActivity(new Intent(ExerciseAddedActivty.this, MenuActivity.class));
                }
            });

            addMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    documentReference.update("remainingCalValue", newRemainingCalValue);
                    startActivity(new Intent(ExerciseAddedActivty.this, SearchExercises.class));
                }
            });
        }
    }

}
