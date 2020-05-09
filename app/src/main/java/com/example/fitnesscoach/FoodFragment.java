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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//This class adds functionality to the food fragment, showing the user's food diary and allowing them
//to add new foods into the database, or into their food diary, altering their calories.
public class FoodFragment extends Fragment {

    FloatingActionButton scanFoodButton;
    FloatingActionButton searchFoodButton;
    FloatingActionButton addFoodButton;
    RecyclerView recyclerView;

    //Creates a reference to the database and the currently logged in user.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    //Creates a reference to the collection containing the user's food diary.
    String userID = mFirebaseAuth.getCurrentUser().getUid();
    CollectionReference foodConsumedRef = db.collection("users").document(userID).collection("foods");

    private FoodConsumedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        scanFoodButton = view.findViewById(R.id.addFoodScanFloatButton);
        searchFoodButton = view.findViewById(R.id.addFoodFloatButton);
        addFoodButton = view.findViewById(R.id.addDatabaseButton);

        recyclerView = view.findViewById(R.id.recyclerView);

        //Populates the recycler view using information from the database.
        setUpRecyclerView();

        //Takes the user to the scanner activity to scan in a barcode.
        scanFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(getActivity(), ScannerActivity.class);
                startActivity(scanIntent);
            }
        });

        //Takes the user to the search activity, where they can search the database for foods to add in.
        searchFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity(), SearchFoods.class);
                startActivity(searchIntent);
            }
        });

        //Allows the user to add a new food into the database in a new activity.
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getActivity(), AddNewFood.class);
                startActivity(addIntent);
            }
        });
        return view;
    }

    //Creates a query containing the user's food diary and passes this to the recycler view to display it.
    private void setUpRecyclerView(){
        Query query = foodConsumedRef;

        FirestoreRecyclerOptions<FoodConsumed> options = new FirestoreRecyclerOptions.Builder<FoodConsumed>()
                .setQuery(query, FoodConsumed.class)
                .build();

        adapter = new FoodConsumedAdapter(options);

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




