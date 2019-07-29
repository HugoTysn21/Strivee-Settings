package com.strivee.timer;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsAndTimer extends AppCompatActivity {

    private Button launch_timer;
    private ImageView plus_break;
    private ImageView moins_break;
    private ImageView moins_rounds;
    private ImageView plus_rounds;
    private ImageView moins_works;
    private ImageView plus_works;
    private TextView edit_rounds;
    private TextView edit_works;
    private TextView edit_break;
    private int counterTimeWorksDesc = 20;
    private int counterSecondsBreak = 10;
    private int counterRounds = 8;
    private int counterStart_rounds = 0;
    private LinearLayout timer_rounds;
    private LinearLayout timer_break;
    private RelativeLayout main_layout;
    private LinearLayout starter_layout;
    private LinearLayout timer_layout;
    private LinearLayout timer_works;
    private TextView time_timer;
    private TextView pre_starter;
    private TextView rounds_nb;
    private int rds;
    private int counterSecondsWorksAsc = 0;
    private int pre_start = 10;
    private int emom_timeworks = 0;
    private TextView break_time;
    private int time_break;
    private Switch work_switch;
    private LinearLayout time_works_fortime;
    private int maxTime = 999999;
    private boolean isTimeCap = true;

    //faire incrementer le seconds works a chaque action + ou - et convert en long (x1000)
    public String convert(int counterSecondWorksDesc) {

        String seconds = "" + counterSecondWorksDesc;
        return seconds;
    }

    private Long counterToLong(int counterSecondWorksDesc) {

        int longCounter = counterSecondWorksDesc * 1000;
        Long l2 = Long.valueOf(longCounter);

        return l2;
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }

    private void runTabata() {

        Log.d("nb", "" + counterStart_rounds);
        if (counterStart_rounds <= counterRounds) {

            Log.d("nb apres if", "" + counterStart_rounds);
            new CountDownTimer(counterToLong(counterTimeWorksDesc), 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    counterTimeWorksDesc--;
                    String time = secondsToString(counterTimeWorksDesc);
                    time_timer.setText(time);
                }

                @Override
                public void onFinish() {

                    counterTimeWorksDesc = 0;
                    rounds_nb.setText("dn");

                    new CountDownTimer(counterToLong(counterSecondsBreak), 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                            counterSecondsBreak--;
                            String time = secondsToString(counterSecondsBreak);
                            time_timer.setText(time);
                        }

                        @Override
                        public void onFinish() {

                            counterStart_rounds++;
                            Log.d("nb", "" + counterStart_rounds);
                            rounds_nb.setText("" + counterStart_rounds);
                            runTabata();
                        }
                    }.start();
                }
            }.start();
        }
    }

    private void startEmom() {

        emom_timeworks = 0;


        if (counterStart_rounds <= counterRounds) {
            new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    emom_timeworks++;
                    String time = secondsToString(emom_timeworks);
                    time_timer.setText(time);
                }

                @Override
                public void onFinish() {
                    Log.d("df,k,", "" + counterToLong(emom_timeworks));
                    counterStart_rounds++;
                    rounds_nb.setText(secondsToString(counterStart_rounds));
                    startEmom();

                }
            }.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata_timer2);


        setting();
    }

    private void setting() {

        Intent incomingIntent = getIntent();
        final String type = incomingIntent.getStringExtra("Type");

        if (type.equals("AMRAP")) {
            setContentView(R.layout.timer);

            launch_timer = findViewById(R.id.launch_timer);
            timer_break = findViewById(R.id.timer_break);
            timer_break.setVisibility(View.GONE);
            timer_rounds = findViewById(R.id.timer_rounds);
            timer_rounds.setVisibility(View.GONE);
            edit_works = findViewById(R.id.edit_works);
            moins_works = findViewById(R.id.button_moins_works);
            plus_works = findViewById(R.id.button_plus_works);
            timer_layout = findViewById(R.id.timer_layout);
            timer_layout.setVisibility(View.GONE);


            plus_works.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterTimeWorksDesc++;
                    edit_works.setText(secondsToString(counterTimeWorksDesc));
                }
            });

            moins_works.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterTimeWorksDesc--;
                    edit_works.setText(secondsToString(counterTimeWorksDesc));
                }
            });

            launch_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    main_layout = findViewById(R.id.main_layout);
                    main_layout.setVisibility(View.GONE);
                    time_timer = findViewById(R.id.time);
                    timer_layout = findViewById(R.id.timer_layout);
                    timer_layout.setVisibility(View.VISIBLE);
                    rounds_nb = findViewById(R.id.rounds_nb);
                    rounds_nb.setText("Dn ");

                    Log.d("how", convert(counterTimeWorksDesc));

                    new CountDownTimer(counterToLong(pre_start), 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            pre_start--;
                            time_timer.setText(secondsToString(pre_start));
                        }

                        @Override
                        public void onFinish() {
                            rounds_nb.setText("UP ");
                            timer_layout.setVisibility(View.VISIBLE);
                            new CountDownTimer(counterToLong(counterTimeWorksDesc), 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                    counterTimeWorksDesc--;
                                    String time = secondsToString(counterTimeWorksDesc);
                                    time_timer.setText(time);
                                }

                                @Override
                                public void onFinish() {
                                    rounds_nb.setText("END ");
                                    time_timer.setText("00:00");
                                }
                            }.start();
                        }
                    }.start();
                }
            });
        } else if (type.equals("TABATA")) {
            setContentView(R.layout.timer);

            launch_timer = findViewById(R.id.launch_timer);
            plus_break = findViewById(R.id.button_plus_break);
            moins_break = findViewById(R.id.button_moins_break);
            plus_rounds = findViewById(R.id.button_plus_rounds);
            moins_rounds = findViewById(R.id.button_moins_rounds);
            moins_works = findViewById(R.id.button_moins_works);
            plus_works = findViewById(R.id.button_plus_works);
            edit_break = findViewById(R.id.edit_breaks);
            edit_rounds = findViewById(R.id.edit_rounds);
            edit_works = findViewById(R.id.edit_works);
            time_timer = findViewById(R.id.time);

            plus_break.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterSecondsBreak++;
                    edit_break.setText(secondsToString(counterSecondsBreak));
                }
            });

            moins_break.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterSecondsBreak--;
                    edit_break.setText(secondsToString(counterSecondsBreak));
                }
            });

            plus_rounds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterRounds++;
                    edit_rounds.setText(convert(counterRounds));
                }
            });

            moins_rounds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterRounds--;
                    edit_rounds.setText(convert(counterRounds));
                }
            });

            plus_works.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterTimeWorksDesc++;
                    edit_works.setText(secondsToString(counterTimeWorksDesc));
                }
            });

            moins_works.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterTimeWorksDesc--;
                    edit_works.setText(secondsToString(counterTimeWorksDesc));
                }
            });

            launch_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    main_layout = findViewById(R.id.main_layout);
                    main_layout.setVisibility(View.GONE);
                    timer_layout = findViewById(R.id.timer_layout);
                    timer_layout.setVisibility(View.VISIBLE);
                    rounds_nb = findViewById(R.id.rounds_nb);
                    rounds_nb.setText("Dn ");

                    new CountDownTimer(counterToLong(pre_start), 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            pre_start--;
                            time_timer.setText(secondsToString(pre_start));
                        }

                        @Override
                        public void onFinish() {

                            /*rounds_nb.setText("UP");*/

                            runTabata();

                        }
                    }.start();

                }
            });

        } else if (type.equals("INTERVAL")) {
            setContentView(R.layout.timer);
            launch_timer = findViewById(R.id.launch_timer);

            plus_break = findViewById(R.id.button_plus_break);
            moins_break = findViewById(R.id.button_moins_break);
            plus_rounds = findViewById(R.id.button_plus_rounds);
            moins_rounds = findViewById(R.id.button_moins_rounds);
            moins_works = findViewById(R.id.button_moins_works);
            plus_works = findViewById(R.id.button_plus_works);
            edit_break = findViewById(R.id.edit_breaks);
            edit_rounds = findViewById(R.id.edit_rounds);
            edit_works = findViewById(R.id.edit_works);

            plus_break.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterSecondsBreak++;
                    edit_break.setText(secondsToString(counterSecondsBreak));
                }
            });

            moins_break.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterSecondsBreak--;
                    edit_break.setText(secondsToString(counterSecondsBreak));
                }
            });
            plus_rounds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterRounds++;
                    edit_rounds.setText(convert(counterRounds));
                }
            });

            moins_rounds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterRounds--;
                    edit_rounds.setText(convert(counterRounds));
                }
            });

            plus_works.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterTimeWorksDesc++;
                    edit_works.setText(secondsToString(counterTimeWorksDesc));
                }
            });

            moins_works.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterTimeWorksDesc--;
                    edit_works.setText(secondsToString(counterTimeWorksDesc));
                }
            });

            launch_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (type.equals("EMOM")) {
            setContentView(R.layout.timer);

            launch_timer = findViewById(R.id.launch_timer);
            edit_rounds = findViewById(R.id.edit_rounds);
            timer_break = findViewById(R.id.timer_break);
            timer_break.setVisibility(View.GONE);
            timer_works = findViewById(R.id.timer_works);
            timer_works.setVisibility(View.GONE);
            rounds_nb = findViewById(R.id.rounds_nb);
            plus_rounds = findViewById(R.id.button_plus_rounds);
            moins_rounds = findViewById(R.id.button_moins_rounds);

            plus_rounds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterRounds++;
                    edit_rounds.setText(convert(counterRounds));
                }
            });

            moins_rounds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterRounds--;
                    edit_rounds.setText(convert(counterRounds));
                }
            });

            launch_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    main_layout = findViewById(R.id.main_layout);
                    main_layout.setVisibility(View.GONE);
                    time_timer = findViewById(R.id.time);
                    timer_layout = findViewById(R.id.timer_layout);
                    timer_layout.setVisibility(View.VISIBLE);
                    rounds_nb = findViewById(R.id.rounds_nb);
                    rounds_nb.setText("Dn ");

                    Log.d("how", convert(counterTimeWorksDesc));

                    new CountDownTimer(counterToLong(pre_start), 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            pre_start--;
                            time_timer.setText(secondsToString(pre_start));
                        }

                        @Override
                        public void onFinish() {
                            rounds_nb.setText("UP ");

                            rds = counterRounds * 60; // nb seconds total

                            timer_layout.setVisibility(View.VISIBLE);
                            startEmom();
                            /*new CountDownTimer(counterToLong(rds), 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                    rds--;
                                    String time = secondsToString(rds);
                                    time_timer.setText(time);
                                }

                                @Override
                                public void onFinish() {
                                    time_timer.setText("00:00");
                                }
                            }.start();*/
                        }
                    }.start();
                }
            });

        } else if (type.equals("FORTIME")) {
            setContentView(R.layout.timer);

            launch_timer = findViewById(R.id.launch_timer);
            edit_works = findViewById(R.id.edit_works);
            timer_break = findViewById(R.id.timer_break);
            timer_break.setVisibility(View.GONE);
            edit_works = findViewById(R.id.edit_works);
            timer_rounds = findViewById(R.id.timer_rounds);
            timer_rounds.setVisibility(View.GONE);
            moins_works = findViewById(R.id.button_moins_works);
            plus_works = findViewById(R.id.button_plus_works);
            work_switch = findViewById(R.id.work_switch1);
            time_works_fortime = findViewById(R.id.time_works_fortime);
            work_switch.isChecked();

            work_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        time_works_fortime.setVisibility(View.VISIBLE);
                        Log.d("isChecked", "onCheckedChanged: ");
                        isTimeCap = true;


                    } else {
                        // If the switch button is off
                        time_works_fortime.setVisibility(View.GONE);
                        // Show the switch button checked status as toast message
                        Toast.makeText(getApplicationContext(),
                                "Switch is off", Toast.LENGTH_LONG).show();
                        isTimeCap = false;
                    }
                }
            });

            plus_works.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterTimeWorksDesc++;
                    edit_works.setText(secondsToString(counterTimeWorksDesc));
                }
            });

            moins_works.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterTimeWorksDesc--;
                    edit_works.setText(secondsToString(counterTimeWorksDesc));
                }
            });

            launch_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    main_layout = findViewById(R.id.main_layout);
                    main_layout.setVisibility(View.GONE);
                    time_timer = findViewById(R.id.time);
                    timer_layout = findViewById(R.id.timer_layout);
                    timer_layout.setVisibility(View.VISIBLE);
                    rounds_nb = findViewById(R.id.rounds_nb);
                    rounds_nb.setText("Dn ");

                    new CountDownTimer(counterToLong(pre_start), 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            pre_start--;
                            time_timer.setText(secondsToString(pre_start));
                        }

                        @Override
                        public void onFinish() {

                            rounds_nb.setText("UP ");
                            timer_layout.setVisibility(View.VISIBLE);

                            if (isTimeCap) {

                                new CountDownTimer(counterToLong(counterTimeWorksDesc), 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                        counterSecondsWorksAsc++;
                                        String time = secondsToString(counterSecondsWorksAsc);
                                        time_timer.setText(time);
                                    }

                                    @Override
                                    public void onFinish() {
                                        rounds_nb.setText("END ");
                                        time_timer.setText("00:00");
                                    }
                                }.start();
                            } else {
                                new CountDownTimer(counterToLong(maxTime), 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                        counterSecondsWorksAsc++;
                                        String time = secondsToString(counterSecondsWorksAsc);
                                        time_timer.setText(time);
                                    }

                                    @Override
                                    public void onFinish() {
                                        rounds_nb.setText("END ");
                                        time_timer.setText("00:00");
                                    }
                                }.start();
                            }
                        }
                    }.start();
                }
            });
        }
    }

}
