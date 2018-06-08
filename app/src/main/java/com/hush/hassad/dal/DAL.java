package com.hush.hassad.dal;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hush.hassad.controller.Constants;
import com.hush.hassad.controller.Manager;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.Team;
import com.hush.hassad.controller.competition.results.MatchResult;
import com.hush.hassad.controller.competition.results.TournamentResult;
import com.hush.hassad.controller.player.Info;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.MatchPrediction;
import com.hush.hassad.controller.predictions.TournamentPrediction;
import com.hush.hassad.ui.fragments.LeaderboardFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class DAL {

    private static DAL instance = null;

    private ArrayList<Match> matches;
	private ArrayList<Team> teams;
	private ArrayList<MatchPrediction> mp;

	private Object temp;

	FirebaseFirestore db = FirebaseFirestore.getInstance();
	CollectionReference users_doc = db.collection("users");
	CollectionReference matches_doc = db.collection("matches");
	CollectionReference match_results_doc = db.collection("match_results");
	CollectionReference team_doc = db.collection("teams");

    private DAL(){
    	init();
	}

	public User createUser(FirebaseUser user){
    	String id = user.getUid();
		String name = user.getDisplayName();
		String email = user.getEmail();
		String photoUrl = String.valueOf(user.getPhotoUrl());
		User u = new User(id, Constants.INITIAL_POINTS, 0, new Info(id, name, email, null, null, 0, null));

		Map<String, Object> doc = new HashMap<>();

		doc.put("id", id);
		doc.put("points", Constants.INITIAL_COINS);
		doc.put("name", name);
		doc.put("email", email);
		doc.put("photoUrl", photoUrl);
		users_doc.add(doc);

		return u;
	}

	public Match getMatchFromDB(int id){
    	final Match match = new Match(0, null, null, null, null, null, false, 0);
    	Query q = matches_doc.whereEqualTo("id", id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				String id = u.getString("id");
				Boolean ended = u.getBoolean("ended");
				String kickoff_time = u.getString("kickoff_time");
				String venue = u.getString("venue");
				String home_team_id = u.getString("home_team_id");
				String away_team_id = u.getString("away_team_id");
				int stage = u.getLong("stage").intValue();

				SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
				Date kickoff_date = null;
				try {
					kickoff_date = dateFormat.parse(kickoff_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Team home = getTeamFromDB(home_team_id);
				Team away = getTeamFromDB(away_team_id);

				MatchResult result = getMatchResult(id);
				match.setId(id);
				match.setEnded(ended);
				match.setAway(away);
				match.setHome(home);
				match.setKickoff_time(kickoff_date);
				match.setStage(stage);
				match.setVenue(venue);
				match.setResult(result);
			}
		});

		return match;
	}

	public MatchResult getMatchResult(final String match_id){
		final MatchResult matchResult = new MatchResult(0, 0, null, 0);
    	Query q = match_results_doc.whereEqualTo("match_id", match_id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				int home_score = u.getLong("home_score").intValue();
				int away_score = u.getLong("away_score").intValue();
				Team winner  = getTeam(id);
				matchResult.setHome_score(home_score);
				matchResult.setAway_score(away_score);
				matchResult.setWinner(winner);
				matchResult.setMatch(match_id);
			}
		});
		return matchResult;
	}

	public Team getTeamFromDB(final String id){
    	final Team team = new Team(0, null, null);
    	Query q = team_doc.whereEqualTo("id", id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				String name = u.getString("name");
				String image_url = u.getString("image_url");
				team.setId(id);
				team.setName(name);
				team.setImage_url(image_url);
			}
		});
		return team;
	}

	public void getUser(String id) {
		Query q = users_doc.whereEqualTo("id", id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
					String id = u.getString("id");
					String name = u.getString("name");
					String email = u.getString("email");
					String photoUrl = u.getString("photoUrl");
					int points = u.getLong("points").intValue();
					User user = new User(id, points, 0, new Info(id, name, email, null, null, 0, photoUrl));
					Manager.getInstance().setPlayingUser(user);
			}
		});
	}

	public void updateLeaderboard(final LeaderboardFragment leaderboardFragment) {
		Query q = users_doc.whereGreaterThan("points", -1);
		Log.i("DAL", "Started Leaderboard");
		Task<QuerySnapshot> task = q.get();
		task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
				ArrayList<User> users = new ArrayList<>();

				for (DocumentSnapshot u : docs){
					String id = u.getString("id");
					String name = u.getString("name");
					String email = u.getString("email");
					String photoUrl = u.getString("photoUrl");
					int points = u.getLong("points").intValue();
					User user = new User(id, points, 0, new Info(id, name, email, null, null, 0,photoUrl));

					users.add(user);
				}
				Collections.sort(users, new Comparator<User>() {
					@Override
					public int compare(User user, User t1) {
						if (user.getPoints() > t1.getPoints()){
							return 1;
						}
						return 0;
					}
				});
				Log.i("DAL", "Loaded Leaderboard");
				leaderboardFragment.setUsers(users);
			}
		});
		task.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.i("DAL", "Could not load leaderboard");
			}
		});
	}

	private void init(){
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

		ArrayList<Team> grp2 = new ArrayList<>();
		grp2.add(arg);
		grp2.add(por);
		grp2.add(bel);
		grp2.add(pak);


		ArrayList<Integer> p = new ArrayList<>(); p.add(0);p.add(0);p.add(0);p.add(0);
	}

    public static DAL getInstance(){
        if(instance == null)
            instance = new DAL();
        return instance;
    }

    public ArrayList<Match> getMatches(Date date)throws Exception{

		User p = Manager.getInstance().getPlayingUser();

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

		matches.add(m);
        return matches;
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

	public ArrayList<User> getFriends(User player) {
		ArrayList<User> friends = new ArrayList<>();

		String photoUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Flibrary.stanford.edu%2Fsites%2Fdefault%2Ffiles%2Fperson%2Fimage%2Fimg_0017.jpg&imgrefurl=https%3A%2F%2Flibrary.stanford.edu%2Fpeople%2Faalsum&docid=2ZcGmzIUiq9mtM&tbnid=GB9HnX-iGA45LM%3A&vet=1&w=4213&h=3602&safe=off&client=safari&bih=907&biw=1680&ved=0ahUKEwjxnerb0sHbAhWhK5oKHbiWCioQMwgzKAAwAA&iact=c&ictx=1";
		String id = UUID.randomUUID().toString();
		User fn1 = new User(id, 1000, 100, new Info(id, "Haroon Ahmed", "haroon@saad.usman",
				new Date(), "Washroom", 12, photoUrl));

		id = UUID.randomUUID().toString();
		User fn2 = new User(id, 1000, 100, new Info(id, "Usman Ahmed", "haripur@saad.usman",
				new Date(), "Bathroom", 12, photoUrl));

		friends.add(fn1);
		friends.add(fn2);

		return friends;
	}

	public TournamentPrediction getTournamentPrediction(User user) {
		TournamentResult result = new TournamentResult(teams.get(1), teams.get(3), teams.get(4));
		TournamentPrediction tp = new TournamentPrediction(UUID.randomUUID(), result, user);
		return tp;
	}


	// Real functions
}
