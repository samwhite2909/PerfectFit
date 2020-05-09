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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//This class provides functionality to the exercise fragment contained within the main menu.
public class ExerciseFragment extends Fragment {

    FloatingActionButton addDatabaseButton;
    FloatingActionButton addExerciseButton;
    RecyclerView recyclerView;

    //Obtains a reference to the database and the currently logged in user.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    String userID;
    CollectionReference completedExerciseRef;

    private CompletedExerciseAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_exercise, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        if(mFirebaseAuth.getCurrentUser() != null) {
            userID = mFirebaseAuth.getCurrentUser().getUid();

            //Gets a reference to the user's completed exercises for the day, aka their workout diary.
            completedExerciseRef = db.collection("users").document(userID).collection("exercises");

            //Method call to populate the recycler view based on user's information.
            setUpRecyclerView();

            addDatabaseButton = view.findViewById(R.id.addDatabaseButton);
            addExerciseButton = view.findViewById(R.id.addExerciseFloatButton);

            //Takes the user to add a new exercise into the database.
            addDatabaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addDatabaseIntent = new Intent(getActivity(), AddNewExercise.class);
                    startActivity(addDatabaseIntent);
                }
            });

            //Takes the user to add a new exercise into their workout diary and adjust their calories.
            addExerciseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addExerciseIntent = new Intent(getActivity(), SearchExercises.class);
                    startActivity(addExerciseIntent);
                }
            });
        }
        return view;
    }

    //Creates the query to search the database to populate the recycler view, using the adapter and FirestoreUI.
    private void setUpRecyclerView(){
        Query query = completedExerciseRef;

        FirestoreRecyclerOptions<CompletedExercise> options = new FirestoreRecyclerOptions.Builder<CompletedExercise>()
                .setQuery(query, CompletedExercise.class)
                .build();

        adapter = new CompletedExerciseAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
