package org.golanamit.casiowatchgameretro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EndGame extends AppCompatActivity implements View.OnClickListener {

    TextView statusTxt, scoreInfoTxt, highScoreTxt;
    Button yesBtn, noBtn, clearHighScoreBtn, chooseLevelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        init();

        setListeners();

        showPage();
    }

    private void init() {
        statusTxt = findViewById(R.id.statusTxtId);
        scoreInfoTxt = findViewById(R.id.scoringInfoTxtId);
        highScoreTxt = findViewById(R.id.highScoreInfotxtId);
        yesBtn = findViewById(R.id.yesBtnId);
        yesBtn.setBackgroundColor(getResources().getColor(R.color.teal_700));
        noBtn = findViewById(R.id.noBtnId);
        noBtn.setBackgroundColor(getResources().getColor(R.color.red));
        clearHighScoreBtn = findViewById(R.id.clearHighScoreBtnId);
        chooseLevelBtn = findViewById(R.id.chooseGameLevelBtnId);
    }

    private void setListeners() {
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
        clearHighScoreBtn.setOnClickListener(this);
        chooseLevelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == yesBtn.getId()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("GAMELEVEL", WelcomeSetLevel.gameLevel.name());
            startActivity(intent);
        } else if(v.getId() == noBtn.getId()) {
            this.finishAffinity();
            System.exit(0);
            finishAndRemoveTask();
        } else if(v.getId() == clearHighScoreBtn.getId()) {
            clearHighScoreBtn.setVisibility(View.INVISIBLE);
            highScoreTxt.setVisibility(View.INVISIBLE);
            SharedPreferences prefs = getSharedPreferences("score", Context.MODE_PRIVATE);
            prefs.edit().clear().commit();
            prefs.edit().remove("score").commit();
        } else if(v.getId() == chooseLevelBtn.getId()) {
            Intent intent = new Intent(this, WelcomeSetLevel.class);
            startActivity(intent);
        }
    }

    private void showPage() {
        String score = "0";
        String state = "LOST";
        try {
            score = getIntent().getStringExtra("SCORE");
            state = getIntent().getStringExtra("STATE");
        } catch (Exception e) {
        }
        statusTxt.setText("You " + state);
        MediaPlayer mp ;
        mp = MediaPlayer.create(this, R.raw.win_ya);
        statusTxt.setTextColor(ContextCompat.getColor(this, R.color.teal_700));
        if(state.equals("LOST")) {
            mp = MediaPlayer.create(this, R.raw.fail_loose);
            statusTxt.setTextColor(ContextCompat.getColor(this, R.color.red));
        }
        mp.start();
        scoreInfoTxt.setText("Score " + score);
        handleHighScore(score);
    }

    private void handleHighScore(String score) {
        SharedPreferences prefs = getSharedPreferences("score", Context.MODE_PRIVATE);
        String scoreFromSP = prefs.getString("score", null);
        if(scoreFromSP == null) {
            clearHighScoreBtn.setVisibility(View.INVISIBLE);
            highScoreTxt.setVisibility(View.INVISIBLE);
            if(!score.equals("0")) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("score", score);
                editor.apply();
                Toast.makeText(getApplicationContext(), "registering high score for the first time. congratulation",
                        Toast.LENGTH_SHORT).show();
            }
            return;
        }
        highScoreTxt.setText("High score: " + scoreFromSP);
        clearHighScoreBtn.setVisibility(View.VISIBLE);
        highScoreTxt.setVisibility(View.VISIBLE);
        int scoreCurrInt = Integer.parseInt(score);
        int scoreSpInt = Integer.parseInt(scoreFromSP);
        if(scoreCurrInt > scoreSpInt) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("score", score);
            editor.apply();
            highScoreTxt.setText("Last high score record: " + scoreFromSP);
            Toast.makeText(getApplicationContext(), "new high score record. congratulation", Toast.LENGTH_SHORT).show();
        }
    }
}