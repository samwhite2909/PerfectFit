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

public class AddTipActivity extends AppCompatActivity {

    Button shareButton;
    EditText tipEdit;
    static int tipCounter;
    FirebaseFirestore fStore;
    int random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);

        shareButton = findViewById(R.id.shareButton);
        tipEdit = findViewById(R.id.tipInput);

        fStore = FirebaseFirestore.getInstance();

        random = new Random().nextInt(100000);


        final String name = getIntent().getStringExtra("name");

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actualTip = tipEdit.getText().toString();

                DocumentReference documentReference = fStore.collection("tips").document();
                final Map<String, Object> tip = new HashMap<>();
                tip.put("name", name);
                tip.put("tip", actualTip);
                tip.put("random", random);
                documentReference.set(tip).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Tag", "onSuccess: tip profile is created");
                        Toast.makeText(AddTipActivity.this, "Tip shared successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent  = new Intent(AddTipActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

    }
}
