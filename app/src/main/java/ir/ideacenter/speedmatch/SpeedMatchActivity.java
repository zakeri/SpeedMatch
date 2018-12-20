package ir.ideacenter.speedmatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class SpeedMatchActivity extends AppCompatActivity {

    Button startGame;
    Button highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_match);

        findViews();
        configure();
    }

    private void findViews() {
        startGame = (Button) findViewById(R.id.start_game);
        highScores = (Button) findViewById(R.id.high_score);
    }

    private void configure() {
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNameDialog getNameDialog = new GetNameDialog(
                        SpeedMatchActivity.this,
                        new OnNameSelectedListener() {
                            @Override
                            public void onNameSelected(String name) {
                                Log.d("TAG", "playerName = " + name);

                                SpeedMatchFragment gameFragment = new SpeedMatchFragment();

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
                String gameName = getString(R.string.game_title_speed_match);
                HighScoreFragment highScoreFragment = HighScoreFragment.newInstance(gameName);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_high_score, highScoreFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
