package com.example.fitnesscoach;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class AddExerciseToUser extends AppCompatActivity {
    TextView textViewExerciseName;
    TextView textViewCalPerMin;
    EditText editTextDuration;
    Button addExerciseButton;

    FirebaseFirestore fStore;
    FirebaseAuth mFirebaseAuth;
    String userID;

    EventListener<DocumentSnapshot> listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_to_user);

        final String exerciseNameString = getIntent().getStringExtra("exerciseName");
        String calPerMinString = getIntent().getStringExtra("calPerMin");

        final double calPerMin = Double.parseDouble(calPerMinString);

        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        textViewExerciseName = findViewById(R.id.titleTextView);
        textViewCalPerMin = findViewById(R.id.calTextView);
        editTextDuration = findViewById(R.id.exerciseDurationInput);
        addExerciseButton = findViewById(R.id.addExerciseButton);

        textViewExerciseName.setText(exerciseNameString);
        textViewCalPerMin.setText("Calories burned per minute: " + calPerMinString);

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String durationString  = editTextDuration.getText().toString();
                double duration = Double.parseDouble(durationString);

                final double caloriesBurned = calPerMin*duration;
                String caloriesBurnedString  = Double.toString(caloriesBurned);

                userID = mFirebaseAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fStore.collection("users").document(userID).collection("exercises").document();
                final Map<String, Object> exercise = new HashMap<>();
                exercise.put("exerciseName", exerciseNameString);
                exercise.put("duration", duration);
                exercise.put("caloriesBurned",caloriesBurned);
                documentReference.set(exercise).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "exercise added for user: " + userID);
                    }
                });
                Intent intent  = new Intent(AddExerciseToUser.this, ExerciseAddedActivty.class);
                intent.putExtra("caloriesBurned", caloriesBurnedString);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
