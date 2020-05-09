package com.example.fitnesscoach;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Random;
import java.util.concurrent.Executor;

//This class provides functionality to the home fragment, the main fragment of the main menu.
public class HomeFragment extends Fragment implements View.OnClickListener {

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseFirestore fStore;
    TextView greeting;
    TextView calValue;
    TextView calLeftWarning;
    TextView calWarningGame;
    String userID;
    CardView leaderboardCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        greeting = view.findViewById(R.id.greetingText);
        calValue = view.findViewById((R.id.caloriesRemainingText));
        calLeftWarning = view.findViewById(R.id.calLeftWarning);
        calWarningGame = view.findViewById(R.id.calWarningGame);

        leaderboardCard = view.findViewById(R.id.leaderboardCard);
        leaderboardCard.setOnClickListener(this);

        //Creates a reference to the database and the currently logged in user.
        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

            if(mFirebaseAuth.getCurrentUser() != null) {
                userID = mFirebaseAuth.getCurrentUser().getUid();

                //Creates a document reference so the the name and calorie values of the user are
                //available to be displayed.
                DocumentReference documentReference = fStore.collection("users").document(userID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if(documentSnapshot != null && documentSnapshot.exists()){
                            String name = documentSnapshot.getString("name");
                            greeting.setText("Welcome back " + name + "!");
                        }
                        else{
                            greeting.setText("Welcome");
                        }
                        if(documentSnapshot != null && documentSnapshot.exists()){
                            double remainingCalValueNum = documentSnapshot.getDouble("remainingCalValue");
                            String remainingCalString  = Double.toString(remainingCalValueNum);
                            String remainingCalStringText = "You have " + remainingCalString + " calories remaining for today";
                            calValue.setText(remainingCalStringText);

                            //Gives the user appropariate messages to be displayed on the leaderboard card, based
                            //on their remaining calories.
                            if(remainingCalValueNum > 250 && remainingCalValueNum <= 500){
                                calLeftWarning.setText("You still have quite a few calories left for today");
                                calWarningGame.setText("To keep on track and to score points, try to get as close to your target as you can!");
                            }
                            if(remainingCalValueNum > 500){
                                calLeftWarning.setText("You still have a lot of calories left for today");
                                calWarningGame.setText("To keep on track and to score more points, try to get as close to your target as you can!");
                            }
                            if(remainingCalValueNum > 125 && remainingCalValueNum <= 250){
                                calLeftWarning.setText("You have a few calories left for today");
                                calWarningGame.setText("You're close to target and you have some points in the bag, get closer to the target for more points!");
                            }
                            if(remainingCalValueNum <= 125){
                                calLeftWarning.setText("You're doing well, you're really close to your target for the day");
                                calWarningGame.setText("You're on target and earning a lot of points, well done!");
                            }
                            if(remainingCalValueNum < 0){
                                calLeftWarning.setText("You've gone over your calories for the day");
                                calWarningGame.setText("Try again tomorrow to stay within your target to keep on track and to score more points");
                            }
                        }
                    }
                });
            }
        return view;
    }

    //Starts the leaderboard activity once the card is clicked.
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), LeaderboardActivity.class));
    }

}
