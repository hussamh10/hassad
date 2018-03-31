package com.hush.hassad.controller.competition.results;

import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.Team;

public class MatchResult extends Result{
    private int home_score;
    private int away_score;
    private Team winner;
    private Match match;

    public MatchResult(int home_score, int away_score, Team winner, Match match) {
        this.home_score = home_score;
        this.away_score = away_score;
        this.winner = winner;
        this.match = match;
    }

    // Auto generated code

    public int getHome_score() {
        return home_score;
    }

    public void setHome_score(int home_score) {
        this.home_score = home_score;
    }

    public int getAway_score() {
        return away_score;
    }

    public void setAway_score(int away_score) {
        this.away_score = away_score;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    @Override
    public String toString() {
        return "MatchResult{" +
                "home_score=" + home_score +
                ", away_score=" + away_score +
                ", winner=" + winner +
                ", match=" + match +
                '}';
    }
}
