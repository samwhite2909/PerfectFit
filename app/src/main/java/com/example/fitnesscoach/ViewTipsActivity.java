package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//Allows the user to view all tips within the database.
public class ViewTipsActivity extends AppCompatActivity {

    //Gets a reference to the database and all tips within it.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference tipRef = db.collection("tips");

    private TipAdapter adapter;

    //Gets a reference to the currently logged in user.
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    String userID= mFirebaseAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tips);

        setUpRecyclerView();
    }

    //Uses the query of all available tips to populate the recycler view [20].
    private void setUpRecyclerView(){

        Query query = tipRef;
        FirestoreRecyclerOptions<Tip> options = new FirestoreRecyclerOptions.Builder<Tip>()
                .setQuery(query, Tip.class)
                .build();

        adapter = new TipAdapter(options);

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
