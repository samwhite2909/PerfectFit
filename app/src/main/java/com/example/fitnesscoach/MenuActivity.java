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

//The main menu activity, containing the four main fragments of the app.
public class MenuActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseFirestore fStore;
    String userID;
    String supportAnswer;
    String lastLoginDate;
    double dailyCalLimitReduction;
    double remainingCal;
    double dailyCalLimit;
    double score;
    CollectionReference foodRef;
    CollectionReference exerciseRef;

    //Gets the current date as a value to be checked against.
    final Calendar calendar = Calendar.getInstance();
    String currentDate = DateFormat.getDateInstance().format(calendar.getTime());


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

            //Gets a a reference to the database and the currently logged in user.
            fStore = FirebaseFirestore.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();

            MaterialToolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            final BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

            //Assigns food and workout diary references based on which user is logged in.
            if(mFirebaseAuth.getCurrentUser() != null) {
                userID = mFirebaseAuth.getCurrentUser().getUid();
                foodRef = fStore.collection("users").document(userID).collection("foods");
                exerciseRef = fStore.collection("users").document(userID).collection("exercises");

                //Obtains references to the user's information and their leaderboard score.
                final DocumentReference documentReference = fStore.collection("users").document(userID);
                final DocumentReference documentReferenceScore = fStore.collection("leaderboardScore").document(userID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            dailyCalLimit = documentSnapshot.getDouble("dailyCalLimit");
                            dailyCalLimitReduction = documentSnapshot.getDouble("calLimitWithReduction");
                            remainingCal = documentSnapshot.getDouble("remainingCalValue");
                            supportAnswer = documentSnapshot.getString("supportAnswer");

                            //Removes the support section if the user has specified that they didn't want it.
                            if (supportAnswer.equalsIgnoreCase("No")) {
                                bottomNav.getMenu().removeItem(R.id.nav_support);
                            }

                            //If the user has logged in on a different day then their score is updated and their
                            //calories and diaries reset.
                            lastLoginDate = documentSnapshot.getString("lastLoginDate");
                            if (!lastLoginDate.equals(currentDate)) {

                                documentReferenceScore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                        score = documentSnapshot.getDouble("score");

                                        //Assigning score to the user based on their closeness to their target.
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

    //Creates the toolbar menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //Provides the expected outcomes once options are selected from the toolbar menu.
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

    //Takes the user back to the default initial starting activity.
    public void openMainActivity() {
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
    }

    //Takes the user to the account details activity.
    public void openAccountActivity() {
        Intent accountIntent = new Intent(this, AccountDetails.class);
        startActivity(accountIntent);
    }

    //Takes the user to the appropriate fragment once the option is selected from the navigation menu.
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

    //Deletes the user's food diary from the previous day.
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

    //Deletes the user's workout diary from the previous day.
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
