package com.example.fitnesscoach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

//This class is used in populating the recycler view that is used within the leaderboard activity,
//giving it the information [20].
public class LeaderboardItemAdapter extends FirestoreRecyclerAdapter<LeaderboardItem, LeaderboardItemAdapter.LeaderboardItemHolder> {

    public LeaderboardItemAdapter(@NonNull FirestoreRecyclerOptions<LeaderboardItem> options) {
        super(options);
    }

    //Gets the information from the database to be set onto the layout items within the view.
    @Override
    protected void onBindViewHolder(@NonNull LeaderboardItemHolder holder, int position, @NonNull LeaderboardItem model) {
        holder.nameTextView.setText(model.getName());
        holder.scoreTextView.setText(Integer.toString(model.getScore()) + " points ");
    }

    //Creates the view to be used by the holder for each card within the recycler view.
    @NonNull
    @Override
    public LeaderboardItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardItemHolder(v);
    }

    //Gets the layout items to be used to show data from the database.
    class LeaderboardItemHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView scoreTextView;

        public LeaderboardItemHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.leaderboardName);
            scoreTextView = itemView.findViewById(R.id.leaderboardScore);
        }
    }
}
