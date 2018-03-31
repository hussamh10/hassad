package com.hush.hassad.controller.competition;

import com.hush.hassad.controller.competition.results.GroupResult;

import java.util.ArrayList;

public class Group {

    int id;
    String name;

    ArrayList<Team> teams; // sorted with points
    ArrayList<Integer> pts; // sorted with points
    ArrayList<Integer> pld; // sorted with points

    boolean ended;
    GroupResult result;

    public Group(int id, String name, ArrayList<Team> teams, ArrayList<Integer> pts, ArrayList<Integer> pld, boolean ended, GroupResult result) {
        this.id = id;
        this.teams = teams;
        this.pts = pts;
        this.pld = pld;
        this.ended = ended;
        this.result = result;
        this.name = name;
    }

    public int get() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
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

    public GroupResult getResult() {
        return result;
    }

    public void setResult(GroupResult result) {
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
