package com.example.fitnesscoach;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.Executor;

public class HomeFragment extends Fragment {

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseFirestore fStore;
    TextView greeting;
    TextView calValue;
    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        greeting = view.findViewById(R.id.homeText);
        calValue = view.findViewById((R.id.calValue));

        fStore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

            if(mFirebaseAuth.getCurrentUser() != null) {
                userID = mFirebaseAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fStore.collection("users").document(userID);

                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        String name = documentSnapshot.getString("name");
                        double remainingCalValueNum = documentSnapshot.getDouble("remainingCalValue");
                        String remainingCalString  = Double.toString(remainingCalValueNum);
                        greeting.setText("Welcome " + name);
                        calValue.setText(remainingCalString);
                    }
                });
            }
        return view;
    }


}
