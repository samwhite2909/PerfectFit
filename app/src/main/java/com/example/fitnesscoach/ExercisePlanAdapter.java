package com.example.fitnesscoach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

//Adapter class which helps to populate the recycler view within the workout planning activity.
public class ExercisePlanAdapter extends FirestoreRecyclerAdapter<ExercisePlan, ExercisePlanAdapter.ExercisePlanHolder> {

    public ExercisePlanAdapter(@NonNull FirestoreRecyclerOptions<ExercisePlan> options) {
        super(options);
    }

    //Gets information from the model class and the database to populate the items within the recycler view.
    @Override
    protected void onBindViewHolder(@NonNull ExercisePlanHolder holder, int position, @NonNull ExercisePlan model) {
        holder.textViewExerciseTitle.setText(model.getExerciseTitle());
        holder.textViewExerciseTarget.setText(model.getExerciseTarget());
        holder.textViewExercisePriority.setText(Integer.toString(model.getExercisePriority()));
    }

    //Creates the view to be passed using the adapter.
    @NonNull
    @Override
    public ExercisePlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item,parent, false);
        return new ExercisePlanHolder(v);
    }

    //Allows for specific items to be deleted using swiping.
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    //Gets the layout items to be populated using information from the database and the model class.
    class ExercisePlanHolder extends RecyclerView.ViewHolder {

        TextView textViewExerciseTitle;
        TextView textViewExerciseTarget;
        TextView textViewExercisePriority;

        public ExercisePlanHolder(@NonNull View itemView) {
            super(itemView);
            textViewExerciseTitle = itemView.findViewById(R.id.exerciseTitle);
            textViewExerciseTarget = itemView.findViewById(R.id.exerciseTarget);
            textViewExercisePriority = itemView.findViewById(R.id.exercisePriority);
        }
    }
}
