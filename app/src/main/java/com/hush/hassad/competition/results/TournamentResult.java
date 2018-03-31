package com.hush.hassad.competition.results;

import com.hush.hassad.player.Player;
import com.hush.hassad.competition.Team;

public class TournamentResult extends Result{
    private Player golden_ball;
    private Player golden_boot;
    private Player golden_glove;

    private Team gold;
    private Team silver;
    private Team bronze;
    private Team wood;

    public TournamentResult(Player golden_ball, Player golden_boot, Player golden_glove, Team gold, Team silver, Team bronze, Team wood) {
        this.golden_ball = golden_ball;
        this.golden_boot = golden_boot;
        this.golden_glove = golden_glove;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.wood = wood;
    }


    // Auto Generated Code

    public Player getGolden_ball() {
        return golden_ball;
    }

    public void setGolden_ball(Player golden_ball) {
        this.golden_ball = golden_ball;
    }

    public Player getGolden_boot() {
        return golden_boot;
    }

    public void setGolden_boot(Player golden_boot) {
        this.golden_boot = golden_boot;
    }

    public Player getGolden_glove() {
        return golden_glove;
    }

    public void setGolden_glove(Player golden_glove) {
        this.golden_glove = golden_glove;
    }

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

    public Team getWood(){
        return this.wood;
    }

    public void setWood(Team wood){
        this.wood = wood;
    }

    @Override
    public String toString() {
        return "TournamentResult{" +
                "golden_ball=" + golden_ball +
                ", golden_boot=" + golden_boot +
                ", golden_glove=" + golden_glove +
                ", gold=" + gold +
                ", silver=" + silver +
                ", bronze=" + bronze +
                '}';
    }
}
