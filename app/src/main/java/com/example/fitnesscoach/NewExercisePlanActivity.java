package com.example.fitnesscoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

//This class allows the user to add in new exercises into their workout plan [20].
public class NewExercisePlanActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextTarget;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise_plan);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextTitle = findViewById(R.id.exerciseTitleInput);
        editTextTarget = findViewById(R.id.exerciseTargetInput);
        numberPickerPriority = findViewById(R.id.numberPicker);

        //Setting parameters for the number picker used.
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_exercise_plan_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Saves the exercise into the workout plan once save button is pressed.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveExercisePlan:
                saveExercise();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Takes the exercise's data given by the user and saves it into the user's workout plan.
    private void saveExercise(){
        String exerciseTitle = editTextTitle.getText().toString();
        String exerciseTarget = editTextTarget.getText().toString();
        int priority = numberPickerPriority.getValue();

        if(exerciseTitle.trim().isEmpty() || exerciseTarget.isEmpty()){
            Toast.makeText(NewExercisePlanActivity.this, "Please give the exercise a title and a target for it", Toast.LENGTH_SHORT).show();
            return;
        }

        //Creates a reference to the database and the currently logged in user.
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String userID = mFirebaseAuth.getCurrentUser().getUid();

        //Gives the reference for the exercise to be added into the workout plan.
        CollectionReference exercisePlanRef = FirebaseFirestore.getInstance()
                .collection("users").document(userID).collection("exercisePlans");
        exercisePlanRef.add(new ExercisePlan(exerciseTitle, exerciseTarget, priority));
        Toast.makeText(NewExercisePlanActivity.this, "Exercise added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
