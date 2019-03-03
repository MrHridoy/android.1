package com.example.mrhridoy.timerandstopwatch;

import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText timer;
    private TextView textView2;
    private Button timer_set,timer_stop;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds;
    private long timeBlinkInMilliseconds;
    private boolean blink;
    public TextView textView;
    public Button start, pause, reset, lap;

    public long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    public int Seconds, Minutes, MilliSeconds ;
    public ListView listView ;
    public String[] ListElements = new String[] {  };
    public List<String> ListElementsArrayList ;
    public ArrayAdapter<String> adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer=(EditText) findViewById(R.id.text_input);
        textView2=(TextView) findViewById(R.id.text_output);
        timer_set=(Button)findViewById(R.id.btn1);
        timer_stop=(Button)findViewById(R.id.btn2);

        timer_set.setOnClickListener(this);
        timer_stop.setOnClickListener(this);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View v) {
        if (v.getId() == R.id.btn1) {
            setTimer();
            timer_stop.setVisibility(View.VISIBLE);
            timer_set.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            timer.setText("");
            startTimer();

        } else if (v.getId() == R.id.btn2) {
            countDownTimer.cancel();
            timer_set.setVisibility(View.VISIBLE);
            timer_stop.setVisibility(View.GONE);
            timer.setVisibility(View.VISIBLE);
        }
    }

    private void setTimer() {
        int time = 0;
        if (!timer.getText().toString().equals("")) {
            time = Integer.parseInt(timer.getText().toString());
        } else
            Toast.makeText(MainActivity.this, "Please Enter Minutes...",
                    Toast.LENGTH_LONG).show();

        totalTimeCountInMilliseconds = 60 * time * 1000;

        timeBlinkInMilliseconds = 30 * 1000;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
            // 500 means, onTick function will be called at every 500
            // milliseconds

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;

                if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {

                    if (blink) {
                        textView2.setVisibility(View.VISIBLE);
                        // if blink is true, textview will be visible
                    } else {
                        textView2.setVisibility(View.INVISIBLE);
                    }

                    blink = !blink; // toggle the value of blink
                }

                textView2.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));
                // format the textview to show the easily readable format

            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                textView2.setText("Time up!");
                textView2.setVisibility(View.VISIBLE);
                timer_set.setVisibility(View.VISIBLE);
                timer_stop.setVisibility(View.GONE);
                timer.setVisibility(View.VISIBLE);
            }

        }.start();









        textView = (TextView)findViewById(R.id.text_view);
        start = (Button)findViewById(R.id.btn_start);
        pause = (Button)findViewById(R.id.btn_pause);
        reset = (Button)findViewById(R.id.btn_restart);
        lap = (Button)findViewById(R.id.btn_lap) ;
        listView = (ListView)findViewById(R.id.listview1);
        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                ListElementsArrayList);

        listView.setAdapter(adapter);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartTime = SystemClock.uptimeMillis();
                start.postDelayed(runnable,0);

                reset.setEnabled(false);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff += MillisecondTime;

                pause.removeCallbacks(runnable);

                reset.setEnabled(true);

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                textView.setText("00:00:00");

                ListElementsArrayList.clear();

                adapter.notifyDataSetChanged();
            }
        });

        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListElementsArrayList.add(textView.getText().toString());

                adapter.notifyDataSetChanged();

            }
        });

    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            textView.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            textView.postDelayed(this,0);
        }

    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}