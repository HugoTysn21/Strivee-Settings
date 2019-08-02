package com.strivee.timer;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SettingsAndTimer extends AppCompatActivity {

    private Button launch_timer;
    private ImageView plus_break;
    private ImageView moins_break;
    private ImageView moins_rounds;
    private ImageView plus_rounds;
    private ImageView moins_works;
    private ImageView plus_works;
    private ImageView cancel;
    private TextView edit_rounds;
    private TextView edit_works;
    private TextView edit_break;
    private TextView time_timer;
    private TextView pre_starter;
    private TextView rounds_nb;
    private TextView break_time;
    private int counterTimeWorksDesc = 20;
    private int counterWorkTime;
    private int counterSecondsBreak = 10;
    private int counterBreakTime;
    private int counterRounds = 8;
    private int counterStart_rounds = 0;
    private int rds;
    private int counterSecondsWorksAsc = -1;
    private int pre_start = 11;
    private int emom_timeworks = -1;
    private int maxTime = 999999;
    private boolean isTimeCap = true;
    private boolean preTimerIsRunning;
    private boolean isWorkTabata = false;
    private boolean isBreakTabata = false;
    private RelativeLayout parentLayout;
    private RelativeLayout main_layout;
    private LinearLayout timer_rounds;
    private LinearLayout timer_break;
    private LinearLayout starter_layout;
    private LinearLayout timer_layout;
    private LinearLayout resume_view;
    private LinearLayout timer_works;
    private LinearLayout time_works_fortime;
    private Switch work_switch;
    private Button resume_view_stop;
    private Button resume_view_resume;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int sound1, sound2;
    private CountDownTimer countDownTimerFortime;
    private CountDownTimer countDownTimerAmrap;
    private CountDownTimer countDownTimerEmom;
    private CountDownTimer countDownTimerTabata;
    private CountDownTimer countDownTimerBreakTabata;

    // incrementer le seconds works for each action + ou - et convert en long (x1000)
    public String convert(int counterSecondWorksDesc) {

        String seconds = "" + counterSecondWorksDesc;
        return seconds;
    }

    private Long counterToLong(int counterSecondWorksDesc) {

        int longCounter = counterSecondWorksDesc * 1000;
        Long l2 = Long.valueOf(longCounter);

        return l2;
    }

    private String roundToFormat(int rds){
        return String.format("%02d", rds);
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata_timer2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();

        }else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        }

        sound1 = soundPool.load(this,R.raw.beep1,1);
        sound2 = soundPool.load(this,R.raw.beep2,1);
        setting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        soundPool.stop(sound1);
        soundPool.stop(sound2);
        soundPool.release();
    }

    private void udpateCountDownTimer() {
        int minutes = (int) (counterTimeWorksDesc / 1000) / 60;
        int seconds = (int) (counterTimeWorksDesc / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        time_timer.setText(timeLeftFormatted);

    }

    private void pauseTimer(String type) {
        if (preTimerIsRunning){
            countDownTimer.cancel();
        }
        else if (type.equals("FORTIME") && !preTimerIsRunning){
            countDownTimerFortime.cancel();
        }
        else if (type.equals("AMRAP") && !preTimerIsRunning){
            countDownTimerAmrap.cancel();
        }
        else if (type.equals("TABATA") && !preTimerIsRunning || type.equals("INTERVAL") && !preTimerIsRunning){
            if (isWorkTabata){
                countDownTimerTabata.cancel();
            }
            else if (isBreakTabata){
                countDownTimerBreakTabata.cancel();
            }
        }
        else if (type.equals("EMOM") && !preTimerIsRunning){
            countDownTimerEmom.cancel();
        }
    }

    private void runTabata() {

        Log.d("nb rd", "" + counterStart_rounds);
        if (counterStart_rounds <= counterRounds) {

            counterWorkTime = counterTimeWorksDesc + 1;
            counterBreakTime = counterSecondsBreak + 1;
            isWorkTabata = true;

            //counterWorkTime n'est pas modifié en haut et du coup il relance avec la valeur de counterTimeWorks qui est de "x"

            countDownTimerTabata = new CountDownTimer(counterToLong(counterTimeWorksDesc) +60, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                     counterWorkTime--;
                     counterTimeWorksDesc--;
                    String time = secondsToString(counterWorkTime);
                    time_timer.setText(time);
                    setSound(counterWorkTime);
                }

                @Override
                public void onFinish() {

                    /*counterWorkTime = 0;*/
                    setSound(counterWorkTime);
                    String time = secondsToString(counterWorkTime);
                    time_timer.setText(time);
                    rounds_nb.setText("dn");
                    isWorkTabata = false;
                    isBreakTabata = true;

                    /*countDownTimerBreakTabata =*/ new CountDownTimer(counterToLong(counterSecondsBreak) +60, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                            counterBreakTime--;
                            String time = secondsToString(counterBreakTime);
                            time_timer.setText(time);
                            setSound(counterBreakTime);
                        }

                        @Override
                        public void onFinish() {

                            counterBreakTime = 0;
                            setSound(counterBreakTime);
                            String time = secondsToString(counterBreakTime);
                            time_timer.setText(time);
                            counterStart_rounds++;
                            rounds_nb.setText(roundToFormat(counterStart_rounds));
                            Log.d("nb", "" + counterStart_rounds);

                            runTabata();
                        }
                    }.start();
                }
            }.start();
        }
    }

    private void runEmom() {

        if (counterStart_rounds <= counterRounds) {
            countDownTimerEmom = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    emom_timeworks++;
                    String time = secondsToString(emom_timeworks);
                    time_timer.setText(time);
                }

                @Override
                public void onFinish() {
                    emom_timeworks = 0;
                    counterStart_rounds++;
                    rounds_nb.setText(roundToFormat(counterStart_rounds));
                    runEmom();
                }
            }.start();
        }
    }

    private void runAmrap() {

        counterTimeWorksDesc++;
        countDownTimerAmrap = new CountDownTimer(counterToLong(counterTimeWorksDesc), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                counterTimeWorksDesc--;
                udpateCountDownTimer();
                String time = secondsToString(counterTimeWorksDesc);
                time_timer.setText(time);
                setSound(counterTimeWorksDesc);
            }

            @Override
            public void onFinish() {
                counterTimeWorksDesc--;
                setSound(counterTimeWorksDesc);
                rounds_nb.setText("END ");
                time_timer.setText("00:00");
            }
        }.start();
    }

    private void runFortime() {

        if (isTimeCap) {

           countDownTimerFortime =  new CountDownTimer(counterToLong(counterTimeWorksDesc), 1000) {
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
            countDownTimerFortime = new CountDownTimer(counterToLong(maxTime), 1000) {

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

    private void setting() {

        setContentView(R.layout.timer);

        Intent incomingIntent = getIntent();
        final String type = incomingIntent.getStringExtra("Type");

        launch_timer = findViewById(R.id.launch_timer);
        timer_break = findViewById(R.id.timer_break);
        timer_rounds = findViewById(R.id.timer_rounds);
        edit_works = findViewById(R.id.edit_works);
        moins_works = findViewById(R.id.button_moins_works);
        plus_works = findViewById(R.id.button_plus_works);
        plus_break = findViewById(R.id.button_plus_break);
        moins_break = findViewById(R.id.button_moins_break);
        plus_rounds = findViewById(R.id.button_plus_rounds);
        moins_rounds = findViewById(R.id.button_moins_rounds);
        timer_layout = findViewById(R.id.timer_layout);
        cancel = findViewById(R.id.cancel);
        main_layout = findViewById(R.id.main_layout);
        time_timer = findViewById(R.id.time);
        timer_layout = findViewById(R.id.timer_layout);
        resume_view_resume = findViewById(R.id.resume_view_resume);
        resume_view_stop = findViewById(R.id.resume_view_stop);
        parentLayout = findViewById(R.id.parentLayout);
        resume_view = findViewById(R.id.resume_view);
        rounds_nb = findViewById(R.id.rounds_nb);
        edit_break = findViewById(R.id.edit_breaks);
        edit_rounds = findViewById(R.id.edit_rounds);
        work_switch = findViewById(R.id.work_switch1);
        time_works_fortime = findViewById(R.id.time_works_fortime);
        timer_works = findViewById(R.id.timer_works);

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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(SettingsAndTimer.this, MainActivity.class);
                startActivity(back);
            }
        });

        timer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancel.setVisibility(View.GONE);
                resume_view.setVisibility(View.VISIBLE);
                timer_layout.setVisibility(View.GONE);
                main_layout.setVisibility(View.GONE);
                pauseTimer(type);

                resume_view_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent back = new Intent(SettingsAndTimer.this, MainActivity.class);
                        startActivity(back);
                    }
                });

                resume_view_resume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cancel.setVisibility(View.VISIBLE);
                        resume_view.setVisibility(View.GONE);
                        timer_layout.setVisibility(View.VISIBLE);
                        main_layout.setVisibility(View.GONE);

                        if (preTimerIsRunning){
                            runPretimer(type);
                        }
                        else {
                            runTimer(type);
                        }
                    }
                });
            }
        });

        launch_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runPretimer(type);
            }
        });

        timer_layout.setVisibility(View.GONE);

        //switch visibility
        switch (type) {
            case "AMRAP":
                timer_break.setVisibility(View.GONE);
                timer_rounds.setVisibility(View.GONE);

                edit_works.setText("15:00");
                counterTimeWorksDesc = 900;

                plus_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterTimeWorksDesc = counterTimeWorksDesc + 60;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                    }
                });

                moins_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterTimeWorksDesc = counterTimeWorksDesc - 60;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                    }
                });
                break;
            case "TABATA":

                edit_works.setText("00:20");
                edit_break.setText("00:10");
                edit_rounds.setText("8");

                plus_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterTimeWorksDesc = counterTimeWorksDesc + 10;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                    }
                });

                moins_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterTimeWorksDesc = counterTimeWorksDesc - 10;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                    }
                });

                plus_break.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterSecondsBreak = counterSecondsBreak + 10;
                        edit_break.setText(secondsToString(counterSecondsBreak));
                    }
                });

                moins_break.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterSecondsBreak = counterSecondsBreak - 10;
                        edit_break.setText(secondsToString(counterSecondsBreak));
                    }
                });
                break;
            case "INTERVAL":

                edit_works.setText("10:00");
                counterTimeWorksDesc = 600;
                edit_break.setText("01:00");
                counterSecondsBreak = 60;
                edit_rounds.setText("4");
                counterRounds = 4;

                plus_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterTimeWorksDesc = counterTimeWorksDesc + 60;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                    }
                });

                moins_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterTimeWorksDesc = counterTimeWorksDesc - 60;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                    }
                });

                plus_break.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterSecondsBreak = counterSecondsBreak + 30;
                        edit_break.setText(secondsToString(counterSecondsBreak));
                    }
                });

                moins_break.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterSecondsBreak = counterSecondsBreak - 30;
                        edit_break.setText(secondsToString(counterSecondsBreak));
                    }
                });
                break;
            case "EMOM":

                edit_rounds.setText("10");
                counterRounds = 10;

                rounds_nb.setText("UP ");
                rds = counterRounds * 60; // nb seconds total
                timer_layout.setVisibility(View.VISIBLE);

                timer_layout.setVisibility(View.GONE);
                timer_break.setVisibility(View.GONE);
                timer_works.setVisibility(View.GONE);
                break;
            case "FORTIME":
                plus_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterTimeWorksDesc = counterTimeWorksDesc + 60;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                    }
                });

                moins_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterTimeWorksDesc = counterTimeWorksDesc - 60;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                    }
                });

                timer_layout.setVisibility(View.GONE);
                timer_break.setVisibility(View.GONE);
                timer_rounds.setVisibility(View.GONE);
                work_switch.setVisibility(View.VISIBLE);
                work_switch.isChecked();

                edit_works.setText("20:00");
                counterTimeWorksDesc = 1200;

                work_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {

                            time_works_fortime.setVisibility(View.VISIBLE);
                            isTimeCap = true;

                        } else {

                            time_works_fortime.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),
                                    "No Time Cap", Toast.LENGTH_LONG).show();
                            isTimeCap = false;
                        }
                    }
                });

                rounds_nb.setText("UP ");
                timer_layout.setVisibility(View.VISIBLE);

                break;
            default:
                Log.d("efr", "setting: ");
        }

    }

    private void setSound(int time){


        if (time <= 5 && time > 0){

            soundPool.play(sound1, 1,1,0,0,1);
            /*mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.beep1);
            mediaPlayer.start();*/

        }
        else if (time == 0){
            Log.d("play", "0");
            soundPool.play(sound2, 1,1,0,0,1);

        }
    }

    private void runPretimer(final String type) {

        preTimerIsRunning = true;
        main_layout.setVisibility(View.GONE);
        timer_layout.setVisibility(View.VISIBLE);
        rounds_nb.setText("Dn ");
        preTimerIsRunning = true;
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.beep1);

        countDownTimer = new CountDownTimer(counterToLong(pre_start), 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                pre_start--;
                time_timer.setText(secondsToString(pre_start));
                Log.d("fie", ""+ pre_start);
                setSound(pre_start);

            }

            @Override
            public void onFinish() {
                rounds_nb.setText("UP ");
                pre_start--;
                time_timer.setText(secondsToString(pre_start));
                setSound(pre_start);
                timer_layout.setVisibility(View.VISIBLE);
                runTimer(type);
            }
        }.start();


        preTimerIsRunning = true;

    }

    private void runTimer(String type) {

        preTimerIsRunning = false;
        if (type.equals("AMRAP")) {

            runAmrap();

        } else if (type.equals("TABATA")) {

            runTabata();

        } else if (type.equals("INTERVAL")) {

            runTabata();

        } else if (type.equals("EMOM")) {

            runEmom();

        } else if (type.equals("FORTIME")) {

            runFortime();
        }
    }

}
