package com.example.fitnesscoach;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;

public class FoodFragment extends Fragment {

    FloatingActionButton scanFoodButton;
    FloatingActionButton searchFoodButton;
    FloatingActionButton addFoodButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        scanFoodButton = view.findViewById(R.id.addFoodScanFloatButton);
        searchFoodButton = view.findViewById(R.id.addFoodFloatButton);
        addFoodButton = view.findViewById(R.id.addDatabaseButton);

        scanFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(getActivity(), ScannerActivity.class);
                startActivity(scanIntent);
            }
        });

        searchFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity(), AddFoodToUser.class);
                startActivity(searchIntent);
            }
        });

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), AddNewFood.class);
                startActivity(addIntent);
            }
        });



        return view;
    }



}
