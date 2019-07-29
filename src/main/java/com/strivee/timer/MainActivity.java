package com.strivee.timer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                i.putExtra("Type",type);
                startActivity(i);

            }
        });

        tabata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "TABATA";

                Intent i = new Intent(getApplicationContext(), SettingsAndTimer.class);
                i.putExtra("Type",type);
                startActivity(i);

            }
        });

        interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "INTERVAL";

                Intent i = new Intent(getApplicationContext(), SettingsAndTimer.class);
                i.putExtra("Type",type);
                startActivity(i);

            }
        });

        emom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "EMOM";

                Intent i = new Intent(getApplicationContext(), SettingsAndTimer.class);
                i.putExtra("Type",type);
                startActivity(i);

            }
        });

        fortime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = "FORTIME";

                Intent i = new Intent(getApplicationContext(), SettingsAndTimer.class);
                i.putExtra("Type",type);
                startActivity(i);

            }
        });

    }
}
