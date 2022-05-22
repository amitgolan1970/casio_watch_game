package org.golanamit.casiowatchgameretro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeSetLevel extends AppCompatActivity implements View.OnClickListener {

    SeekBar seekBar;
    TextView gameLevelInfoTxtTv;
    Button startGameBtn;
    public static GameLevel gameLevel = GameLevel.MEDIUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_set_level);

        init();

        setlisteners();
    }

    private void init() {
        seekBar = findViewById(R.id.seekBarId);
        gameLevelInfoTxtTv = findViewById(R.id.gamelevelInfoTxtId);
        startGameBtn = findViewById(R.id.startGameBtnId);
    }

    private void setlisteners() {
        startGameBtn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
                switch (progress) {
                    case 0:
                        gameLevel = GameLevel.EASY;
                        break;
                    case 1:
                        gameLevel = GameLevel.MEDIUM;
                        break;
                    case 2:
                        gameLevel = GameLevel.HARD;
                        break;
                    default:
                        System.err.println("Should never reach here");
                }
                gameLevelInfoTxtTv.setText("Level: " + gameLevel.name());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == startGameBtn.getId()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("GAMELEVEL", gameLevel.name());
            startActivity(intent);
        }
    }
}