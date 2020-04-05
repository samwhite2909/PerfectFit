package com.example.fitnesscoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;

public class StopwatchActivity extends AppCompatActivity {
    Chronometer chronometer;
    ImageButton startButton;
    ImageButton stopButton;
    Button homeButton;

    private boolean isResume;
    Handler handler;
    long tMilliSec,tStart,tBuff,tUpdate = 0L;
    int min,sec,milliSec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        chronometer = findViewById(R.id.chronometer);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        homeButton = findViewById(R.id.homeButton);

        handler = new Handler();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isResume){
                    tStart = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable,0);
                    chronometer.start();
                    isResume = true;
                    stopButton.setVisibility(View.GONE);
                    startButton.setImageDrawable(getResources().getDrawable(
                            R.drawable.ic_pause_black_24dp
                    ));
                }
                else{
                    tBuff += tMilliSec;
                    handler.removeCallbacks(runnable);
                    chronometer.stop();
                    isResume = false;
                    stopButton.setVisibility(View.VISIBLE);
                    startButton.setImageDrawable(getResources().getDrawable(
                            R.drawable.ic_play_arrow_black_24dp
                    ));
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isResume){
                    startButton.setImageDrawable(getResources().getDrawable(
                            R.drawable.ic_play_arrow_black_24dp
                    ));
                    tMilliSec = 0L;
                    tStart = 0L;
                    tBuff = 0L;
                    tUpdate = 0L;
                    sec = 0;
                    min = 0;
                    milliSec = 0;
                    chronometer.setText("00:00:00");
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(StopwatchActivity.this, MenuActivity.class );
                startActivity(homeIntent);
            }
        });

    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tMilliSec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMilliSec;
            sec = (int) (tUpdate/1000);
            min = sec/60;
            sec = sec%60;
            milliSec = (int) (tUpdate%100);
            chronometer.setText(String.format("%02d", min) + ":" +
            String.format("%02d", sec) + ":" + String.format("%02d", milliSec));
            handler.postDelayed(this,60);
        }
    };
}
