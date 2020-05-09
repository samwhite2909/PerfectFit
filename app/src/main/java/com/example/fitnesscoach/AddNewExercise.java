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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

//This class allows the user to add in a new exercise into the database.
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

        //Gets a reference to the database.
        fStore = FirebaseFirestore.getInstance();

        //Adds in the exercise to the database, based on the input from the user.
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String exerciseName = editExerciseName.getText().toString();
                String calPerMinString = editCalPerMin.getText().toString();

                if (exerciseName.isEmpty() || calPerMinString.isEmpty()) {
                    Toast.makeText(AddNewExercise.this, "Please give the exercise a name and a value for calories burned per minute", Toast.LENGTH_SHORT).show();
                } else {
                    double calPerMin = Double.parseDouble(editCalPerMin.getText().toString());
                    String search = exerciseName.toLowerCase();

                    //Creates a reference to a new document to add the new exercise into the database and fills it
                    //with the information about that exercise.
                    DocumentReference documentReference = fStore.collection("exercises").document(exerciseName);
                    final Map<String, Object> exercise = new HashMap<>();
                    exercise.put("exerciseName", exerciseName);
                    exercise.put("calPerMin", calPerMin);
                    exercise.put("search", search);
                    documentReference.set(exercise).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Tag", "onSuccess: exercise profile is created for " + exerciseName);
                        }
                    });
                    Intent intent = new Intent(AddNewExercise.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
