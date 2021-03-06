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

//This class adds in a food to the database.
public class AddNewFoodSecondStage extends AppCompatActivity {

    EditText editTextBarcode;
    EditText editTextPortionSize;
    Button addFoodButton;

    //Creates a reference to the database.
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_second_stage);

        editTextBarcode = findViewById(R.id.barcodeInput);
        editTextPortionSize = findViewById(R.id.portionSizeInput);
        addFoodButton = findViewById(R.id.addFoodButton);

        //Gets the information from the previous activity attached to the intent.
        final String foodName = getIntent().getStringExtra("foodName");
        String calPer100gString = getIntent().getStringExtra("calPer100g");
        final double calPer100g = Double.parseDouble(calPer100gString);
        final String search = foodName.toLowerCase();

        //Adds the food into the database, based on user input once validated.
       addFoodButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                if(editTextBarcode.getText().toString().isEmpty() || editTextPortionSize.getText().toString().isEmpty()){
                    Toast.makeText(AddNewFoodSecondStage.this, "Please fill out the required fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    String barcode = editTextBarcode.getText().toString();
                    double portionSize = Double.parseDouble(editTextPortionSize.getText().toString());
                    double calPerPortion = (calPer100g / 100) * portionSize;

                    //Creates a reference to a new document for the food, and fills it with the data given.
                    DocumentReference documentReference = fStore.collection("foods").document(barcode);
                    final Map<String, Object> food = new HashMap<>();
                    food.put("foodName", foodName);
                    food.put("calPer100g", calPer100g);
                    food.put("portionSize", portionSize);
                    food.put("calPerPortion", calPerPortion);
                    food.put("barcode", barcode);
                    food.put("search", search);
                    documentReference.set(food).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Tag", "onSuccess: food profile is created for " + foodName);
                            Toast.makeText(AddNewFoodSecondStage.this, "Food added to database", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent i = new Intent(AddNewFoodSecondStage.this, MenuActivity.class);
                    startActivity(i);
                }
           }
       });
    }
}
