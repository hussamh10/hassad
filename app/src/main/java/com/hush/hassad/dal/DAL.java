package com.hush.hassad.dal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hush.hassad.R;
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
import com.hush.hassad.receiver.AlarmReceiver;
import com.hush.hassad.ui.activities.ScheduleActivity;
import com.hush.hassad.ui.fragments.LeaderboardFragment;
import com.hush.hassad.ui.fragments.home.DayFragment;
import com.hush.hassad.ui.fragments.home.HomeFragment;
import com.hush.hassad.util.GetByteArrayFromURL;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import static android.content.ContentValues.TAG;

public class DAL {

    private static DAL instance = null;

    private ArrayList<Match> matches;
	private ArrayList<Team> teams;
	private ArrayList<MatchPrediction> mp;

	private Object temp;

	FirebaseFirestore db = FirebaseFirestore.getInstance();
	CollectionReference users_doc = db.collection("users");
	CollectionReference predictions_doc = db.collection("predictions_test");
	CollectionReference matches_doc = db.collection("matches");
	CollectionReference match_results_doc = db.collection("match_results");
	CollectionReference team_doc = db.collection("teams");

    private DAL(){
		//TODO Prediction
    	//init();
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

	public Team getTeamAsync(final int id, Team team){
		//TODO change the following vals to a default value
		final Team temp = new Team(0, null, null);
		team = temp;
		Query q = team_doc.whereEqualTo("id", id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				String name = u.getString("name");
				String image_url = u.getString("image_url");
				temp.setId(id);
				temp.setName(name);
				//temp.setImage_url(image_url);
			}
		});
		return temp;
	}

	public Team getTeamAsync(final int id, final Callback callback){
		//TODO change the following vals to a default value
		final Team temp = new Team(0, null, null);
		Query q = team_doc.whereEqualTo("id", id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				String name = u.getString("name");
				String image_url = u.getString("image_url");
				temp.setId(id);
				temp.setName(name);
				//temp.setImage_url(image_url);
				callback.callback(temp);
			}
		});
		return temp;
	}


	public Match getMatchAsync(int id, Match return_match){
		// TODO change to a default value
		final Match match = new Match(0, null, null, null, null, null, false, 0);
		return_match = match;
		Query q = matches_doc.whereEqualTo("id", id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				int id = u.getLong("id").intValue();
				Boolean ended = u.getBoolean("ended");
				com.google.firebase.Timestamp temp = u.getTimestamp("kickoff_time");
				String k = temp.toString();
				//TODO fix this date stuff
				//String kickoff_time = u.getString("kickoff_time");
				String venue = u.getString("venue");
				int home_team_id = u.getLong("home_team_id").intValue();
				int away_team_id = u.getLong("away_team_id").intValue();
				int stage = u.getLong("stage").intValue();
				int result_id = u.getLong("match_result_id").intValue();

				/*
				SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
				Date kickoff_date = null;
				try {
					kickoff_date = dateFormat.parse(k);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				*/

				Team home = null;
				Team away = null;
				home = getTeamAsync(home_team_id, home);
				away = getTeamAsync(away_team_id, away);

				MatchResult result = null;
				result = getMatchResultAsync(result_id, result);
				match.setId(id);
				match.setEnded(ended);
				match.setAway(away);
				match.setHome(home);
				match.setKickoff_time(new Date());
				match.setStage(stage);
				match.setVenue(venue);
				match.setResult(result);
			}
		});

		return return_match;
	}

	public Match getMatchAsync(int id, final Callback callback){
		// TODO change to a default value
		final Match match = new Match(0, null, null, null, null, null, false, 0);
		Query q = matches_doc.whereEqualTo("id", id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				int id = u.getLong("id").intValue();
				Boolean ended = u.getBoolean("ended");
				com.google.firebase.Timestamp temp = u.getTimestamp("kickoff_time");
				String k = temp.toString();
				//TODO fix this date stuff
				//String kickoff_time = u.getString("kickoff_time");
				String venue = u.getString("venue");
				int home_team_id = u.getLong("home_team_id").intValue();
				int away_team_id = u.getLong("away_team_id").intValue();
				int stage = u.getLong("stage").intValue();
				int result_id = u.getLong("match_result_id").intValue();

				/*
				SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
				Date kickoff_date = null;
				try {
					kickoff_date = dateFormat.parse(k);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				*/

				Team home = null;
				Team away = null;
				home = getTeamAsync(home_team_id, home);
				away = getTeamAsync(away_team_id, away);

				MatchResult result = null;
				result = getMatchResultAsync(result_id, result);
				match.setId(id);
				match.setEnded(ended);
				match.setAway(away);
				match.setHome(home);
				match.setKickoff_time(new Date());
				match.setStage(stage);
				match.setVenue(venue);
				match.setResult(result);
				callback.callback(match);
			}
		});

		return match;
	}

	public MatchResult getMatchResultAsync(final int match_id, final Callback callback){
		final MatchResult matchResult = new MatchResult(0, 0, null, 0);
		Query q = match_results_doc.whereEqualTo("match_id", match_id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				int id = u.getLong("id").intValue();
				int home_score = u.getLong("home_score").intValue();
				int away_score = u.getLong("away_score").intValue();
				int match_id = u.getLong("match_id").intValue();
				Team winner = null;
				winner = getTeamAsync(id, winner);
				matchResult.setId(id);
				matchResult.setHome_score(home_score);
				matchResult.setAway_score(away_score);
				matchResult.setWinner(winner);
				matchResult.setMatch(match_id);
				callback.callback(matchResult);
			}
		});
		return matchResult;
	}

	public MatchResult getMatchResultAsync(final int match_id, MatchResult mr){
		final MatchResult matchResult = new MatchResult(0, 0, null, 0);
		Query q = match_results_doc.whereEqualTo("match_id", match_id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				int id = u.getLong("id").intValue();
				int home_score = u.getLong("home_score").intValue();
				int away_score = u.getLong("away_score").intValue();
				int match_id = u.getLong("match_id").intValue();
				Team winner = null;
				winner = getTeamAsync(id, winner);
				matchResult.setId(id);
				matchResult.setHome_score(home_score);
				matchResult.setAway_score(away_score);
				matchResult.setWinner(winner);
				matchResult.setMatch(match_id);
			}
		});
		return matchResult;
	}

	public void submitMatchResult(MatchResult match_result){
    	int id = match_result.getId();
		int match = match_result.getMatch();
		int home_score = match_result.getHome_score();
		int away_score = match_result.getAway_score();

		int winner = match_result.getWinner().getId();

		Map<String, Object> doc = new HashMap<>();

		doc.put("id", id);
		doc.put("match_id", match);
		doc.put("home_score", home_score);
		doc.put("away_score", away_score);
		doc.put("winner", winner);
		match_results_doc.add(doc);
	}

	public void submitPrediction(MatchPrediction prediction) {
		MatchResult mr = prediction.getPredicted_result();
		submitMatchResult(mr);

		String user_id = Manager.getInstance().getPlayingUser().getId();
		int match_id = prediction.getPredicted_result().getMatch();
		int mr_id = mr.getId();

		Map<String, Object> doc = new HashMap<>();

		doc.put("user_id", user_id);
		doc.put("match_id", match_id);
		doc.put("match_result_id", mr_id);

		Log.i("DAL", "Added predictoins");
		predictions_doc.add(doc);
	}

	public void setPlayingUser(String id) {
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
	//TODO Prediction
/*

	private void init(){
		Team brasil = new Team(111, "Brasil", "");
		Team italy = new Team(112, "Italy", "");
		Team eng = new Team(113, "England", "");
		Team fra = new Team(114, "France", "");
		Team arg = new Team(115, "Argentina", "");
		Team por = new Team(116, "Portugal", "");
		Team bel = new Team(117, "Belgium", "");
		Team pak = new Team(118, "Pakistan", "");
		Team ind = new Team(119, "India", "");

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

*/
    public static DAL getInstance(){
        if(instance == null)
            instance = new DAL();
        return instance;
    }
	//TODO Prediction
/*

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

*/
    private com.google.firebase.Timestamp getNextDay(Date date) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.DAY_OF_MONTH, 1);
    	return new com.google.firebase.Timestamp(cal.getTime());
	}

    public void updateMatches(final DayFragment dayFragment, Date date) throws Exception {
		com.google.firebase.Timestamp start = new com.google.firebase.Timestamp(date);
		com.google.firebase.Timestamp end = getNextDay(date);
		Log.i("DAL", "Started Matches");

		final Executor executor = new Executor() {
			@Override
			public void execute(@NonNull Runnable command) {
				command.run();
			}
		};
		
		Query query = matches_doc.whereGreaterThanOrEqualTo("kickoff_time",start).whereLessThanOrEqualTo("kickoff_time",end);
		Task<QuerySnapshot> matchTask = query.get();

		matchTask.addOnSuccessListener(executor, new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				List<DocumentSnapshot> matchDocs = queryDocumentSnapshots.getDocuments();

				final ArrayList<Match> matches = new ArrayList<>();

				for (DocumentSnapshot m : matchDocs) {
					final int match_id = m.getLong("id").intValue();
					final int away_team_id = Integer.parseInt(m.get("away_team_id").toString());
					final int home_team_id = Integer.parseInt(m.get("home_team_id").toString());
					final boolean ended = Boolean.parseBoolean(m.get("ended").toString());
					final Date kickoffTime = m.getDate("kickoff_time");
					final int result_id = Integer.parseInt(m.get("match_result_id").toString());
					final int stage = Integer.parseInt(m.get("stage").toString());
					final String venue = m.getString("venue");

					try {
						final Match match = new Match();
						match.setId(match_id);
						match.setHome(new Team());
						match.setAway(new Team());
						match.setResult(new MatchResult());
						match.setStage(stage);
						match.setKickoff_time(kickoffTime);
						match.setEnded(ended);
						match.setVenue(venue);

						Task<QuerySnapshot> initialTask = team_doc.whereEqualTo("id", home_team_id).get();

						initialTask.continueWithTask(executor, new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
							@Override
							public Task<QuerySnapshot> then(@NonNull Task<QuerySnapshot> task) {
								DocumentSnapshot homeDoc = task.getResult().getDocuments().get(0);

								Team homeTeam = match.getHome();
								homeTeam.setId(home_team_id);
								homeTeam.setName(homeDoc.getString("name"));
								//homeTeam.setImage_url(homeDoc.getString("image_url"));
								byte [] bytes = GetByteArrayFromURL.getByteArrayFromURL(homeDoc.getString("image_url"));
								homeTeam.setImage_url(bytes);
								return team_doc.whereEqualTo("id", away_team_id).get();
							}
						}).continueWithTask(executor, new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
							@Override
							public Task<QuerySnapshot> then(@NonNull Task<QuerySnapshot> task) {
								DocumentSnapshot awayDoc = task.getResult().getDocuments().get(0);

								Team awayTeam = match.getAway();
								awayTeam.setId(away_team_id);
								awayTeam.setName(awayDoc.getString("name"));
								//awayTeam.setImage_url(awayDoc.getString("image_url"));
								byte [] bytes = GetByteArrayFromURL.getByteArrayFromURL(awayDoc.getString("image_url"));
								awayTeam.setImage_url(bytes);

								return match_results_doc.whereEqualTo("match_id", match_id).get();
							}
						}).continueWith(executor, new Continuation<QuerySnapshot, Match>() {
							@Override
							public Match then(@NonNull Task<QuerySnapshot> task) {
								DocumentSnapshot ds = task.getResult().getDocuments().get(0);
								MatchResult matchResult = match.getResult();
								matchResult.setId(result_id);
								matchResult.setHome_score(ds.getLong("home_score").intValue());
								matchResult.setAway_score(ds.getLong("away_score").intValue());

								int winnerId = ds.getLong("winner").intValue();

								if (winnerId == home_team_id) {
									match.getResult().setWinner(match.getHome());
								} else {
									match.getResult().setWinner(match.getAway());
								}

								return match;
							}
						}).addOnSuccessListener(new OnSuccessListener<Match>() {
							@Override
							public void onSuccess(Match match) {
								dayFragment.addMatchSorted(match);
								Log.i("DAL", "onSuccess: sorted matches" + match.getHome().getName());
							}
						});

					} catch (Exception e) {
						Log.i("DAL", "failed to load matches");
					}
				}
			}
		});
	}

	public void updateSchedule(final ScheduleActivity scheduleActivity){
		Log.i("DAL", "Started Matches");
		Query q = matches_doc.whereEqualTo("ended",false);
		Task<QuerySnapshot> task = q.get();
		final Executor executor = new Executor() {
			@Override
			public void execute(@NonNull Runnable command) {
				command.run();
			}
		};

		task.addOnSuccessListener(executor, new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				List<DocumentSnapshot> matchDocs = queryDocumentSnapshots.getDocuments();

				final ArrayList<Match> matches = new ArrayList<>();

				for (DocumentSnapshot m : matchDocs) {
					final int match_id = m.getLong("id").intValue();
					final int away_team_id = Integer.parseInt(m.get("away_team_id").toString());
					final int home_team_id = Integer.parseInt(m.get("home_team_id").toString());
					final boolean ended = Boolean.parseBoolean(m.get("ended").toString());
					final Date kickoffTime = m.getDate("kickoff_time");
					final int result_id = Integer.parseInt(m.get("match_result_id").toString());
					final int stage = Integer.parseInt(m.get("stage").toString());
					final String venue = m.getString("venue");

					try {
						final Match match = new Match();
						match.setId(match_id);
						match.setHome(new Team());
						match.setAway(new Team());
						match.setResult(new MatchResult());
						match.setStage(stage);
						match.setKickoff_time(kickoffTime);
						match.setEnded(ended);
						match.setVenue(venue);

						Task<QuerySnapshot> initialTask = team_doc.whereEqualTo("id", home_team_id).get();

						initialTask.continueWithTask(executor, new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
							@Override
							public Task<QuerySnapshot> then(@NonNull Task<QuerySnapshot> task) {
								DocumentSnapshot homeDoc = task.getResult().getDocuments().get(0);

								Team homeTeam = match.getHome();
								homeTeam.setId(home_team_id);
								homeTeam.setName(homeDoc.getString("name"));
								//homeTeam.setImage_url(homeDoc.getString("image_url"));
								byte [] bytes = GetByteArrayFromURL.getByteArrayFromURL(homeDoc.getString("image_url"));
								homeTeam.setImage_url(bytes);
								return team_doc.whereEqualTo("id", away_team_id).get();
							}
						}).continueWithTask(executor, new Continuation<QuerySnapshot, Task<QuerySnapshot>>() {
							@Override
							public Task<QuerySnapshot> then(@NonNull Task<QuerySnapshot> task) {
								DocumentSnapshot awayDoc = task.getResult().getDocuments().get(0);

								Team awayTeam = match.getAway();
								awayTeam.setId(away_team_id);
								awayTeam.setName(awayDoc.getString("name"));
								//awayTeam.setImage_url(awayDoc.getString("image_url"));
								byte [] bytes = GetByteArrayFromURL.getByteArrayFromURL(awayDoc.getString("image_url"));
								awayTeam.setImage_url(bytes);

								return match_results_doc.whereEqualTo("match_id", match_id).get();
							}
						}).continueWith(executor, new Continuation<QuerySnapshot, Match>() {
							@Override
							public Match then(@NonNull Task<QuerySnapshot> task) {
								DocumentSnapshot ds = task.getResult().getDocuments().get(0);
								MatchResult matchResult = match.getResult();
								matchResult.setId(result_id);
								matchResult.setHome_score(ds.getLong("home_score").intValue());
								matchResult.setAway_score(ds.getLong("away_score").intValue());

								int winnerId = ds.getLong("winner").intValue();

								if (winnerId == home_team_id) {
									match.getResult().setWinner(match.getHome());
								} else {
									match.getResult().setWinner(match.getAway());
								}

								return match;
							}
						}).addOnSuccessListener(new OnSuccessListener<Match>() {
							@Override
							public void onSuccess(Match match) {
								scheduleActivity.addMatchSorted(match);
								Log.i("DAL", "onSuccess: sorted matches" + match.getHome().getName());
							}
						});

					} catch (Exception e) {
						Log.i("DAL", "failed to load matches");
					}
				}
			}
		});
	}

	public void isMatch(final AlarmReceiver alarmReceiver, Date date){
		com.google.firebase.Timestamp start = new com.google.firebase.Timestamp(date);
		com.google.firebase.Timestamp end = getNextDay(date);
		Log.i("DAL", "Getting Num of Matches");

		Query q = matches_doc.whereGreaterThanOrEqualTo("kickoff_time",start).whereLessThanOrEqualTo("kickoff_time",end);
		Task<QuerySnapshot> task = q.get();
		task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
				if(docs.isEmpty()) {
					int count = 0;
					alarmReceiver.setCount(count);
					alarmReceiver.sendNotification();
				}
				else{
					int count = 1;
					alarmReceiver.setCount(count);
					alarmReceiver.sendNotification();
				}
				Log.i("DAL", "Got Num of Matches");
			}
		});
		task.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.i("DAL", "Could not get num of matches");
			}
		});
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

	public interface Callback {
		void callback(Object o);
	}

	// Real functions
}
/*
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
*/
