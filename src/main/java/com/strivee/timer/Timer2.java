package com.strivee.timer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Timer2 extends AppCompatActivity {

    private TextView mTimerTextField;

    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata_timer);

        mTimerTextField = findViewById(R.id.timerText);

        mTimerTextField = findViewById(R.id.timerText);

        Intent incomingIntent = getIntent();
        final String works = incomingIntent.getStringExtra("work");
        final String round = incomingIntent.getStringExtra("round");
        final String breakk = incomingIntent.getStringExtra("break");

        new CountDownTimer(60000, 1000) {

            Intent incomingIntent = getIntent();
            int time = incomingIntent.getIntExtra("time",1);
            String type = incomingIntent.getStringExtra("type");

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = time;
                /*int minutes = seconds / 60;*/
                seconds = seconds % 60;

                while(minutes == 0 && seconds >= 1){

                    if(seconds == 0 && type.equals("AMRAP")){
                        minutes--;
                    }

                }

                if(seconds == 0){
                    minutes++;
                    Log.d("test", "minutes: " + minutes);
                }
                else{
                    minutes++;
                    Log.d("test", "no minutes: " + minutes);
                }
                mTimerTextField.setText("TIME : " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                //
                mTimerTextField.setText("done!");
            }
        }.start();
    }
}