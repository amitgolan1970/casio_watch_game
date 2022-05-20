package org.golanamit.casiowatchgameretro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndGame extends AppCompatActivity implements View.OnClickListener {

    TextView statusTxt, scoreInfoTxt;
    Button yesBtn, noBtn;

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
        yesBtn = findViewById(R.id.yesBtnId);
        noBtn = findViewById(R.id.noBtnId);
    }

    private void setListeners() {
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == yesBtn.getId()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if(v.getId() == noBtn.getId()) {
            this.finishAffinity();
            System.exit(0);
            finishAndRemoveTask();
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
        statusTxt.setTextColor(ContextCompat.getColor(this, R.color.teal_700));
        if(state.equals("LOST"))
            statusTxt.setTextColor(ContextCompat.getColor(this, R.color.red));
        scoreInfoTxt.setText("Score " + score);
    }
}