package org.golanamit.casiowatchgameretro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView userNumTxt, scoreTxt;
    TextView[] numTv ;
    Button decBtn, incBtn, interceptBtn;

    private Board brd;
    TimerTask repeatedTask;
    Timer timer;

    //  index 0 = easy, 1 = medium, 2 = hard
    private static final long[] PERIODS = {1000L * 3L, 1000L * 2L, 1000L * 1L};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setListeners();

        startGame();
    }

    public void startGame() {
        clearTextArray();
        brd.resetArray();
        brd.setGameLevel(WelcomeSetLevel.gameLevel);
        long period = PERIODS[WelcomeSetLevel.gameLevel.ordinal()];
        long delay = 1000L;
        repeatedTask = new TimerTask() {
            @Override
            public void run() {
                brd.fillArrElemRnd();
                displayTextArray();
                boolean lost = brd.didWeLoose();
                if(lost) {
                    cancel();
                    endGame("LOST");
                    return;
                }
                boolean won = brd.didWeWeen();
                if(won) {
                    cancel();
                    endGame("WON");
                    return;
                }
                brd.makeStepForward();
            }
        };
        timer = new Timer("Timer");
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
    }

    private void endGame(String state) {
        String msg = "You " + (state.equals("LOST") ? "Loose" : "Win");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        scoreTxt.setText("");
        Intent intent = new Intent(MainActivity.this, EndGame.class);
        intent.putExtra("SCORE", String.valueOf(brd.getScore()));
        intent.putExtra("STATE", state);
        startActivity(intent);
    }

    private void clearTextArray() {
        for(int i = 0; i < Board.ARR_SIZE; i++) {
            numTv[i].setText(Board.SPACE);
        }
    }

    private void displayTextArray() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < Board.ARR_SIZE; i++) {
                    String text = brd.returnArrayElementByIndex(i);
                    numTv[i].setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                    numTv[i].setTypeface(null, Typeface.NORMAL);
                    numTv[i].setTextSize(14F);

                    if(i == brd.returnLastFilledElemIndex()) {
                        numTv[i].setTextColor(ContextCompat.getColor(MainActivity.this, R.color.blue));
                        numTv[i].setTypeface(null, Typeface.BOLD);
                        numTv[i].setTextSize(18F);
                    }
                    numTv[i].setText(text);
                    //System.out.println("set #" + i + " to " + text);
                }
            }
        });
        brd.printArray();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setListeners() {
        incBtn.setOnClickListener(this);
        decBtn.setOnClickListener(this);
        interceptBtn.setOnClickListener(this);
    }

    private void init() {
        brd = new Board();
        userNumTxt = findViewById(R.id.userNumTxtId);
        scoreTxt = findViewById(R.id.scoretxtId);
        numTv = new TextView[10];
        numTv[0] = findViewById(R.id.tvId0);
        numTv[1] = findViewById(R.id.tvId1);
        numTv[2] = findViewById(R.id.tvId2);
        numTv[3] = findViewById(R.id.tvId3);
        numTv[4] = findViewById(R.id.tvId4);
        numTv[5] = findViewById(R.id.tvId5);
        numTv[6] = findViewById(R.id.tvId6);
        numTv[7] = findViewById(R.id.tvId7);
        numTv[8] = findViewById(R.id.tvId8);
        numTv[9] = findViewById(R.id.tvId9);
        for(int i = 0; i < numTv.length; i++) {
            numTv[i].setGravity(Gravity.CENTER);
        }

        decBtn = findViewById(R.id.decIdBtnId);
        incBtn = findViewById(R.id.incBtnId);
        interceptBtn = findViewById(R.id.interceptBtnId);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == decBtn.getId()) {
            brd.decreaseUserNumber();
            userNumTxt.setText(brd.getUserNum());
            //System.out.println("decrement was clicked");
        } else if(v.getId() == incBtn.getId()) {
            brd.increaseUserNumber();
            userNumTxt.setText(brd.getUserNum());
            //System.out.println("increment was clicked");
        } else if(v.getId() == interceptBtn.getId()) {
            //System.out.println("Intercept was clicked");
            boolean hit = brd.matchHit(userNumTxt.getText().toString());
            MediaPlayer mp ;
            if(hit) {
                mp = MediaPlayer.create(this, R.raw.hit);
            } else {
                mp = MediaPlayer.create(this, R.raw.miss);
            }
            mp.start();
            //displayTextArray();
            scoreTxt.setText(String.valueOf(brd.getScore()));
        }
    }
}