package com.example.fitnesscoach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FoodSuggestionAdapter extends FirestoreRecyclerAdapter<FoodSuggestion, FoodSuggestionAdapter.FoodSuggestionHolder> {


    public FoodSuggestionAdapter(@NonNull FirestoreRecyclerOptions<FoodSuggestion> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodSuggestionHolder holder, int position, @NonNull FoodSuggestion model) {
        holder.textViewFoodName.setText(model.getFoodName());
        double dCal = (model.getCalPerPortion());
        int iCal = (int) Math.round(dCal);
        holder.textViewCalPerPortion.setText(iCal + "kcal");
        double dPortion = (model.getPortionSize());
        int iPortion = (int) Math.round(dPortion);
        holder.textViewPortionSize.setText(iPortion + "g");
    }

    @NonNull
    @Override
    public FoodSuggestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_suggestion_item,
        parent, false);
        return new FoodSuggestionHolder(v);
    }

    class FoodSuggestionHolder extends RecyclerView.ViewHolder {
        TextView textViewFoodName;
        TextView textViewCalPerPortion;
        TextView textViewPortionSize;
        public FoodSuggestionHolder(@NonNull View itemView) {
            super(itemView);
            textViewFoodName = itemView.findViewById(R.id.foodName);
            textViewCalPerPortion = itemView.findViewById(R.id.calPerPortion);
            textViewPortionSize = itemView.findViewById(R.id.portionSize);

        }
    }
}
