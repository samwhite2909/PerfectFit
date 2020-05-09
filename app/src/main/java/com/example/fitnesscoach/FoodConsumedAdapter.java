package com.example.fitnesscoach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

//Adapter for displaying the user's food diary within the food fragment, used by FirestoreUI.
public class FoodConsumedAdapter extends FirestoreRecyclerAdapter<FoodConsumed, FoodConsumedAdapter.FoodConsumedHolder> {

    public FoodConsumedAdapter(@NonNull FirestoreRecyclerOptions<FoodConsumed> options) {
        super(options);
    }

    //Displays the information from the database into the layout items within each card in the recycler view.
    @Override
    protected void onBindViewHolder(@NonNull FoodConsumedHolder holder, int position, @NonNull FoodConsumed model) {
        holder.textViewFoodName.setText(model.getFoodName());
        double dCal = (model.getCaloriesConsumed());
        int iCal = (int) Math.round(dCal);
        holder.textViewCaloriesConsumed.setText(iCal + " kcal");
        double dGram = (model.getAmountConsumed());
        int iGram = (int) Math.round(dGram);
        holder.textViewAmountConsumed.setText(iGram + "g");
    }

    //Creates the new view to be used by the holder class.
    @NonNull
    @Override
    public FoodConsumedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eaten_food_item,
        parent, false);
        return new FoodConsumedHolder(v);
    }

    //Gets the layout items to be used to add data into the cards in the recycler view.
    class FoodConsumedHolder extends RecyclerView.ViewHolder{
        TextView textViewFoodName;
        TextView textViewCaloriesConsumed;
        TextView  textViewAmountConsumed;

        public FoodConsumedHolder(@NonNull View itemView) {
            super(itemView);

            textViewFoodName = itemView.findViewById(R.id.foodName);
            textViewAmountConsumed = itemView.findViewById(R.id.foodAmount);
            textViewCaloriesConsumed = itemView.findViewById(R.id.foodCalConsumed);
        }
    }
}
