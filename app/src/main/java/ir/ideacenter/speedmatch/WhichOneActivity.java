package ir.ideacenter.speedmatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rilixtech.materialfancybutton.MaterialFancyButton;


public class WhichOneActivity extends AppCompatActivity {

    MaterialFancyButton startGame;
    MaterialFancyButton highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_which_one);

        findViews();
        configure();
    }

    private void findViews() {
        startGame = (MaterialFancyButton) findViewById(R.id.start_game);
        highScores = (MaterialFancyButton) findViewById(R.id.high_score);
    }

    private void configure() {
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNameDialog getNameDialog = new GetNameDialog(
                        WhichOneActivity.this,
                        new OnNameSelectedListener() {
                            @Override
                            public void onNameSelected(String name) {
                                Log.d("TAG", "playerName = " + name);

                                WhichOneFragment gameFragment = new WhichOneFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString("player_name", name);
                                gameFragment.setArguments(bundle);

                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_game_container, gameFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                getNameDialog.show();

            }
        });

        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameName = getString(R.string.game_title_which_one);
                HighScoreFragment highScoreFragment = HighScoreFragment.newInstance(gameName);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_high_score, highScoreFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
