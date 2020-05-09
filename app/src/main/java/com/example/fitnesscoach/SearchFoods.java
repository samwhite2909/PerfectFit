package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//Allows the user to search the database for foods to add into their food diary.
public class SearchFoods extends AppCompatActivity {

    //Gets a reference to the database and the collection of all foods within it.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference searchFoodRef = db.collection("foods");

    private SearchFoodAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foods);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpRecyclerView();
    }

    //Populates the recycler view with a query containing all foods within the database.
    private void setUpRecyclerView(){
        Query query = searchFoodRef;

        FirestoreRecyclerOptions<SearchFood> options  = new FirestoreRecyclerOptions.Builder<SearchFood>()
                .setQuery(query, SearchFood.class)
                .build();

        adapter = new SearchFoodAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SearchFoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Once a food is clicked on, it's information and the user is taken to the next
                //activity to add it in.
                SearchFood food = documentSnapshot.toObject(SearchFood.class);
                String foodName = food.getFoodName();
                double calPerPortion = food.getCalPerPortion();
                double portionSize = food.getPortionSize();
                String calPerPortionString = Double.toString(calPerPortion);
                String portionSizeString = Double.toString(portionSize);
                Intent i = new Intent(SearchFoods.this, AddFoodToUser.class);
                i.putExtra("foodName", foodName);
                i.putExtra("calPerPortion", calPerPortionString);
                i.putExtra("portionSize", portionSizeString);
                startActivity(i);
            }
        });
    }

    //Allows the user to alter the contents of the recycler view to filter foods by name.
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

    //Changes the query of foods to just those with the name that the user has given.
    private void changeQuery(String s) {
        String searchText = s.toLowerCase();
        Query newQuery = db.collection("foods")
                .whereEqualTo("search", searchText);

        // Make new options
        FirestoreRecyclerOptions<SearchFood> newOptions = new FirestoreRecyclerOptions.Builder<SearchFood>()
                .setQuery(newQuery, SearchFood.class)
                .build();

        // Change options of adapter.
        adapter.updateOptions(newOptions);
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
