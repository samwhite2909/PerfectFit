package com.example.fitnesscoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Calendar;

import javax.annotation.Nullable;

public class MenuActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseFirestore fStore;
    String userID;
    String supportAnswer;
    final Calendar calendar = Calendar.getInstance();
    String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
    String lastLoginDate;
    double dailyCalLimitReduction;
    double remainingCal;
    double dailyCalLimit;
    double score;
    CollectionReference foodRef;
    CollectionReference exerciseRef;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


            fStore = FirebaseFirestore.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();


            MaterialToolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            final BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

            if(mFirebaseAuth.getCurrentUser() != null) {
                userID = mFirebaseAuth.getCurrentUser().getUid();
                foodRef = fStore.collection("users").document(userID).collection("foods");
                exerciseRef = fStore.collection("users").document(userID).collection("exercises");


                final DocumentReference documentReference = fStore.collection("users").document(userID);
                final DocumentReference documentReferenceScore = fStore.collection("leaderboardScore").document(userID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            supportAnswer = documentSnapshot.getString("supportAnswer");
                            dailyCalLimitReduction = documentSnapshot.getDouble("calLimitWithReduction");
                            remainingCal = documentSnapshot.getDouble("remainingCalValue");
                            dailyCalLimit = documentSnapshot.getDouble("dailyCalLimit");
                            if (supportAnswer.equalsIgnoreCase("No")) {
                                bottomNav.getMenu().removeItem(R.id.nav_support);
                            }
                            lastLoginDate = documentSnapshot.getString("lastLoginDate");
                            if (!lastLoginDate.equals(currentDate)) {

                                documentReferenceScore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                        score = documentSnapshot.getDouble("score");


                                        if(remainingCal > 125 && remainingCal <= 250){
                                            documentReferenceScore.update("score", score + (250 - remainingCal));
                                        }

                                        if(remainingCal <= 125){
                                            documentReferenceScore.update("score", score + (250 - remainingCal)*2);
                                        }
                            }
                        });
                                documentReference.update("remainingCalValue", dailyCalLimitReduction);
                                documentReference.update("lastLoginDate", currentDate);
                                deleteFoodCollection();
                                deleteExerciseCollection();
                            }
                        }
                    }
                });
            }
            else{
                finish();
            }
            bottomNav.setOnNavigationItemSelectedListener(navListener);

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                    new HomeFragment()).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutItem:
                openMainActivity();
                FirebaseAuth.getInstance().signOut();
                return true;

            case R.id.accountItem:
                openAccountActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openMainActivity() {
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
    }

    public void openAccountActivity() {
        Intent accountIntent = new Intent(this, AccountDetails.class);
        startActivity(accountIntent);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch(item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_food:
                            selectedFragment = new FoodFragment();
                            break;
                        case R.id.nav_exercise:
                            selectedFragment = new ExerciseFragment();
                            break;
                        case R.id.nav_support:
                            selectedFragment = new SupportFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                            selectedFragment).commit();

                    return true;
                }
            };

    public void deleteFoodCollection(){
        foodRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots){
                    String id = docSnapshot.getId();
                    foodRef.document(id).delete();
                }
            }
        });
    }

    public void deleteExerciseCollection(){
        exerciseRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot docSnapshot : queryDocumentSnapshots){
                    String id = docSnapshot.getId();
                    exerciseRef.document(id).delete();
                }
            }
        });
    }

}
