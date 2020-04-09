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

public class FoodFragment extends Fragment {

    FloatingActionButton scanFoodButton;
    FloatingActionButton searchFoodButton;
    FloatingActionButton addFoodButton;

    RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
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

        setUpRecyclerView();

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
                Intent searchIntent = new Intent(getActivity(), SearchFoods.class);
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




