package com.strivee.timer;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button amrap;
    private Button tabata;
    private Button emom;
    private Button interval;
    private Button fortime;
    public String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amrap = findViewById(R.id.amrap);
        tabata = findViewById(R.id.tabata);
        emom = findViewById(R.id.emom);
        interval = findViewById(R.id.interval);
        fortime = findViewById(R.id.fortime);


        amrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "AMRAP";

                Intent i = new Intent(getApplicationContext(), SettingsAndTimer.class);
                i.putExtra("Type", type);
                startActivityForResult(i, 1);

            }
        });

        tabata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "TABATA";

                Intent i = new Intent(getApplicationContext(), SettingsAndTimer.class);
                i.putExtra("Type", type);
                startActivityForResult(i, 1);

            }
        });

        interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "INTERVAL";

                Intent i = new Intent(getApplicationContext(), SettingsAndTimer.class);
                i.putExtra("Type", type);
                startActivityForResult(i, 1);

            }
        });

        emom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "EMOM";

                Intent i = new Intent(getApplicationContext(), SettingsAndTimer.class);
                i.putExtra("Type", type);
                startActivityForResult(i, 1);
            }
        });

        fortime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "FORTIME";

                Intent i = new Intent(getApplicationContext(), SettingsAndTimer.class);
                i.putExtra("Type", type);
                startActivityForResult(i, 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                switch (type){
                    case "FORTIME":
                        int data_fortime_work = data.getIntExtra("value_timeWork", 0);
                        int data_fortime_work_done = data.getIntExtra("value_done_work", 0);
                        Log.d("Test fortime", "TIME WORK SET: " + data_fortime_work);
                        Log.d("Test fortime", "TIME WORK DONE: " + data_fortime_work_done);
                        break;
                    case "INTERVAL":
                    case "TABATA":
                        int data_tabata_works = data.getIntExtra("value_timeWork",0);
                        int data_tabata_break = data.getIntExtra("value_timeBreak",0);
                        Log.d("Test tabata", "TIME WORK SET: " + data_tabata_works);
                        Log.d("Test tabata", "TIME BREAK SET: " + data_tabata_break);
                        break;
                    case "AMRAP":
                        int data_amrap_works = data.getIntExtra("value_timeWork",0);
                        int data_amrap_round = data.getIntExtra("round",0);
                        int data_amrap_done_works = data.getIntExtra("value_done_work",0);
                        Log.d("Test AMRAP", "TIME WORK SET: " + data_amrap_works);
                        Log.d("Test AMRAP", "onActivityResult: " + data_amrap_round);
                        Log.d("Test AMRAP", "TIME WORK DONE: " + data_amrap_done_works);
                        break;
                    case "EMOM":
                        int data_emom_round = data.getIntExtra("round_nb",0);
                        int data_emom_done_round = data.getIntExtra("round_done",0);
                        int data_emom_value_in_round = data.getIntExtra("value_work_time_in_round",0);
                        int data_emom_value_every = data.getIntExtra("value_works_setting",0);
                        Log.d("Test EMOM", "ROUND SET: " + data_emom_round);
                        Log.d("Test EMOM", "ROUND DONE: " + data_emom_done_round);
                        Log.d("Test EMOM", "VALUE IN ROUND: " + data_emom_value_in_round);
                        Log.d("Test EMOM", "EVERY: " + data_emom_value_every);
                        break;
                    default:
                        Log.d("no type", "any value ");
                }
            }
        }
    }
}
