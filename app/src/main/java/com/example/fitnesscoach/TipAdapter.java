package com.example.fitnesscoach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

//The adapter required for displaying tips within the database to the user.
public class TipAdapter extends FirestoreRecyclerAdapter<Tip, TipAdapter.TipHolder> {

    public TipAdapter(@NonNull FirestoreRecyclerOptions<Tip> options) {
        super(options);
    }

    //Populates the layout items with data from the database to be displayed.
    @Override
    protected void onBindViewHolder(@NonNull TipHolder holder, int position, @NonNull Tip model) {
        holder.textViewTitle.setText(model.getTipTitle());
        holder.textViewTip.setText(model.getTipTip());
        holder.textViewPoster.setText("Posted by: " + model.getPoster());
    }

    //Creates the view to be used by each card within the recycler view.
    @NonNull
    @Override
    public TipHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_item, parent, false);
        return new TipHolder(v);
    }

    //Gets the layout items that will be used to hold information from the database.
    class TipHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewTip;
        TextView textViewPoster;

        public TipHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tipTitle);
            textViewTip = itemView.findViewById(R.id.tipContent);
            textViewPoster = itemView.findViewById(R.id.posterName);
        }
    }
}
