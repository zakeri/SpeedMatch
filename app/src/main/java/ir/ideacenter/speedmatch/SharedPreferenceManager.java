package ir.ideacenter.speedmatch;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferenceManager {

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;
    private static SharedPreferenceManager instance = null;

    public  static SharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context);
        }
        return instance;
    }

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public HighScoreList getWhichOneHighScoreList() {
        String hsl = sharedPreferences.getString("which_one_high_score_list", null);
        if (hsl == null) {
            return new HighScoreList();
        }
        Gson gson = new Gson();
        return gson.fromJson(hsl, HighScoreList.class);
    }

    public void putWhichOneHighScoreList(HighScoreList list) {
        Gson gson = new Gson();
        String hsl = gson.toJson(list, HighScoreList.class);
        editor.putString("which_one_high_score_list", hsl);
        editor.apply();
    }

    public HighScoreList getSpeedMatchHighScoreList() {
        String hsl = sharedPreferences.getString("speed_match_high_score_list", null);
        if (hsl == null) {
            return new HighScoreList();
        }
        Gson gson = new Gson();
        return gson.fromJson(hsl, HighScoreList.class);
    }

    public void putSpeedMatchHighScoreList(HighScoreList list) {
        Gson gson = new Gson();
        String hsl = gson.toJson(list, HighScoreList.class);
        editor.putString("speed_match_high_score_list", hsl);
        editor.apply();
    }
}
