package com.hush.hassad.controller.competition.results;

import com.hush.hassad.controller.competition.Team;

public class TournamentResult extends Result{
    private Team gold;
    private Team silver;
    private Team bronze;

    public TournamentResult(Team gold, Team silver, Team bronze) {
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
    }


    // Auto Generated Code

    public Team getGold() {
        return this.gold;
    }

    public void setGold(Team gold) {
        this.gold = gold;
    }

    public Team getSilver() {
        return this.silver;
    }

    public void setSilver(Team silver) {
        this.silver = silver;
    }

    public Team getBronze() {
        return this.bronze;
    }

    public void setBronze(Team bronze) {
        this.bronze = bronze;
    }

    @Override
    public String toString() {
        return "TournamentResult{" +
                ", gold=" + gold +
                ", silver=" + silver +
                ", bronze=" + bronze +
                '}';
    }
}
