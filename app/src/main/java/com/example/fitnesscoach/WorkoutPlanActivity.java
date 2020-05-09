package com.example.fitnesscoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//Allows the user to view their workout plan and make changes to it should they choose to.
public class WorkoutPlanActivity extends AppCompatActivity {

    //Gets a reference to the database and the currently logged in user.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    //Gets a reference containing the user's workout plan.
    String userID = mFirebaseAuth.getCurrentUser().getUid();
    CollectionReference exercisePlanRef = db.collection("users").document(userID).collection("exercisePlans");

    private ExercisePlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);

        //Takes the user to another activity to add in a new exercise into their plan.
        FloatingActionButton floatingActionButton = findViewById(R.id.addExerciseButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutPlanActivity.this, NewExercisePlanActivity.class));
            }
        });

        setUpRecyclerView();
    }

    //Displays all the user's exercises within their workout plan using a query to populate a recycler view
    //containing them all.
    private void setUpRecyclerView(){
        Query query = exercisePlanRef.orderBy("exercisePriority", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ExercisePlan> options = new FirestoreRecyclerOptions.Builder<ExercisePlan>()
                .setQuery(query, ExercisePlan.class)
                .build();
        adapter = new ExercisePlanAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Allows the user to swipe left or right to delete entries from this list and the database.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            //Deletes the specific item.
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        })
                .attachToRecyclerView(recyclerView);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
