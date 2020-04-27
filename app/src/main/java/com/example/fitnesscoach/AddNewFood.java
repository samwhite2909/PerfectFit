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

public class AddNewFood extends AppCompatActivity {

    EditText editFoodName;
    EditText editCalPer100g;
    Button addFoodButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);

        editFoodName = findViewById(R.id.nameInput);
        editCalPer100g = findViewById(R.id.calPer100gInput);
        addFoodButton = findViewById(R.id.addFoodButton);


        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String foodName = editFoodName.getText().toString();
                String calPer100g = editCalPer100g.getText().toString();

                if(editFoodName.getText().toString().isEmpty() || editCalPer100g.getText().toString().isEmpty()){
                    Toast.makeText(AddNewFood.this, "Please fill out the required fields", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(AddNewFood.this, AddNewFoodSecondStage.class);
                    intent.putExtra("foodName", foodName);
                    intent.putExtra("calPer100g", calPer100g);
                    startActivity(intent);
                }
            }
        });
    }
}
