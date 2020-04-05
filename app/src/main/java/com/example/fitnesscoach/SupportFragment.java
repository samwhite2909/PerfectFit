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

public class SupportFragment extends Fragment implements View.OnClickListener {
    CardView stopwatchCard;
    CardView suggestionsCard;
    CardView foodCard;
    CardView workoutCard;
    CardView tipsCard;
    CardView addTipsCard;
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



        return view;
    }


    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()){
            case R.id.stopwatchCard: i = new Intent(getActivity(), StopwatchActivity.class);
                startActivity(i);
                break;
            case R.id.suggestionsCard: i = new Intent(getActivity(), SuggestionsActivity.class);
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
            case R.id.addTipsCard: i = new Intent(getActivity(), AddTipActivity.class);
                startActivity(i);
                break;
            default:
                break;

        }


    }
}
