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
//This class provides functionality for the account details UI.
public class AccountDetails extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
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

        //Gets a reference of the Firestore database and the authorised user on the app.
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

        //Creates a reference the logged in user's document within the user collection for access to information.
        DocumentReference documentReference = fStore.collection("users").document(userID);

        //Gets the values to be displayed to user of the details the app currently holds on them.
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                String nameForView = documentSnapshot.getString("name");
                String emailForView = documentSnapshot.getString("email");
                String dateForView = documentSnapshot.getString("dateJoined");
                String genderForView = documentSnapshot.getString("gender");
                double BMIForView = documentSnapshot.getDouble("BMI");
                int BMIInt = (int) Math.round(BMIForView);
                String BMIText = Double.toString(BMIInt);
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

        //Takes the user to the activity for updating their measurements.
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateMeasurementsIntent = new Intent (AccountDetails.this, UpdateMeasurements.class);
                startActivity(updateMeasurementsIntent);
            }
        });
    }
}
