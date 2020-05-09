package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

//Allows users scores to be displayed in the form of a leaderboard.
public class LeaderboardActivity extends AppCompatActivity {

    private FirebaseFirestore db  = FirebaseFirestore.getInstance();
    private CollectionReference scoreRef = db.collection("leaderboardScore");
    private LeaderboardItemAdapter adapter;
    TextView scoreText;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    String userID= mFirebaseAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        scoreText = findViewById(R.id.scoreText);

        //Populates the recycler view based on a later created query.
        setUpRecyclerView();

        //Gives the user's total score at the top, in case it is difficult to find their name on the
        //leaderboard.
        if(mFirebaseAuth.getCurrentUser() != null) {
            userID = mFirebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("leaderboardScore").document(userID);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if(documentSnapshot != null && documentSnapshot.exists()){
                        double score = documentSnapshot.getDouble("score");
                        int iScore = (int) Math.round(score);
                        scoreText.setText("Your score is " + iScore + ", where do you rank on the leaderboard?");
                    }
                }
            });
        }
    }

    //Creates a query containing all users and their scores to be displayed in the leaderboard.
    private void setUpRecyclerView(){

        Query query = scoreRef.orderBy("score", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<LeaderboardItem> options = new FirestoreRecyclerOptions.Builder<LeaderboardItem>()
                .setQuery(query, LeaderboardItem.class)
                .build();

        adapter = new LeaderboardItemAdapter(options);

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
