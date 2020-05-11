package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//Gives the user food suggestions based on their remaining calories.
public class SuggestionsActivity extends AppCompatActivity {

    //Gets a reference to the database and all foods stored within it.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference foodRef = db.collection("foods");

    private FoodSuggestionAdapter adapter;

    double remainingCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        setUpRecyclerView();
    }

    //Populates the recycler view with all foods with less calories per average portion then the
    //user's remaining calories [20].
    private void setUpRecyclerView(){

        String remainingCalString  = getIntent().getStringExtra("caloriesRemaining");
        remainingCal = Double.parseDouble(remainingCalString);
        Query query = foodRef.whereLessThan("calPerPortion", remainingCal).orderBy("calPerPortion", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<FoodSuggestion> options = new FirestoreRecyclerOptions.Builder<FoodSuggestion>()
                .setQuery(query, FoodSuggestion.class)
                .build();

        adapter = new FoodSuggestionAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
