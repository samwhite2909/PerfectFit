package com.example.fitnesscoach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

//Adapter class which contains the operations to populate the recycler view contained within the exercise fragment.
public class CompletedExerciseAdapter extends FirestoreRecyclerAdapter<CompletedExercise, CompletedExerciseAdapter.CompletedExerciseHolder> {

    public CompletedExerciseAdapter(@NonNull FirestoreRecyclerOptions<CompletedExercise> options) {
        super(options);
    }

    //Gives the information, using the model class, to be displayed by taking from the database about the user's
    //competed exercises to be put into their workout diary.
    @Override
    protected void onBindViewHolder(@NonNull CompletedExerciseHolder holder, int position, @NonNull CompletedExercise model) {
        holder.textViewTitle.setText(model.getExerciseName());
        double dMin = (model.getDuration());
        int iMin = (int) Math.round(dMin);
        if(iMin != 1) {
            holder.textViewDuration.setText(iMin + " minutes");
        }
        if(iMin == 1){
            holder.textViewDuration.setText(iMin + " minute");
        }
        double dCal = (model.getCaloriesBurned());
        int iCal = (int) Math.round(dCal);
        holder.textViewCalBurned.setText(iCal + " kcal");
    }

    //Creates the view to be displayed within the recycler view.
    @NonNull
    @Override
    public CompletedExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_exercise_item,
                parent, false);
        return new CompletedExerciseHolder(v);
    }

    //Gets the items to be displayed, not including content which is set by the adapter.
    class CompletedExerciseHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewCalBurned;
        TextView textViewDuration;

        public CompletedExerciseHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.completedExerciseTitle);
            textViewCalBurned = itemView.findViewById(R.id.completedExerciseCalBurned);
            textViewDuration = itemView.findViewById(R.id.completedExerciseDuration);
        }
    }
}
