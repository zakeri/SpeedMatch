package ir.ideacenter.speedmatch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class SpeedMatchFragment extends Fragment {
    private enum CardShape {
        HEART, STAR, CIRCLE
    };

    private enum CardColor {
        RED, GREEN, BLUE, YELLOW
    };

    private final int GAME_LEVEL_MAX = 10;
    private final int GAME_TIME_MAX_MILLIS = 20000;
    private final int GAME_TIME_TICK_MILLIS = 1000;
    private final int CARD_SHAPE_TOTAL = CardShape.values().length;
    private final int CARD_COLOR_TOTAL = CardColor.values().length;
    private final int BOTH_BUTTON = 0;
    private final int ONE_BUTTON = 1;
    private final int NONE_BUTTON = 2;


    TextView userScoreBoard;
    TextView gameLevelBoard;
    Button bothButton;
    Button oneButton;
    Button noneButton;
    ConstraintLayout gameContainer;
    TextView gameCountDown;
    ImageView rightTick;
    ImageView wrongTick;
    CardView questionCard;
    ImageView questionValue;

    private int userScore;
    private CardShape currentCardShape;
    private CardShape previousCardShape;
    private CardColor currentCardColor;
    private CardColor previousCardColor;
    private String playerName;
    private Boolean gameFinished;
    private int countDownInt = 3;
    private Boolean questionBeingChanged;

    CountDownTimer gameTimer;

    private void readArguments(Bundle bundle) {
        playerName = getArguments().getString("player_name", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_speed_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readArguments(savedInstanceState);

        userScore = 0;
        gameFinished = false;
        questionBeingChanged = true;

        findViews(view);
        setButtonOnClicks();
        startCountDown();
        //startGame();
    }

    @Override
    public void onPause() {
        super.onPause();
        gameTimer.cancel();
    }

    private void startGame() {
        gameCountDown.setVisibility(View.INVISIBLE);
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
                HighScoreList hsl = SharedPreferenceManager.getInstance(getActivity()).getSpeedMatchHighScoreList();
                HighScoreUser hsu = new HighScoreUser();
                hsu.setHighScore(userScore);
                hsu.setUserName(playerName);
                hsl.addHighScoreUser(hsu);
                SharedPreferenceManager.getInstance(getActivity()).putSpeedMatchHighScoreList(hsl);
            }
        };
        generateQuestion();
        initBoards();
        gameTimer.start();
    }

    private void setButtonOnClicks() {
        bothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameFinished || questionBeingChanged) return;
                questionBeingChanged = true;
                boolean isCorrect = evaluate(BOTH_BUTTON);
                generateQuestion();
                updateBoards(isCorrect, false);
            }
        });

        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameFinished || questionBeingChanged) return;
                questionBeingChanged = true;
                boolean isCorrect = evaluate(ONE_BUTTON);
                generateQuestion();
                updateBoards(isCorrect, false);
            }
        });

        noneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameFinished || questionBeingChanged) return;
                questionBeingChanged = true;
                boolean isCorrect = evaluate(NONE_BUTTON);
                generateQuestion();
                updateBoards(isCorrect, false);
            }
        });
    }

    private boolean evaluate(int whichPressed) {
        boolean shapeEqual = (currentCardShape == previousCardShape);
        boolean colorEqual = (currentCardColor == previousCardColor);
        if (((whichPressed == BOTH_BUTTON) && (shapeEqual && colorEqual)) ||
                ((whichPressed == ONE_BUTTON) && (shapeEqual ^ colorEqual)) ||
                ((whichPressed == NONE_BUTTON) && (!shapeEqual && !colorEqual)))
        {
            return true;
        }
        return false;
    }

    private void initBoards() {
        rightTick.setVisibility(View.INVISIBLE);
        wrongTick.setVisibility(View.INVISIBLE);
        currentCardColor = generateCardColor();
        currentCardShape = generateCardShape();
        updateQuestionValue();
        userScoreBoard.setText(getString(R.string.user_points, userScore));

        updateBoards(false, true);
    }

    private void updateBoards(boolean currentAnswer, boolean initUpdate) {
        if (currentAnswer) userScore++;
        userScoreBoard.setText(getString(R.string.user_points, userScore));
        if (!initUpdate) showTick(currentAnswer);
        changeQuestionCard();
    }

    private void changeQuestionCard() {
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(
                questionCard,
                "alpha",
                1f, 1f, 1f, 1f, 0.5f, 0f
        );
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(
                questionCard,
                "alpha",
                0f, 0.5f, 1f, 1f, 1f
        );
        ObjectAnimator translationX1 = ObjectAnimator.ofFloat(
                questionCard,
                "translationX",
                -100f, -200f, -300f, -400f, -400f, -400f
        );
        ObjectAnimator translationX2 = ObjectAnimator.ofFloat(
                questionCard,
                "translationX",
                400f, 400f, 400f, 300f, 200f, 100f, 0f
        );
        alpha1.setDuration(200);
        translationX1.setDuration(200);
        alpha2.setDuration(200);
        translationX2.setDuration(200);

        AnimatorSet animation1 = new AnimatorSet();
        final AnimatorSet animation2 = new AnimatorSet();
        animation1.playTogether(alpha1, translationX1);
        animation2.playTogether(alpha2, translationX2);
        animation1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                updateQuestionValue();
                animation2.start();
            }
        });
        animation2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                questionBeingChanged = false;
            }
        });
        animation1.start();
    }

    private void showTick(final boolean isRight) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
                (isRight) ? rightTick : wrongTick,
                "scaleX",
                1f, 2f, 3f, 4f, 4f, 4f, 4f
        );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
                (isRight) ? rightTick : wrongTick,
                "scaleY",
                1f, 2f, 3f, 4f, 4f, 4f, 4f
        );
        ObjectAnimator alpha = ObjectAnimator.ofFloat(
                (isRight) ? rightTick : wrongTick,
                "alpha",
                0.5f, 1f, 1f, 1f, 1f, 1f, 0.5f
        );
        scaleX.setDuration(400);
        scaleY.setDuration(400);
        alpha.setDuration(400);

        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(scaleX, scaleY, alpha);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (isRight)
                    rightTick.setVisibility(View.VISIBLE);
                else {
                    wrongTick.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isRight)
                    rightTick.setVisibility(View.INVISIBLE);
                else {
                    wrongTick.setVisibility(View.INVISIBLE);
                }
            }
        });
        animation.start();
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
                    startGame();
                }
            }
        });
        animation.start();
    }

    private void updateQuestionValue() {
        if (currentCardShape == CardShape.HEART) {
            questionValue.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        else if (currentCardShape == CardShape.STAR) {
            questionValue.setImageResource(R.drawable.ic_star_black_24dp);
        }
        else if (currentCardShape == CardShape.CIRCLE) {
            questionValue.setImageResource(R.drawable.ic_lens_black_24dp);
        }

        if (currentCardColor == CardColor.RED) {
            questionValue.setColorFilter(getResources().getColor(R.color.colorSpeedMatchRed), PorterDuff.Mode.SRC_ATOP);
        }
        else if (currentCardColor == CardColor.GREEN) {
            questionValue.setColorFilter(getResources().getColor(R.color.colorSpeedMatchGreen), PorterDuff.Mode.SRC_ATOP);
        }
        else if (currentCardColor == CardColor.BLUE) {
            questionValue.setColorFilter(getResources().getColor(R.color.colorSpeedMatchBlue), PorterDuff.Mode.SRC_ATOP);
        }
        else if (currentCardColor == CardColor.YELLOW) {
            questionValue.setColorFilter(getResources().getColor(R.color.colorSpeedMatchYellow), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void findViews(View view) {
        bothButton = (Button) view.findViewById(R.id.speed_match_button_both);
        oneButton = (Button) view.findViewById(R.id.speed_match_button_one);
        noneButton = (Button) view.findViewById(R.id.speed_match_button_none);
        userScoreBoard = (TextView) view.findViewById(R.id.speed_match_user_score);
        gameLevelBoard = (TextView) view.findViewById(R.id.speed_match_game_level);
        gameContainer = (ConstraintLayout) view.findViewById(R.id.speed_match_game_container);
        gameCountDown = (TextView) view.findViewById(R.id.speed_match_game_count_down);
        rightTick = (ImageView) view.findViewById(R.id.speed_match_right_tick);
        wrongTick = (ImageView) view.findViewById(R.id.speed_match_wrong_tick);
        questionCard = (CardView) view.findViewById(R.id.speed_match_question_card);
        questionValue = (ImageView) view.findViewById(R.id.speed_match_question_value);
    }

    private void generateQuestion() {
        previousCardColor = currentCardColor;
        previousCardShape = currentCardShape;
        currentCardColor = generateCardColor();
        currentCardShape = generateCardShape();
    }

    private CardShape generateCardShape() {
        Random randomInt = new Random();
        int i = randomInt.nextInt();
        if (i < 0) i *= -1;
        return CardShape.values()[i % CARD_SHAPE_TOTAL];
    }

    private CardColor generateCardColor() {
        Random randomInt = new Random();
        int i = randomInt.nextInt();
        if (i < 0) i *= -1;
        return CardColor.values()[i % CARD_COLOR_TOTAL];
    }
}
