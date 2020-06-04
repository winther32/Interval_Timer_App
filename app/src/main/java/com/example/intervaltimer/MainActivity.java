package com.example.intervaltimer;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.SystemClock;


public class MainActivity extends AppCompatActivity {

    //Declare Vars used
    TextView display;
    Button start, stop, reset, set;
    Handler handler;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int inputSeconds, Seconds, Minutes, MilliSeconds, SetTime ;

    EditText setSec;

    MediaPlayer shortBeep;

    // Spinner Example
//    Spinner setSec;
//    String secList[] = {"1","2","3","4","5","6","7","8","9","10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shortBeep = MediaPlayer.create(this, R.raw.short_beep01);

        // Create the buttons and timer in code land to match layout land
        display = (TextView)findViewById(R.id.display);
        start = (Button)findViewById(R.id.startButton);
        stop  = (Button)findViewById(R.id.stopButton);
        reset = (Button)findViewById(R.id.resetButton);
        set = (Button)findViewById(R.id.setTimer);
        handler = new Handler();

        setSec = (EditText)findViewById(R.id.setSeconds);

        // Spinner Example
//        Spinner setSec = (Spinner)findViewById(R.id.spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, secList);
//        setSec.setAdapter(adapter);

//        set.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                inputSeconds = Integer.valueOf(setSec.getText().toString());
//
//                Seconds = inputSeconds % 60 ;
//                Minutes =  inputSeconds / 60 ;
//                MilliSeconds = 0 ;
//
//                display.setText("" + String.format("%02d", Minutes) + ":"
//                        + String.format("%02d", Seconds) + "."
//                        + String.format("%03d", MilliSeconds));
//            }
//        });

        // Ex. use number pad ENTER/Done click to do an action
        setSec.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Check if null entry
                if (setSec.getText().toString().trim().length() == 0) {
                    inputSeconds = 0;
                } else {
                    inputSeconds = Integer.valueOf(setSec.getText().toString());
                }

                SetTime = inputSeconds;
                TimeBuff = inputSeconds * 1000; //TimeBuff in millisec

                Seconds = inputSeconds % 60 ;
                Minutes =  inputSeconds / 60 ;
                MilliSeconds = 0 ;

                display.setText("" + String.format("%02d", Minutes) + ":"
                        + String.format("%02d", Seconds) + "."
                        + String.format("%03d", MilliSeconds));

                stop.setEnabled(true);
                return false;
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StartTime = SystemClock.elapsedRealtime();
                handler.postDelayed(runnable, 0);

                reset.setEnabled(false);
                set.setEnabled(false);
                stop.setEnabled(true);

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff -= MillisecondTime;

                handler.removeCallbacks(runnable);

                reset.setEnabled(true);
                set.setEnabled(true);
                stop.setEnabled(false);

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = SetTime * 1000;
                UpdateTime = 0L ;

                Seconds = SetTime % 60;
                Minutes = SetTime / 60;
                MilliSeconds = 0 ;

                display.setText("" + String.format("%02d", Minutes) + ":"
                        + String.format("%02d", Seconds) + "."
                        + String.format("%03d", MilliSeconds));

                stop.setEnabled(true);
            }
        });

    }

    // Countdown Runnable
    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.elapsedRealtime() - StartTime;
            UpdateTime = TimeBuff - MillisecondTime;

            // Check if timer is at 0 or close enough :/ (hundreth sec accuracy I think)
            if (UpdateTime <= 0) {
                shortBeep.start();

                display.setText("00:00.000");
                handler.removeCallbacks(runnable);

                // Button toggle on/off
                reset.setEnabled(true);
                set.setEnabled(true);
                stop.setEnabled(false);
            } else { // Continue countdown
                Seconds = (int) (UpdateTime / 1000);
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;
                MilliSeconds = (int) (UpdateTime % 1000);
                display.setText("" + String.format("%02d", Minutes) + ":"
                        + String.format("%02d", Seconds) + "."
                        + String.format("%03d", MilliSeconds));
                handler.postDelayed(this, 0);
            }
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
