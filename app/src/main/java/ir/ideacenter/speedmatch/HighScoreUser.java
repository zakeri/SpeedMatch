package ir.ideacenter.speedmatch;

public class HighScoreUser {
    private String userName;
    private int highScore;

    public HighScoreUser() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public String toString() {
        return "HighScoreUser{" +
                "userName='" + userName + '\'' +
                ", highScore=" + highScore +
                '}';
    }
}
