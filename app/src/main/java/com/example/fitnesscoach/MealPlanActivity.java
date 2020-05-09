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

//Allows the user to view their planned meals, swipe to delete them and be taken to add a new meal in.
public class MealPlanActivity extends AppCompatActivity {

    //Gets a reference to the database and the currently logged in user.
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    String userID = mFirebaseAuth.getCurrentUser().getUid();

    //Obtains a reference to the logged in user's meal plans.
    CollectionReference mealPlanRef = db.collection("users").document(userID).collection("mealPlans");

    private MealPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        FloatingActionButton addMealButton = findViewById(R.id.addMealButton);

        //Takes the user to add in a new meal.
        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MealPlanActivity.this, NewMealPlanActivity.class));
            }
        });

        setUpRecyclerView();
    }

    //Populates the recycler view with the user's planned meals, by using a query for that collection
    //and the meal plan adapter.
    private void setUpRecyclerView(){
        Query query = mealPlanRef.orderBy("priority",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MealPlan> options = new FirestoreRecyclerOptions.Builder<MealPlan>()
                .setQuery(query, MealPlan.class)
                .build();

        adapter = new MealPlanAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Gives left and right swipe to delete functionality.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
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
