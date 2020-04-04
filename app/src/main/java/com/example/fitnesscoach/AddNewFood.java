package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNewFood extends AppCompatActivity {

    EditText editFoodName;
    EditText editCalPer100g;
    Button addFoodButton;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);

        editFoodName = findViewById(R.id.nameInput);
        editCalPer100g = findViewById(R.id.calPer100gInput);
        addFoodButton = findViewById(R.id.addFoodButton);

        fStore = FirebaseFirestore.getInstance();

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String foodName = editFoodName.getText().toString();
                double calPer100g = Double.parseDouble(editCalPer100g.getText().toString());

                DocumentReference documentReference = fStore.collection("foods").document(foodName);
                final Map<String, Object> food = new HashMap<>();
                food.put("foodName", foodName);
                food.put("calPer100g", calPer100g);
                documentReference.set(food).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "onSuccess: food profile is created for " + foodName);
                    }
                });
                Intent intent  = new Intent(AddNewFood.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
