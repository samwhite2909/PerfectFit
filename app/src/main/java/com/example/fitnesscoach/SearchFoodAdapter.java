package com.example.fitnesscoach;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class SearchFoodAdapter extends FirestoreRecyclerAdapter<SearchFood, SearchFoodAdapter.SearchFoodHolder> {
    private OnItemClickListener listener;

    public SearchFoodAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull SearchFoodHolder holder, int position, @NonNull SearchFood model) {
        holder.textViewFoodName.setText(model.getFoodName());
        holder.textViewPortionSize.setText(Double.toString(model.getPortionSize()));
        holder.textViewCalPerPortion.setText(Double.toString(model.getCalPerPortion()));
    }

    @NonNull
    @Override
    public SearchFoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_food_item,
                parent, false);
        return new SearchFoodHolder(v);
    }

    class SearchFoodHolder extends RecyclerView.ViewHolder {
        TextView textViewFoodName;
        TextView textViewPortionSize;
        TextView textViewCalPerPortion;

        public SearchFoodHolder(@NonNull View itemView) {
            super(itemView);

            textViewFoodName = itemView.findViewById(R.id.foodName);
            textViewCalPerPortion = itemView.findViewById(R.id.foodCalPerAveragePortion);
            textViewPortionSize = itemView.findViewById(R.id.foodPortionSize);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
