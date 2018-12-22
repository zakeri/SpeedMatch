package ir.ideacenter.speedmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rilixtech.materialfancybutton.MaterialFancyButton;


public class MainActivity extends AppCompatActivity {

    MaterialFancyButton speedMatchGame;
    MaterialFancyButton whichOneGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        configure();
    }

    private void findViews() {
        speedMatchGame = (MaterialFancyButton) findViewById(R.id.game_speed_match);
        whichOneGame = (MaterialFancyButton) findViewById(R.id.game_which_one);
    }

    private void configure() {
        speedMatchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SpeedMatchActivity.class));
            }
        });

        whichOneGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WhichOneActivity.class));
            }
        });
    }
}
