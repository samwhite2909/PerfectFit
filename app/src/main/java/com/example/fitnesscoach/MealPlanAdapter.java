package com.example.fitnesscoach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.w3c.dom.Text;


public class MealPlanAdapter extends FirestoreRecyclerAdapter<MealPlan, MealPlanAdapter.MealPlanHolder> {

    public MealPlanAdapter(@NonNull FirestoreRecyclerOptions<MealPlan> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MealPlanHolder holder, int position, @NonNull MealPlan model) {
        holder.mealPlanTitleTextView.setText(model.getMealTitle());
        holder.mealPlanDescTextView.setText(model.getMealDesc());
        holder.mealPlanPriorityTextView.setText(Integer.toString(model.getPriority()));

    }

    @NonNull
    @Override
    public MealPlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_plan_item,
                parent, false);
        return new MealPlanHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class MealPlanHolder extends RecyclerView.ViewHolder{
        TextView mealPlanTitleTextView;
        TextView mealPlanDescTextView;
        TextView mealPlanPriorityTextView;

        public MealPlanHolder(@NonNull View itemView) {
            super(itemView);

            mealPlanTitleTextView = itemView.findViewById(R.id.mealTitle);
            mealPlanDescTextView = itemView.findViewById(R.id.mealDesc);
            mealPlanPriorityTextView = itemView.findViewById(R.id.mealPriority);

        }
    }
}
