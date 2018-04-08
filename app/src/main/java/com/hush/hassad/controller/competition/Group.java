package com.hush.hassad.controller.competition;

import android.support.annotation.NonNull;

import com.hush.hassad.controller.competition.results.GroupResult;

import java.util.ArrayList;
import java.util.Collections;

public class Group {

    int id;
    String name;

    public class Row implements Comparable{
        public Team team;
        public int pts;
        public int pld;

        Row(Team team, int pts, int pld){
            this.team = team;
            this.pts = pts;
            this.pld = pld;
        }

        @Override
        public int compareTo(@NonNull Object obj) {
            Row o = (Row) obj;

        	if (pts == o.pts){
        	    return 0;
            }
            if (pts < o.pts){
        	    return -1;
            }
            if (pts > o.pts){
                return 1;
            }
            return 0;
        }
    }

    ArrayList<Row> rows;

    ArrayList<Team> teams;

    boolean ended;
    GroupResult result;

    public Group(int id, String name, ArrayList<Team> teams, ArrayList<Integer> pts, ArrayList<Integer> pld, boolean ended, GroupResult result) {
        this.id = id;

        rows = new ArrayList<>();

        for(int i = 0; i < teams.size(); i++){
            rows.add(new Row(teams.get(i), pts.get(i), pld.get(i)));
        }

        this.teams = teams;

        this.ended = ended;
        this.result = result;
        this.name = name;
    }

    public int getId() {
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

    public void setPoints(Team t, int pts){
    	for(Row row : rows){
    	    if (t.getId() == row.team.getId()){
    	        row.pts = pts;
            }
        }
    }

    public void setPlayed(Team t, int pld){
        for(Row row : rows){
            if (t.getId() == row.team.getId()){
                row.pld = pld;
            }
        }
    }

    public ArrayList<Row> getRows(){
        return rows;
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
                ", ended=" + ended +
                ", result=" + result +
                '}';
    }

    public ArrayList<Team> getQualifying() {
    	// TODO TEST

        Collections.sort(rows);
        ArrayList<Team> q = new ArrayList<>();

        q.add(rows.get(0).team);
        q.add(rows.get(1).team);

        return q;
    }
}
