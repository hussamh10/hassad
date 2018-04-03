package com.hush.hassad.dal;

import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.Team;
import com.hush.hassad.controller.competition.results.MatchResult;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.MatchPrediction;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DAL {

    private static DAL instance = null;

    private ArrayList<Match> matches;
	private ArrayList<Team> teams;

    private DAL(){
		Team brasil = new Team(111, "Brasil", "");
		Team italy = new Team(112, "Italy", "");

		teams = new ArrayList<>();
		matches = new ArrayList<>();

		Match bravita = new Match(12, brasil, italy, "old trafford", new Date(), null, false, 2);
		Match bravita2 = new Match(13, brasil, italy, "old trafford", new Date(), null, false, 2);
		Match bravita3 = new Match(14, brasil, italy, "old trafford", new Date(), null, false, 2);

		teams.add(brasil);
		teams.add(italy);
		matches.add(bravita);
		matches.add(bravita2);
		matches.add(bravita3);
	}

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
                date,
                null,
                false,
                1);

        ArrayList<Match> matches = new ArrayList<>();

        matches.add(m);

        m = new Match(1,
                new Team(1, "Brasil", ""),
                new Team(2, "Italy", ""),
                "Old Trafford",
                date,
                null,
                false,
                1);

        if (date.getDate() == 31){
            matches.add(m);
        }

        return matches;
    }

	public ArrayList<MatchPrediction> getPredictedMatches(User user){
    	// FIXME
		ArrayList<MatchPrediction> mp = new ArrayList<>();

		MatchResult result_ended = new MatchResult(10, 1, teams.get(0), 0);

		matches.get(0).setEnded(true);
		matches.get(0).setResult(result_ended);

		MatchResult presult_ended = new MatchResult(10, 23, teams.get(1), 0);
		MatchResult presult_to_play = new MatchResult(10, 22, teams.get(1), 1);

		MatchPrediction mp1 = new MatchPrediction(UUID.randomUUID(), presult_ended, user);
		MatchPrediction mp2 = new MatchPrediction(UUID.randomUUID(), presult_to_play, user);

		mp.add(mp1);
		mp.add(mp2);

		return mp;
	}


	public Team getTeam(int id) {
		if (id < teams.size()){
			return teams.get(id);
		}
		return teams.get(0);
	}

	public Match getMatch(int id) {
		if (id < matches.size()){
			return matches.get(id);
		}
		return matches.get(0);
	}
}
