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


public class CompletedExerciseAdapter extends FirestoreRecyclerAdapter<CompletedExercise, CompletedExerciseAdapter.CompletedExerciseHolder> {

    public CompletedExerciseAdapter(@NonNull FirestoreRecyclerOptions<CompletedExercise> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CompletedExerciseHolder holder, int position, @NonNull CompletedExercise model) {
        holder.textViewTitle.setText(model.getExerciseName());
        holder.textViewDuration.setText(Double.toString(model.getDuration()));
        holder.textViewCalBurned.setText(Double.toString(model.getCaloriesBurned()));
    }

    @NonNull
    @Override
    public CompletedExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_exercise_item,
                parent, false);
        return new CompletedExerciseHolder(v);
    }

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
