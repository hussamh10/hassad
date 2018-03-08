package competition;

import competition.results.Result;

import java.util.ArrayList;

public class Group {
    int id;

    ArrayList<Team> teams; // sorted with points
    ArrayList<Integer> pts; // sorted with points
    ArrayList<Integer> pld; // sorted with points

    boolean ended;
    Result result;

    public Group(int id, ArrayList<Team> teams, ArrayList<Integer> pts, ArrayList<Integer> pld, boolean ended, Result result) {
        this.id = id;
        this.teams = teams;
        this.pts = pts;
        this.pld = pld;
        this.ended = ended;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public ArrayList<Integer> getPts() {
        return pts;
    }

    public void setPts(ArrayList<Integer> pts) {
        this.pts = pts;
    }

    public ArrayList<Integer> getPld() {
        return pld;
    }

    public void setPld(ArrayList<Integer> pld) {
        this.pld = pld;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", teams=" + teams +
                ", pts=" + pts +
                ", pld=" + pld +
                ", ended=" + ended +
                ", result=" + result +
                '}';
    }
}
