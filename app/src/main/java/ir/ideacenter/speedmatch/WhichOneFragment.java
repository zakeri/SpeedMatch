package ir.ideacenter.speedmatch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class WhichOneFragment extends Fragment {
    private final int GAME_LEVEL_MAX = 10;
    private final int GAME_TIME_MAX_MILLIS = 10000;
    private final int GAME_TIME_TICK_MILLIS = 1000;
    private final int NUMBER_RANGE = 30;
    private final int LEFT_BUTTON = 0;
    private final int RIGHT_BUTTON = 1;
    private final int EQUAL_BUTTON = 2;

    TextView userScoreBoard;
    TextView gameLevelBoard;
    Button leftButton;
    Button rightButton;
    Button equalButton;
    LinearLayout gameContainer;
    TextView gameCountDown;

    private int gameLevel;
    private int userScore;
    private int currentLeftNumber;
    private int currentRightNumber;
    private String playerName;
    private Boolean gameFinished;
    private int countDownInt = 3;

    CountDownTimer gameTimer;

    private void readArguments(Bundle bundle) {
        playerName = getArguments().getString("player_name", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_which_one_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readArguments(savedInstanceState);

        gameLevel = 0;
        userScore = 0;
        currentLeftNumber = 0;
        currentRightNumber = 0;
        gameFinished = false;



        findViews(view);
        setButtonOnClicks();
        startCountDown();
        //startGame();
    }

    private void startCountDown() {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(
                gameCountDown,
                "rotation",
                0f, 45f, -45f, 45f, 0
        );
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
                gameCountDown,
                "scaleX",
                1f, 3f, 1f, 2f
        );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
                gameCountDown,
                "scaleY",
                1f, 3f, 1f, 2f
        );
        ObjectAnimator alpha = ObjectAnimator.ofFloat(
                gameCountDown,
                "alpha",
                0.7f, 1f, 0.5f, 0f
        );
        ObjectAnimator translationX = ObjectAnimator.ofFloat(
                gameCountDown,
                "translationY",
                0f, 100f, -200f
        );
        rotation.setDuration(1000);
        scaleX.setDuration(1000);
        scaleY.setDuration(1000);
        alpha.setDuration(1000);
        translationX.setDuration(1000);

        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(rotation, scaleX, scaleY, alpha, translationX);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                gameCountDown.setText(String.valueOf(countDownInt));
                countDownInt--;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (countDownInt > 0)
                    startCountDown();
                else {
                    gameCountDown.setVisibility(View.INVISIBLE);
                    startGame();
                }
            }
        });
        animation.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        gameTimer.cancel();
    }

    private void startGame() {
        gameContainer.setVisibility(View.VISIBLE);
        gameTimer = new CountDownTimer(GAME_TIME_MAX_MILLIS, GAME_TIME_TICK_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                gameLevelBoard.setText(getString(R.string.game_timer, (int) (millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                gameFinished = true;
                gameLevelBoard.setText(getString(R.string.game_finished));
                HighScoreList hsl = SharedPreferenceManager.getInstance(getActivity()).getWhichOneHighScoreList();
                HighScoreUser hsu = new HighScoreUser();
                hsu.setHighScore(userScore);
                hsu.setUserName(playerName);
                hsl.addHighScoreUser(hsu);
                SharedPreferenceManager.getInstance(getActivity()).putWhichOneHighScoreList(hsl);
            }
        };
        gameTimer.start();
        generateNumbers();
        gameLevel++;
        updateBoards();
    }

    private void setButtonOnClicks() {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameFinished) return;
                evaluate(LEFT_BUTTON);
                generateNumbers();
                updateBoards();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameFinished) return;
                evaluate(RIGHT_BUTTON);
                generateNumbers();
                updateBoards();
            }
        });

        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameFinished) return;
                evaluate(EQUAL_BUTTON);
                generateNumbers();
                updateBoards();
            }
        });
    }

    private void evaluate(int whichPressed) {
        if (whichPressed == LEFT_BUTTON) {
            if (currentLeftNumber > currentRightNumber) userScore++;
        }
        else if (whichPressed == RIGHT_BUTTON) {
            if (currentLeftNumber < currentRightNumber) userScore++;
        }
        else if (whichPressed == EQUAL_BUTTON) {
            if (currentLeftNumber == currentRightNumber) userScore++;
        }

        /*if (gameLevel == GAME_LEVEL_MAX) {
            gameFinished = true;
        }
        else {
            gameLevel++;
        }*/
    }

    private void updateBoards() {
        userScoreBoard.setText(getString(R.string.user_points, userScore));
        /*if (!gameFinished) {
            gameLevelBoard.setText(getString(R.string.game_level, gameLevel, GAME_LEVEL_MAX));
        }
        else
            gameLevelBoard.setText(getString(R.string.game_finished));*/

    }

    private void generateNumbers() {
        if (gameFinished) return;
        currentLeftNumber = generateInt();
        currentRightNumber = generateInt();
        leftButton.setText(String.valueOf(currentLeftNumber));
        rightButton.setText(String.valueOf(currentRightNumber));
    }

    private void findViews(View view) {
        leftButton = (Button) view.findViewById(R.id.which_one_left_button);
        rightButton = (Button) view.findViewById(R.id.which_one_right_button);
        equalButton = (Button) view.findViewById(R.id.which_one_equal_button);
        userScoreBoard = (TextView) view.findViewById(R.id.which_one_user_score);
        gameLevelBoard = (TextView) view.findViewById(R.id.which_one_game_level);
        gameContainer = (LinearLayout) view.findViewById(R.id.which_one_game_container);
        gameCountDown = (TextView) view.findViewById(R.id.which_one_game_count_down);
    }

    private int generateInt() {
        Random randomInt = new Random();
        int i = randomInt.nextInt();
        if (i < 0) i *= -1;
        return (i % NUMBER_RANGE);
    }
}
