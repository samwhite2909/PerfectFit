package com.example.fitnesscoach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class LeaderboardItemAdapter extends FirestoreRecyclerAdapter<LeaderboardItem, LeaderboardItemAdapter.LeaderboardItemHolder> {

    public LeaderboardItemAdapter(@NonNull FirestoreRecyclerOptions<LeaderboardItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LeaderboardItemHolder holder, int position, @NonNull LeaderboardItem model) {
        holder.nameTextView.setText(model.getName());
        holder.scoreTextView.setText(Integer.toString(model.getScore()) + " points ");
    }

    @NonNull
    @Override
    public LeaderboardItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardItemHolder(v);
    }

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
