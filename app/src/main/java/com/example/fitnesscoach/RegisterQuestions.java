package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//Gains additional information from the user for full registration.
public class RegisterQuestions extends AppCompatActivity {

    RadioGroup rgSupport;
    RadioButton rbSupport;
    String supportAnswer;
    RadioGroup rgLossAmount;
    RadioButton rbLossAmount;
    String lossAmount;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_questions);

        rgSupport = findViewById(R.id.supportGroup);
        rgLossAmount = findViewById(R.id.loseGroup);
        registerButton = findViewById(R.id.registerButton);

        //Takes the user to the next registration activity once previous information has been provided.
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");

                //Input validation.
                if (supportAnswer == null) {
                    Toast.makeText(RegisterQuestions.this, "Please answer the support question", Toast.LENGTH_SHORT).show();
                } else if (lossAmount == null) {
                    Toast.makeText(RegisterQuestions.this, "Please answer the weight loss question", Toast.LENGTH_SHORT).show();
                } else if (supportAnswer == null && lossAmount == null) {
                    Toast.makeText(RegisterQuestions.this, "Please answer both questions", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(RegisterQuestions.this, RegisterDetails.class);
                    intent.putExtra("email", email);
                    intent.putExtra("name", name);
                    intent.putExtra("supportAnswer", supportAnswer);
                    intent.putExtra("weightLossAnswer", lossAmount);
                    startActivity(intent);
                }
            }
        });
    }

    //Handles the support question radio button clicks.
    public void rbClickSupport(View v){
        int radioButtonID =  rgSupport.getCheckedRadioButtonId();
        rbSupport = findViewById(radioButtonID);
        supportAnswer = rbSupport.getText().toString();
    }

    //Handles the weight loss question radio button clicks.
    public void rbClickLoss(View v){
        int radioButtonID =  rgLossAmount.getCheckedRadioButtonId();
        rbLossAmount = findViewById(radioButtonID);
        lossAmount = rbLossAmount.getText().toString();
    }
}
