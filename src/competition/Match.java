package competition;

import competition.results.Result;

import java.util.Date;

public class Match {
    private int id;

    private Team home;
    private Team away;

    private String venue;
    private Date kickoff_time;

    private Result result;
    private boolean ended;

    private int stage; // higher points for winning higher stage bets

    public Match(int id, boolean ended, Team home, Team away, String venue, Date kickoff_time, Result result, int stage) {
        this.id = id;
        this.ended = ended;
        this.home = home;
        this.away = away;
        this.venue = venue;
        this.kickoff_time = kickoff_time;
        this.result = result;
        this.stage = stage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public Team getHome() {
        return home;
    }

    public void setHome(Team home) {
        this.home = home;
    }

    public Team getAway() {
        return away;
    }

    public void setAway(Team away) {
        this.away = away;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Date getKickoff_time() {
        return kickoff_time;
    }

    public void setKickoff_time(Date kickoff_time) {
        this.kickoff_time = kickoff_time;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", ended=" + ended +
                ", home=" + home +
                ", away=" + away +
                ", venue='" + venue + '\'' +
                ", kickoff_time=" + kickoff_time +
                ", result=" + result +
                ", stage=" + stage +
                '}';
    }
}