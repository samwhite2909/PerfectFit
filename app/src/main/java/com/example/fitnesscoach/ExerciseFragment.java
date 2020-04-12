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

public class ExerciseFragment extends Fragment {

    FloatingActionButton addDatabaseButton;
    FloatingActionButton addExerciseButton;

    RecyclerView recyclerView;

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
            completedExerciseRef = db.collection("users").document(userID).collection("exercises");

            setUpRecyclerView();

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
                    Intent addExerciseIntent = new Intent(getActivity(), SearchExercises.class);
                    startActivity(addExerciseIntent);
                }
            });
        }

        return view;
    }

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
