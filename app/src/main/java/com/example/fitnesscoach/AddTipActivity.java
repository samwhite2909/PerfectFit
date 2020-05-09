package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//This class allows the user to share their tips with other users of the app.
public class AddTipActivity extends AppCompatActivity {

    Button shareButton;
    EditText tipEdit;
    EditText tipTitleEdit;
    FirebaseFirestore fStore;
    int random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        shareButton = findViewById(R.id.shareButton);
        tipEdit = findViewById(R.id.tipInput);
        tipTitleEdit = findViewById(R.id.tipTitleInput);

        //Creates a reference to the database.
        fStore = FirebaseFirestore.getInstance();

        random = new Random().nextInt(100000);

        //Gets the name and ID of the user that is making the post.
        final String name = getIntent().getStringExtra("name");
        final String userID = getIntent().getStringExtra("userID");

        //Adds the tip into the database to be displayed, based on user input once validated.
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actualTip = tipEdit.getText().toString();
                String tipTitle = tipTitleEdit.getText().toString();

                if(actualTip.isEmpty() || tipTitle.isEmpty()){
                    Toast.makeText(AddTipActivity.this, "Please give your tip a title and content", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentReference = fStore.collection("tips").document();
                    final Map<String, Object> tip = new HashMap<>();
                    tip.put("userID", userID);
                    tip.put("poster", name);
                    tip.put("tipTitle", tipTitle);
                    tip.put("tipTip", actualTip);
                    tip.put("random", random);
                    documentReference.set(tip).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Tag", "onSuccess: tip profile is created");
                            Toast.makeText(AddTipActivity.this, "Tip shared successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(AddTipActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
