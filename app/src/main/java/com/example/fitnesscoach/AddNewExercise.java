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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class AddNewExercise extends AppCompatActivity {

    EditText editExerciseName;
    EditText editCalPerMin;
    Button addExerciseButton;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_exercise);

        editExerciseName = findViewById(R.id.nameInput);
        editCalPerMin = findViewById(R.id.calPerMinInput);
        addExerciseButton = findViewById(R.id.addExerciseButton);

        fStore = FirebaseFirestore.getInstance();

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String exerciseName = editExerciseName.getText().toString();
                double calPerMin = Double.parseDouble(editCalPerMin.getText().toString());

                DocumentReference documentReference = fStore.collection("exercises").document(exerciseName);
                final Map<String, Object> exercise = new HashMap<>();
                exercise.put("exerciseName", exerciseName);
                exercise.put("calPerMin", calPerMin);
                documentReference.set(exercise).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "onSuccess: exercise profile is created for " + exerciseName);
                    }
                });
                Intent intent  = new Intent(AddNewExercise.this, MenuActivity.class);
                startActivity(intent);
            }
        });

    }
}
