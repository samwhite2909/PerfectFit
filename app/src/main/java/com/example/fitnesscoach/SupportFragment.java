package com.example.fitnesscoach;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.Objects;

//This class adds fucntionality to the support fragment, giving users access to numerous support features, [34] was
// heavily used as inspiration for the UI for corresponding to this activity.
public class SupportFragment extends Fragment implements View.OnClickListener {

    CardView stopwatchCard;
    CardView suggestionsCard;
    CardView foodCard;
    CardView workoutCard;
    CardView tipsCard;
    CardView addTipsCard;
    String userID;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;
    String userName;
    double caloriesRemaining;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_support, container, false);

        stopwatchCard = view.findViewById(R.id.stopwatchCard);
        suggestionsCard = view.findViewById(R.id.suggestionsCard);
        foodCard = view.findViewById(R.id.foodCard);
        workoutCard = view.findViewById(R.id.workoutCard);
        tipsCard = view.findViewById(R.id.tipsCard);
        addTipsCard = view.findViewById(R.id.addTipsCard);

        stopwatchCard.setOnClickListener(this);
        suggestionsCard.setOnClickListener(this);
        foodCard.setOnClickListener(this);
        workoutCard.setOnClickListener(this);
        tipsCard.setOnClickListener(this);
        addTipsCard.setOnClickListener(this);

        //Gets a reference to the database and the currently logged in user.
        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser() != null) {
            userID =mFirebaseAuth.getCurrentUser().getUid();

            //Gets the name and calories remaining of the currently logged in user which is needed
            //for some support features.
            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if(documentSnapshot != null && documentSnapshot.exists()) {
                        userName = documentSnapshot.getString("name");
                        caloriesRemaining = documentSnapshot.getDouble("remainingCalValue");
                    }
                }
            });
        }
        return view;
    }

    //Handles clicks onto cards, taking the user to the relevant activity, passing information where
    //required.
    @Override
    public void onClick(View v) {
        final Intent i;

        switch (v.getId()){
            case R.id.stopwatchCard: i = new Intent(getActivity(), StopwatchActivity.class);
                startActivity(i);
                break;
            case R.id.suggestionsCard: i = new Intent(getActivity(), SuggestionsActivity.class);
                i.putExtra("caloriesRemaining", Double.toString(caloriesRemaining));
                startActivity(i);
                break;
            case R.id.foodCard: i = new Intent(getActivity(), MealPlanActivity.class);
                startActivity(i);
                break;
            case R.id.workoutCard: i = new Intent(getActivity(), WorkoutPlanActivity.class);
                startActivity(i);
                break;
            case R.id.tipsCard: i = new Intent(getActivity(), ViewTipsActivity.class);
                startActivity(i);
                break;
            case R.id.addTipsCard:
                    i = new Intent(getActivity(), AddTipActivity.class);
                    i.putExtra("name", userName);
                    i.putExtra("userID", userID);
                    startActivity(i);
                break;
            default:
                break;

        }
    }
}
