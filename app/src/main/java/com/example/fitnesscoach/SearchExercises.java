package com.example.fitnesscoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.CollationElementIterator;

public class SearchExercises extends AppCompatActivity {
    FirebaseFirestore fStore;
    RecyclerView mFirestoreList;
    FirestoreRecyclerAdapter adapter;
    onItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_exercises);


        fStore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.rv);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Query query = fStore.collection("exercises");

        FirestoreRecyclerOptions<Exercise> options =  new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(query,Exercise.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<Exercise, ExercisesViewHolder>(options) {
            @NonNull
            //@Override
            public ExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder, parent, false);
                return new ExercisesViewHolder(view);

            }

            //@Override
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercise_search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                changeQuery(query);
                return false;
            }



            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void changeQuery(String s) {
        String searchText = s.toLowerCase();
        Query newQuery = fStore.collection("exercises")
                .whereEqualTo("search", searchText);

        // Make new options
        FirestoreRecyclerOptions<Exercise> newOptions = new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(newQuery, Exercise.class)
            .build();

        // Change options of adapter.
       adapter.updateOptions(newOptions);
    }

    private void searchData(String s) {

        String searchText = s.toLowerCase();




        /*fStore.collection("exercises").whereEqualTo("search", s.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Exercise exercise = new Exercise(doc.getString("exerciseName"), doc.getDouble("calPerMin"));
                            //Toast.makeText(SearchExercises.this, exercise.getExerciseName(), Toast.LENGTH_SHORT).show();


                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SearchExercises.this, e.getMessage(),Toast.LENGTH_SHORT);
                    }
                });*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class ExercisesViewHolder extends RecyclerView.ViewHolder {


        private TextView exerciseName;
        private TextView calPerMin;
        public ExercisesViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            calPerMin = itemView.findViewById(R.id.calValue);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                        String exerciseNameString = exerciseName.getText().toString();
                        String calPerMinDouble =  calPerMin.getText().toString();
                        Intent addExerciseToUser = new Intent(SearchExercises.this, AddExerciseToUser.class);
                        addExerciseToUser.putExtra("exerciseName", exerciseNameString);
                        addExerciseToUser.putExtra("calPerMin", calPerMinDouble);
                        startActivity(addExerciseToUser);

                 }
             });
        }

    }
    public interface onItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
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
