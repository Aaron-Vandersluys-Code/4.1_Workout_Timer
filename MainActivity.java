package com.example.workouttimerapplication;

// Importing required components into the project
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /*
    Declaring of instance variables for each
    of the components of the application.
     */
    EditText workoutInput;
    EditText restInput;
    ProgressBar phaseProgress;
    CountDownTimer workoutTimer;
    CountDownTimer restTimer;
    TextView displayWorkout;
    TextView displayRest;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mapping XML attributes to code
        workoutInput = findViewById(R.id.workoutInput);
        restInput = findViewById(R.id.restInput);
        phaseProgress = findViewById(R.id.phaseProgress);
        displayWorkout = findViewById(R.id.displayWorkout);
        displayRest = findViewById(R.id.displayRest);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);

        /*
        On click listener event for each of the two buttons
        (start and stop). This code starts the timer when the
        user starts the timer, and stops it when they press the
        stop button.
         */
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stopTimer();
            }
        });
    }

    /*
    Built in Android MediaPlayer class. This code will check if there is
    an instance of MediaPlayer and release the associated resources. Then
    it fetches the default Android notification sound, creates a newly
    required media player instance and plays the sound. This allows my
    application to play a sound when the users timer ends.
     */
    private void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mediaPlayer = MediaPlayer.create(this, notification);
        mediaPlayer.start();
    }

    /*
    This method implements CountDownTimer to provide functionality for the
    workout timer. The method first takes the users inputted workout and
    rest times, and sets the combined value of to the progress bar. Instead
    of using a handler and runner, I have utilised CountDownTimers OnTick
    method to update the seconds remaining and progress bar for the user. I
    personally found it made more sense to utilise the options available in
    CountDownTimer. Lastly, the onFinish method calls the media player player
    method and plays a sound to alert the user their timer has finished, and then
    starts the rest timer.
     */
    private void startTimer() {
        final int workoutTime = Integer.parseInt(workoutInput.getText().toString());
        final int restTime = Integer.parseInt(restInput.getText().toString());

        phaseProgress.setMax(workoutTime);

        workoutTimer = new CountDownTimer(workoutTime * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                phaseProgress.setProgress((int) secondsRemaining);
                displayWorkout.setText("Your remaining workout time is: " + secondsRemaining + " seconds");
            }

            public void onFinish() {
                playSound();
                startRestTimer(restTime);
            }
        }.start();
    }

    /*
    A second instance of CountDownTimer, utilised for the rest timer. Functionality
    is the same as the previous instance, but instead is used for the rest timer.
     */
    private void startRestTimer(int restTime) {
        phaseProgress.setMax(restTime);

        restTimer = new CountDownTimer(restTime * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                phaseProgress.setProgress((int) secondsRemaining);
                displayRest.setText("Your remaining rest time is: " + secondsRemaining + " seconds");
            }

            public void onFinish() {
                playSound();
            }
        }.start();
    }

    /*
    This method stops the timers after they have completed running. If the
    workout and rest timers have been initialized they are cancelled (stopped).
     */
    private void stopTimer() {
        if (workoutTimer != null) {
            workoutTimer.cancel();
        }
        if (restTimer != null) {
            restTimer.cancel();
        }
    }
}