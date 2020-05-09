package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//This class allows a user to add a food into their food diary.
public class AddFoodToUser extends AppCompatActivity {

    EditText editTextAmount;
    Button addFoodButton;
    TextView textViewFoodName;
    TextView textViewPortionSize;
    TextView textViewCalPerPortion;

    //Gets a reference to the database and the currently logged in user.
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String userID;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_to_user);

        final String foodName  = getIntent().getStringExtra("foodName");
        final String calPerPortion = getIntent().getStringExtra("calPerPortion");
        final String portionSize = getIntent().getStringExtra("portionSize");

        textViewFoodName = findViewById(R.id.titleTextView);
        textViewPortionSize = findViewById(R.id.portionTextView);
        textViewCalPerPortion = findViewById(R.id.calTextView);

        textViewFoodName.setText(foodName);
        textViewCalPerPortion.setText("Calories per portion: " + calPerPortion);
        textViewPortionSize.setText("Average portion size: " + portionSize + "g");

        editTextAmount = findViewById(R.id.foodAmountInput);
        addFoodButton = findViewById(R.id.addFoodButton);

        //Adds the food into the user's food diary for the day, passes the information to be added in the next activity.
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = editTextAmount.getText().toString();
                if (amountString.isEmpty()) {
                    Toast.makeText(AddFoodToUser.this, "Please add an amount eaten", Toast.LENGTH_SHORT).show();
                } else {
                    double amount = Double.parseDouble(amountString);
                    double calPerPortionNum = Double.parseDouble(calPerPortion);
                    double caloriesConsumed = amount * calPerPortionNum;
                    String caloriesConsumedString = Double.toString(caloriesConsumed);
                    double portionSizeDouble = Double.parseDouble(portionSize);
                    double amountConsumed = amount * portionSizeDouble;

                    userID = mFirebaseAuth.getCurrentUser().getUid();

                    //Creates a reference to a document for this food to be added into and adds it in.
                    DocumentReference documentReference = fStore.collection("users").document(userID).collection("foods").document();
                    final Map<String, Object> food = new HashMap<>();
                    food.put("foodName", foodName);
                    food.put("caloriesConsumed", caloriesConsumed);
                    food.put("amountConsumed", amountConsumed);
                    documentReference.set(food).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Tag", "food added for user: " + userID);
                        }
                    });
                    Intent intent = new Intent(AddFoodToUser.this, FoodAddedActivity.class);
                    intent.putExtra("caloriesConsumed", caloriesConsumedString);
                    startActivity(intent);
                    finish();
                }
            }
        });



    }
}
