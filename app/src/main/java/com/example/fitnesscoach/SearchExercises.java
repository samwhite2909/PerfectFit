package com.example.fitnesscoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchExercises extends AppCompatActivity {
    FirebaseFirestore fStore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_exercises);

        fStore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.rv);

        Query query = fStore.collection("exercises");

        FirestoreRecyclerOptions<Exercise> options =  new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(query,Exercise.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Exercise, ExercisesViewHolder>(options) {
            @NonNull
            @Override
            public ExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder, parent, false);
                return new ExercisesViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull ExercisesViewHolder holder, int position, @NonNull Exercise model) {
                holder.exerciseName.setText(model.getExerciseName());
                double calDouble = model.getCalPerMin();
                holder.calPerMin.setText(Double.toString(calDouble));
            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

    }

    private class ExercisesViewHolder extends RecyclerView.ViewHolder{

        private TextView exerciseName;
        private TextView calPerMin;
        public ExercisesViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            calPerMin = itemView.findViewById(R.id.calValue);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}