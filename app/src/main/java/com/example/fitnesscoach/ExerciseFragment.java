package com.example.fitnesscoach;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;

public class ExerciseFragment extends Fragment {

    FloatingActionButton addDatabaseButton;
    FloatingActionButton addExerciseButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_exercise, container, false);

        addDatabaseButton = view.findViewById(R.id.addDatabaseButton);
        addExerciseButton = view.findViewById(R.id.addExerciseFloatButton);

        addDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addDatabaseIntent = new Intent(getActivity(), AddNewExercise.class);
                startActivity(addDatabaseIntent);
            }
        });

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addExerciseIntent = new Intent(getActivity(), AddExerciseToUser.class);
                startActivity(addExerciseIntent);
            }
        });


        return view;
    }


}
