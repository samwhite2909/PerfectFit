package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

//This class gives users a choice about their next action once they've added in a food to their food diary.
public class FoodAddedActivity extends AppCompatActivity {

    Button menuButton;
    Button addMoreButton;
    FirebaseAuth mFirebaseAuth;
    String userID;
    FirebaseFirestore fStore;
    double newRemainingCalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_added);

        menuButton = findViewById(R.id.menuButton);
        addMoreButton = findViewById(R.id.addMoreFoodsButton);
        final String caloriesConsumed = getIntent().getStringExtra("caloriesConsumed");

        //Creates a reference to the database and the currently logged in user.
        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        userID = mFirebaseAuth.getCurrentUser().getUid();

        //Creates a reference to the user's information and creates a new value for their reamining calories based
        //on their previous input.
        final DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                double remainingCalValueNum = documentSnapshot.getDouble("remainingCalValue");
                double caloriesBurnedDouble = Double.parseDouble(caloriesConsumed);
                newRemainingCalValue = remainingCalValueNum - caloriesBurnedDouble;
            }
        });

        //Takes the user back to the main menu, updating their calorie information.
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.update("remainingCalValue", newRemainingCalValue);
                startActivity(new Intent(FoodAddedActivity.this, MenuActivity.class));
            }
        });

        //Takes the user back to the search, updating their calorie information.
        addMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.update("remainingCalValue", newRemainingCalValue);
                startActivity(new Intent(FoodAddedActivity.this, SearchFoods.class));
            }
        });
    }
}
