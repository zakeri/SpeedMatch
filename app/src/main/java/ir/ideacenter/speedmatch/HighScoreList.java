package ir.ideacenter.speedmatch;

import java.util.ArrayList;
import java.util.List;

public class HighScoreList {
    List<HighScoreUser> highScoreUserList;
    public HighScoreList() {
        highScoreUserList = new ArrayList<HighScoreUser>();
    }

    public List<HighScoreUser> getHighScoreUserList() {
        return highScoreUserList;
    }

    public void setHighScoreUserList(List<HighScoreUser> highScoreUserList) {
        this.highScoreUserList = highScoreUserList;
    }

    public void addHighScoreUser(HighScoreUser hsu) {
        highScoreUserList.add(hsu);
    }
}
