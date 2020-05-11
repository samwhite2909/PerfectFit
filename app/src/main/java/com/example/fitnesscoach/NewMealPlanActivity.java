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

//This class allows the user to add a new meal into their meal plan [20].
public class NewMealPlanActivity extends AppCompatActivity {
    private EditText editTextMealTitle;
    private EditText editTextMealDesc;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal_plan);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextMealTitle = findViewById(R.id.mealTitleInput);
        editTextMealDesc = findViewById(R.id.mealDescInput);
        numberPicker = findViewById(R.id.numberPicker);

        //Sets the parameters for the number picker.
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_meal_plan_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Saves the meal into the meal plan once save button is pressed.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_meal:
                saveMeal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Adds the meal into the meal plan, based on the user's input.
    private void saveMeal(){
        String mealTitle = editTextMealTitle.getText().toString();
        String mealDesc = editTextMealDesc.getText().toString();
        int priority = numberPicker.getValue();

        //Input validation.
        if(mealTitle.trim().isEmpty()){
            Toast.makeText(NewMealPlanActivity.this, "Please give the meal a title", Toast.LENGTH_SHORT).show();
            return;
        }

        //Creates a reference to the database and the currently logged in user.
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String userID = mFirebaseAuth.getCurrentUser().getUid();

        //Creates a reference to the meal plan collection for a new meal to be added and adds it.
        CollectionReference mealPlanRef = FirebaseFirestore.getInstance()
                .collection("users").document(userID).collection("mealPlans");
        mealPlanRef.add(new MealPlan(mealTitle, mealDesc, priority));
        Toast.makeText(NewMealPlanActivity.this, "Meal added", Toast.LENGTH_SHORT).show();
        finish();
    }

}
