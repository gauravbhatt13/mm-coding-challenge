package coding.challenge.domain;

public class Stats {
    private String inputTime;
    private String team;
    private String possessionTime;
    private int shots;
    private int scores;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPossessionTime() {
        return possessionTime;
    }

    public void setPossessionTime(String possessionTime) {
        this.possessionTime = possessionTime;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }
}
