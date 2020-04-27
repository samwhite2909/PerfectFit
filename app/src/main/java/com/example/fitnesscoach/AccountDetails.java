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

import javax.annotation.Nullable;

public class AccountDetails extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseFirestore fStore;
    TextView name;
    TextView email;
    TextView dateJoined;
    TextView gender;
    TextView BMI;
    TextView age;
    Button updateButton;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        updateButton = findViewById(R.id.updateButton);
        name = findViewById(R.id.nameText);
        email = findViewById(R.id.emailText);
        dateJoined = findViewById(R.id.dateJoinedText);
        gender = findViewById(R.id.genderText);
        BMI = findViewById(R.id.BMIText);
        age = findViewById(R.id.ageText);

        userID = mFirebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                String nameForView = documentSnapshot.getString("name");
                String emailForView = documentSnapshot.getString("email");
                String dateForView = documentSnapshot.getString("dateJoined");
                String genderForView = documentSnapshot.getString("gender");
                double BMIForView = documentSnapshot.getDouble("BMI");
                String BMIText = Double.toString(BMIForView);
                double ageForView = documentSnapshot.getDouble("age");
                int ageInt = (int) Math.round(ageForView);
                String ageText = Integer.toString(ageInt);
                name.setText("Name: " + nameForView);
                email.setText("Email: " + emailForView);
                dateJoined.setText("Date Joined: " + dateForView);
                gender.setText("Gender: " + genderForView);
                BMI.setText("BMI: " + BMIText);
                age.setText("Age: " + ageText);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateMeasurementsIntent = new Intent (AccountDetails.this, UpdateMeasurements.class);
                startActivity(updateMeasurementsIntent);
            }
        });
    }
}
