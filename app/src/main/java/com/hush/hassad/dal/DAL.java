package com.hush.hassad.dal;

import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.Team;

import java.util.ArrayList;
import java.util.Date;

public class DAL {

    private static DAL instance = null;

    private DAL(){}

    public static DAL getInstance(){
        if(instance == null)
            instance = new DAL();
        return instance;
    }

    public ArrayList<Match> getMatches(Date date)throws Exception{


        Match m = new Match(1,
                new Team(1, "Brasil", ""),
                new Team(2, "Italy", ""),
                "Old Trafford",
                new Date(),
                null,
                false,
                1);

        ArrayList<Match> matches = new ArrayList<>();

        matches.add(m);

        m = new Match(1,
                new Team(1, "Brasil", ""),
                new Team(2, "Italy", ""),
                "Old Trafford",
                new Date(),
                null,
                false,
                1);

        if (date.getDate() == 31){
            matches.add(m);
        }

        return matches;
    }
}
