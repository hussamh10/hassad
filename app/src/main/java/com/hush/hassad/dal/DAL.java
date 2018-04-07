package com.hush.hassad.dal;

import com.hush.hassad.controller.competition.Group;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.Team;
import com.hush.hassad.controller.competition.results.GroupResult;
import com.hush.hassad.controller.competition.results.MatchResult;
import com.hush.hassad.controller.player.Info;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.GroupPrediction;
import com.hush.hassad.controller.predictions.MatchPrediction;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DAL {

    private static DAL instance = null;

    private ArrayList<Match> matches;
	private ArrayList<Team> teams;
	private ArrayList<Group> groups;
	private ArrayList<MatchPrediction> mp;

    private DAL(){
		Team brasil = new Team(111, "Brasil", "");
		Team italy = new Team(112, "Italy", "");
		Team eng = new Team(113, "England", "");
		Team fra = new Team(114, "France", "");
		Team arg = new Team(115, "Argentina", "");
		Team por = new Team(116, "Portugal", "");
		Team bel = new Team(116, "Belgium", "");
		Team pak = new Team(116, "Pakistan", "");
		Team ind = new Team(116, "India", "");

		teams = new ArrayList<>();
		matches = new ArrayList<>();

		Match bravita = new Match(12, brasil, italy, "old trafford", new Date(), null, false, 2);
		Match bravita2 = new Match(13, brasil, italy, "old trafford", new Date(), null, false, 2);
		Match bravita3 = new Match(14, brasil, italy, "old trafford", new Date(), null, false, 2);

		teams.add(brasil);
		teams.add(italy);
		teams.add(eng);
		teams.add(fra);
		teams.add(arg);
		teams.add(por);
		teams.add(bel);
		teams.add(pak);
		teams.add(ind);

		matches.add(bravita);
		matches.add(bravita2);
		matches.add(bravita3);

		ArrayList<Team> grp1 = new ArrayList<>();
		grp1.add(brasil);
		grp1.add(italy);
		grp1.add(eng);
		grp1.add(fra);
ArrayList<Team> grp2 = new ArrayList<>(); grp2.add(arg);
		grp2.add(por);
		grp2.add(bel);
		grp2.add(pak);

		Group g1 = new Group(0, "Group A", grp1, null, null, false, null);
		Group g2 = new Group(0, "Group B", grp2, null, null, false, null);

		groups = new ArrayList<>();

		groups.add(g1);
		groups.add(g2);


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

    public ArrayList<GroupPrediction> getPredictedGroups(User user){
		ArrayList<GroupPrediction> gp = new ArrayList<>();


		GroupResult gr1 = new GroupResult(teams.get(0), teams.get(1), 0);
		GroupResult gr2 = new GroupResult(teams.get(5), teams.get(6), 1);

		GroupPrediction gp1 = new GroupPrediction(UUID.randomUUID(), gr1, user);
		GroupPrediction gp2 = new GroupPrediction(UUID.randomUUID(), gr2, user);

		gp.add(gp1);
		gp.add(gp2);

		return gp;
	}

	public void addPrediction(MatchPrediction m){
    	mp.add(m);
	}

	public ArrayList<MatchPrediction> getPredictedMatches(User user){
		mp = new ArrayList<>();
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

	public Group getGroup(int id) {
		if (id < groups.size()){
			return groups.get(id);
		}
		return groups.get(0);
	}

	public ArrayList<User> getFriends(User player) {
		ArrayList<User> friends = new ArrayList<>();

		UUID id = UUID.randomUUID();
		User fn1 = new User(id, 1000, 100, new Info(id, "Haroon Ahmed", "haroon@saad.usman",
				new Date(), "Washroom", 12));

		id = UUID.randomUUID();
		User fn2 = new User(id, 1000, 100, new Info(id, "Usman Ahmed", "haripur@saad.usman",
				new Date(), "Bathroom", 12));

		friends.add(fn1);
		friends.add(fn2);

		return friends;
	}
}
