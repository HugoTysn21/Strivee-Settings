package com.strivee.timer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

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
    private TextView rounds_nb;
    private TextView timer_works_title;
    private int counterTimeWorksDesc = 20;
    private int counterWorkTime;
    private int counterSecondsBreak = 10;
    private int counterBreakTime;
    private int counterRounds = 8;
    private int counterStart_rounds = 0;
    private int rds;
    private int counterSecondsWorksAsc = 0;
    private int pre_start = 11;
    private int emom_timeworks = -1;
    private int counterStarter_T_I = 1;
    private int counterStarter_emom = 1;
    private int maxTime = 999999;
    private boolean isTimeCap = true;
    private boolean preTimerIsRunning;
    private boolean isWorkTabata = false;
    private boolean isResting = true;
    private RelativeLayout main_layout;
    private LinearLayout timer_rounds;
    private LinearLayout timer_break;
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
    private LinearLayout totalRound;
    private TextView totalRound_tv;
    private Button btn_plus_rd;
    private int roundUpdate = 0;
    private Button btn_minus_rd;
    private int counterSecondsWorksEmom = 60;
    private int counterAmrapWork = 0;
    private TextView layout_title;
    private ImageView logo_strivee_counter;
    private TextView timer_round_title;

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

    private String roundToFormat(int rds) {
        return String.format("%02d", rds);
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata_timer2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        sound1 = soundPool.load(this, R.raw.beep1, 1);
        sound2 = soundPool.load(this, R.raw.beep2, 1);
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
        if (preTimerIsRunning) {
            countDownTimer.cancel();
        } else if (type.equals("FORTIME") && !preTimerIsRunning) {
            countDownTimerFortime.cancel();
        } else if (type.equals("AMRAP") && !preTimerIsRunning) {
            countDownTimerAmrap.cancel();
        } else if (type.equals("TABATA") && !preTimerIsRunning || type.equals("INTERVAL") && !preTimerIsRunning) {
            countDownTimerTabata.cancel();
        } else if (type.equals("EMOM") && !preTimerIsRunning) {
            countDownTimerEmom.cancel();
        }
    }

    private void stopTimerAndSaveData(String type) {

        if (type.equals("FORTIME")) {
            Log.d("test", "stopTimer: " + counterSecondsWorksAsc + " : time set : " + counterTimeWorksDesc);
            Intent intent_data_fortime = new Intent();
            intent_data_fortime.putExtra("type", type);
            intent_data_fortime.putExtra("round", roundUpdate);
            intent_data_fortime.putExtra("value_timeWork", counterTimeWorksDesc);
            intent_data_fortime.putExtra("value_done_work", counterSecondsWorksAsc);
            setResult(RESULT_OK, intent_data_fortime);
            finish();
        } else if (type.equals("AMRAP")) {
            Intent intent_data_amrap = new Intent(SettingsAndTimer.this, MainActivity.class);
            intent_data_amrap.putExtra("type", type);
            intent_data_amrap.putExtra("round", roundUpdate);
            intent_data_amrap.putExtra("value_timeWork", counterTimeWorksDesc);
            intent_data_amrap.putExtra("value_done_work", counterAmrapWork);
            setResult(RESULT_OK, intent_data_amrap);
            finish();
        } else if (type.equals("TABATA") || type.equals("INTERVAL")) {
            Intent intent_data_tab_inter = new Intent(SettingsAndTimer.this, MainActivity.class);
            intent_data_tab_inter.putExtra("type", type);
            intent_data_tab_inter.putExtra("round", roundUpdate);
            intent_data_tab_inter.putExtra("rounds_nb", counterRounds);
            intent_data_tab_inter.putExtra("value_timeWork", counterTimeWorksDesc);
            intent_data_tab_inter.putExtra("value_timeBreak", counterBreakTime);
            setResult(RESULT_OK, intent_data_tab_inter);
            finish();
        } else if (type.equals("EMOM")) {
            Intent intent_data_emom = new Intent(SettingsAndTimer.this, MainActivity.class);
            intent_data_emom.putExtra("round_nb", counterRounds);
            intent_data_emom.putExtra("value_works_setting", counterSecondsWorksEmom);
            intent_data_emom.putExtra("round_done", counterStart_rounds);
            intent_data_emom.putExtra("value_work_time_in_round", emom_timeworks);
            setResult(RESULT_OK, intent_data_emom);
            finish();
        }
    }

    private void runTabata(final String type) {

        // nb round for each rep
        if (counterStarter_T_I <= counterRounds) {

            isWorkTabata = true;

            if (isResting) {
                countDownTimerTabata = new CountDownTimer(counterToLong(counterWorkTime) +60, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        if (type.equals("INTERVAL")){
                            counterSecondsWorksAsc++;
                            String time = secondsToString(counterSecondsWorksAsc);
                            time_timer.setText(time);
                        }
                        else{
                            counterWorkTime--;
                            String time = secondsToString(counterWorkTime);
                            time_timer.setText(time);
                            setSound(counterWorkTime);
                        }

                        /*if(counterSecondsWorksAsc == counterWorkTime){
                            time_timer.setText("00:00");
                        }*/

                    }

                    @Override
                    public void onFinish() {

                        Log.d("frfrf", "onFinish: " + counterSecondsWorksAsc);

                        setSound(counterWorkTime);
                        String time = secondsToString(counterWorkTime);
                        time_timer.setText(time);
                        rounds_nb.setText(" r");
                        isResting = false;
                        if (type.equals("TABATA")){
                            counterWorkTime = counterTimeWorksDesc;
                        }
                        else {
                            counterSecondsWorksAsc = 0;
                        }
                        runTabata(type);
                    }
                }.start();
            }
            // LAUNCH REST
            else {

                counterBreakTime = counterSecondsBreak;
                countDownTimerTabata = new CountDownTimer(counterToLong(counterBreakTime) +60, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        counterBreakTime--;
                        String time = secondsToString(counterBreakTime);
                        time_timer.setText(time);
                        setSound(counterBreakTime);
                    }

                    @Override
                    public void onFinish() {

                        setSound(counterBreakTime);
                        String time = secondsToString(counterBreakTime);
                        time_timer.setText(time);
                        counterStarter_T_I++;
                        rounds_nb.setText(roundToFormat(counterStarter_T_I));
                        Log.d("nb", "" + counterStarter_T_I);
                        isResting = true;
                        counterBreakTime = counterSecondsBreak;
                        runTabata(type);
                    }
                }.start();
            }
        }
    }

    private void runEmom() {

        if (counterStarter_emom <= counterRounds) {
            countDownTimerEmom = new CountDownTimer(counterToLong(counterSecondsWorksEmom), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    emom_timeworks++;
                    String time = secondsToString(emom_timeworks);
                    time_timer.setText(time);
                }

                @Override
                public void onFinish() {
                    emom_timeworks = 0;
                    counterStarter_emom++;
                    rounds_nb.setText(roundToFormat(counterStarter_emom));
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
                time_timer.setText("00:00");
            }
        }.start();
    }

    private void runFortime() {

        if (isTimeCap) {

            Log.d("counter", "runFortime: " + counterTimeWorksDesc);
            countDownTimerFortime = new CountDownTimer(counterToLong(counterTimeWorksDesc), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    Log.d("counter on thick", "onTick: " + counterSecondsWorksAsc);
                    counterSecondsWorksAsc++;
                    String time = secondsToString(counterSecondsWorksAsc);
                    time_timer.setText(time);
                }

                @Override
                public void onFinish() {
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
        resume_view = findViewById(R.id.resume_view);
        rounds_nb = findViewById(R.id.rounds_nb);
        edit_break = findViewById(R.id.edit_breaks);
        edit_rounds = findViewById(R.id.edit_rounds);
        work_switch = findViewById(R.id.work_switch1);
        time_works_fortime = findViewById(R.id.time_works_fortime);
        timer_works = findViewById(R.id.timer_works);
        totalRound = findViewById(R.id.totalRound);
        totalRound_tv = findViewById(R.id.totalRound_tv);
        btn_plus_rd = findViewById(R.id.totalRound_button_plus);
        btn_minus_rd = findViewById(R.id.totalRound_button_minus);
        totalRound.setVisibility(View.GONE);
        timer_works_title = findViewById(R.id.timer_works_title);
        layout_title = findViewById(R.id.layout_title);
        logo_strivee_counter = findViewById(R.id.logo_strivee_counter);
        logo_strivee_counter.setVisibility(View.GONE);
        timer_round_title = findViewById(R.id.timer_round_title);

        plus_rounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterRounds++;
                edit_rounds.setText(convert(counterRounds));
                vibration(false);
            }
        });

        moins_rounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterRounds > 1) {
                    counterRounds--;
                    edit_rounds.setText(convert(counterRounds));
                    vibration(false);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(SettingsAndTimer.this, MainActivity.class);
                startActivity(back);
                vibration(false);
            }
        });


        timer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resume_view.setVisibility(View.VISIBLE);
                timer_layout.setVisibility(View.GONE);
                main_layout.setVisibility(View.GONE);
                totalRound.setVisibility(View.GONE);
                vibration(false);
                pauseTimer(type);

                resume_view_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vibration(false);
                        stopTimerAndSaveData(type);
                    }
                });

                resume_view_resume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cancel.setVisibility(View.VISIBLE);
                        resume_view.setVisibility(View.GONE);
                        timer_layout.setVisibility(View.VISIBLE);
                        main_layout.setVisibility(View.GONE);
                        vibration(true);

                        if (preTimerIsRunning) {
                            runPretimer(type);
                        } else {
                            runTimer(type);
                        }
                    }
                });
            }
        });

        launch_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibration(true);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                runPretimer(type);
            }
        });

        timer_layout.setVisibility(View.GONE);

        switch (type) {
            case "AMRAP":

                layout_title.setText("AMRAP");
                timer_break.setVisibility(View.GONE);
                timer_rounds.setVisibility(View.GONE);
                edit_works.setText("15:00");
                counterTimeWorksDesc = 900;

                plus_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterTimeWorksDesc = counterTimeWorksDesc + 60;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                        vibration(false);
                    }
                });

                moins_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (counterTimeWorksDesc > 60) {

                            counterTimeWorksDesc = counterTimeWorksDesc - 60;
                            edit_works.setText(secondsToString(counterTimeWorksDesc));
                            vibration(false);
                        }
                    }
                });

                btn_plus_rd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        vibration(false);
                        roundUpdate++;
                        totalRound_tv.setText("Round : " + roundToFormat(roundUpdate));

                        if (roundUpdate >= 1) {

                            btn_minus_rd.setVisibility(View.VISIBLE);
                        }
                    }
                });

                btn_minus_rd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        vibration(false);

                        if (roundUpdate > 0) {

                            roundUpdate--;
                            totalRound_tv.setText("Round : " + roundToFormat(roundUpdate));
                        }
                    }
                });
                break;
            case "TABATA":

                layout_title.setText("TABATA");
                edit_works.setText("00:20");
                edit_break.setText("00:10");
                edit_rounds.setText("8");
                counterWorkTime = counterTimeWorksDesc;
                counterBreakTime = counterSecondsBreak;

                plus_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        vibration(false);
                        counterTimeWorksDesc = counterTimeWorksDesc + 10;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                    }
                });

                moins_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (counterTimeWorksDesc > 10) {

                            vibration(false);
                            counterTimeWorksDesc = counterTimeWorksDesc - 10;
                            edit_works.setText(secondsToString(counterTimeWorksDesc));
                        }
                    }
                });

                plus_break.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterSecondsBreak = counterSecondsBreak + 10;
                        edit_break.setText(secondsToString(counterSecondsBreak));
                        vibration(false);
                    }
                });

                moins_break.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (counterSecondsBreak > 10) {

                            counterSecondsBreak = counterSecondsBreak - 10;
                            edit_break.setText(secondsToString(counterSecondsBreak));
                            vibration(false);
                        }
                    }
                });
                break;
            case "INTERVAL":

                layout_title.setText("INTERVAL");
                edit_works.setText("10:00");
                counterTimeWorksDesc = 600;
                edit_break.setText("01:00");
                counterSecondsBreak = 60;
                edit_rounds.setText("4");
                counterRounds = 4;
                counterWorkTime = counterTimeWorksDesc;
                counterBreakTime = counterSecondsBreak;

                plus_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (counterTimeWorksDesc <= 570) {

                            counterTimeWorksDesc = counterTimeWorksDesc + 30;
                            edit_works.setText(secondsToString(counterTimeWorksDesc));
                        } else if (counterTimeWorksDesc >= 600 && counterTimeWorksDesc <= 1740) {

                            counterTimeWorksDesc = counterTimeWorksDesc + 60;
                            edit_works.setText(secondsToString(counterTimeWorksDesc));
                        }
                        vibration(false);
                    }
                });

                moins_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (counterTimeWorksDesc >= 60 && counterTimeWorksDesc <= 600) {
                            counterTimeWorksDesc = counterTimeWorksDesc - 30;
                            edit_works.setText(secondsToString(counterTimeWorksDesc));
                        } else if (counterTimeWorksDesc >= 600) {
                            counterTimeWorksDesc = counterTimeWorksDesc - 60;
                            edit_works.setText(secondsToString(counterTimeWorksDesc));
                        }
                        vibration(false);
                    }
                });

                plus_break.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterSecondsBreak = counterSecondsBreak + 30;
                        edit_break.setText(secondsToString(counterSecondsBreak));
                        vibration(false);
                    }
                });

                moins_break.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (counterSecondsBreak > 30)
                            counterSecondsBreak = counterSecondsBreak - 30;
                        edit_break.setText(secondsToString(counterSecondsBreak));
                        vibration(false);
                    }
                });
                break;
            case "EMOM":

                timer_round_title.setText("Every");
                layout_title.setText("EMOM");
                timer_works_title.setText("Rounds");
                edit_works.setText("10");

                edit_rounds.setText("01:00");
                counterRounds = 10;

                rounds_nb.setText(roundToFormat(counterStarter_emom));
                rds = counterRounds * 60; // nb seconds total

                timer_layout.setVisibility(View.GONE);
                timer_break.setVisibility(View.GONE);

                plus_rounds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (counterSecondsWorksEmom >= 30 && counterSecondsWorksEmom <= 570) {
                            counterSecondsWorksEmom = counterSecondsWorksEmom + 30;
                            edit_rounds.setText(secondsToString(counterSecondsWorksEmom));
                        } else if (counterSecondsWorksEmom >= 600 && counterSecondsWorksEmom <= 1740) {
                            counterSecondsWorksEmom = counterSecondsWorksEmom + 60;
                            edit_rounds.setText(secondsToString(counterSecondsWorksEmom));
                        }
                        vibration(false);

                    }
                });

                moins_rounds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (counterSecondsWorksEmom >= 60 && counterSecondsWorksEmom <= 600) {
                            counterSecondsWorksEmom = counterSecondsWorksEmom - 30;
                            edit_rounds.setText(secondsToString(counterSecondsWorksEmom));
                        } else if (counterSecondsWorksEmom >= 600) {
                            counterSecondsWorksEmom = counterSecondsWorksEmom - 60;
                            edit_rounds.setText(secondsToString(counterSecondsWorksEmom));
                        }
                        vibration(false);
                    }
                });

                plus_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterRounds++;
                        edit_works.setText(convert(counterRounds));
                        vibration(false);
                    }
                });

                moins_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (counterRounds > 1) {
                            counterRounds--;
                            edit_works.setText(convert(counterRounds));
                            vibration(false);
                        }
                    }
                });


                break;
            case "FORTIME":

                layout_title.setText("FORTIME");

                plus_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        counterTimeWorksDesc = counterTimeWorksDesc + 60;
                        edit_works.setText(secondsToString(counterTimeWorksDesc));
                        vibration(false);
                    }
                });

                moins_works.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (counterTimeWorksDesc > 60) {
                            counterTimeWorksDesc = counterTimeWorksDesc - 60;
                            edit_works.setText(secondsToString(counterTimeWorksDesc));
                            vibration(false);
                        }
                    }
                });

                btn_plus_rd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        roundUpdate++;
                        totalRound_tv.setText("Round : " + roundToFormat(roundUpdate));
                        if (roundUpdate >= 1) {
                            btn_minus_rd.setVisibility(View.VISIBLE);
                        }
                        vibration(false);
                    }
                });

                btn_minus_rd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (roundUpdate > 0) {
                            roundUpdate--;
                            totalRound_tv.setText("Round : " + roundToFormat(roundUpdate));
                        }
                        vibration(false);
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

                            time_works_fortime.setVisibility(View.INVISIBLE);
                            isTimeCap = false;
                        }
                        vibration(false);
                    }
                });
                break;
            default:
                Log.d("no type", "any value ");
        }

    }

    private void setSound(int time) {


        if (time <= 5 && time > 0) {

            soundPool.play(sound1, 1, 1, 0, 0, 1);

        } else if (time == 0) {
            Log.d("play", "0");
            soundPool.play(sound2, 1, 1, 0, 0, 1);

        }
    }

    private void vibration(boolean oneOrTwo) {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {1000, 2000};
        /*v.vibrate(pattern,3 );*/
        if (Build.VERSION.SDK_INT >= 26 /*Build.VERSION_CODES.O*/) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            Log.d("ju", "vibration: ");
        } else {

            if (v.hasVibrator()) {
                if (oneOrTwo) {
                    v.vibrate(250);
                    v.vibrate(250);
                } else {
                    v.vibrate(AudioAttributes.USAGE_ASSISTANT);
                }
                Log.v("Can Vibrate", "YES");
            } else {
                Log.v("Can Vibrate", "NO");
            }
        }
    }

    private void runPretimer(final String type) {

        preTimerIsRunning = true;
        main_layout.setVisibility(View.GONE);
        timer_layout.setVisibility(View.VISIBLE);
        rounds_nb.setText("00");
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep1);

        countDownTimer = new CountDownTimer(counterToLong(pre_start), 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                pre_start--;
                time_timer.setText(secondsToString(pre_start));
                setSound(pre_start);
            }

            @Override
            public void onFinish() {
                rounds_nb.setText("UP ");
                pre_start--;
                time_timer.setText(secondsToString(pre_start));
                setSound(pre_start);
                timer_layout.setVisibility(View.VISIBLE);
                logo_strivee_counter.setVisibility(View.VISIBLE);
                runTimer(type);
            }
        }.start();

        preTimerIsRunning = true;
    }

    private void runTimer(String type) {

        preTimerIsRunning = false;
        if (type.equals("AMRAP")) {

            totalRound.setVisibility(View.VISIBLE);
            runAmrap();

        } else if (type.equals("TABATA")) {

            counterStarter_T_I = 1;
            rounds_nb.setText(roundToFormat(counterStarter_T_I));
            counterWorkTime = counterTimeWorksDesc;
            runTabata(type);

        } else if (type.equals("INTERVAL")) {

            counterStarter_T_I = 1;
            rounds_nb.setText(roundToFormat(counterStarter_T_I));
            counterWorkTime = counterTimeWorksDesc;
            runTabata(type);

        } else if (type.equals("EMOM")) {

            counterStarter_emom = 1;
            rounds_nb.setText(roundToFormat(counterStarter_emom));
            runEmom();

        } else if (type.equals("FORTIME")) {

            totalRound.setVisibility(View.VISIBLE);
            runFortime();
        }
    }

}
