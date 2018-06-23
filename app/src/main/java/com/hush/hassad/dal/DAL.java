package com.hush.hassad.dal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telecom.Call;
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
import com.hush.hassad.controller.predictions.Prediction;
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
	CollectionReference tournament_predictions = db.collection("tournament_predictions");

    private DAL(){
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


	public void getTeamAsync(final int id, final Callback callback){
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
				callback.callback(temp);
			}
		});
	}

	public void getAllTeams(final Callback callback){
    	team_doc.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				ArrayList<Team> teams = new ArrayList<>();
				for (DocumentSnapshot u : queryDocumentSnapshots.getDocuments()) {
					Team temp = new Team();
					int id = u.getLong("id").intValue();
					String name = u.getString("name");
					String image_url = u.getString("image_url");
					temp.setId(id);
					temp.setName(name);
					teams.add(temp);
				}
				callback.callback(teams);
			}
		});
	}

	public void getAllMatchResults(final Callback callback){
    	match_results_doc.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				ArrayList<MatchResult> results = new ArrayList<>();
				for (DocumentSnapshot u : queryDocumentSnapshots.getDocuments()){
					MatchResult matchResult = new MatchResult();
					int id = u.getLong("id").intValue();
					int home_score = u.getLong("home_score").intValue();
					int away_score = u.getLong("away_score").intValue();
					int match_id = u.getLong("match_id").intValue();
					int winner_id = u.getLong("winner").intValue();

					Team winner = Manager.getInstance().getTeamCached(winner_id);

					matchResult.setId(id);
					matchResult.setHome_score(home_score);
					matchResult.setAway_score(away_score);
					matchResult.setWinner(winner);
					matchResult.setMatch(match_id);
					results.add(matchResult);
				}
				callback.callback(results);
			}
		});
	}

	public void getAllMatches(final Callback callback) {
		matches_doc.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				ArrayList<Match> matches = new ArrayList<>();
				for (DocumentSnapshot u : queryDocumentSnapshots.getDocuments()){
					final Match match = new Match();

					final int id = u.getLong("id").intValue();
					final Boolean ended = u.getBoolean("ended");
					final Boolean started = u.getBoolean("started");
					com.google.firebase.Timestamp temp = u.getTimestamp("kickoff_time");
					final String k = temp.toString();
					final String venue = u.getString("venue");
					final int home_team_id = u.getLong("home_team_id").intValue();
					final int away_team_id = u.getLong("away_team_id").intValue();
					final int stage = u.getLong("stage").intValue();
					final int result_id = u.getLong("match_result_id").intValue();

					Team away = Manager.getInstance().getTeamCached(away_team_id);
					Team home = Manager.getInstance().getTeamCached(home_team_id);

					MatchResult result = Manager.getInstance().getMatchResultCached(result_id);

					match.setId(id);
					match.setEnded(ended);
					match.setStarted(started);
					match.setAway(away);
					match.setHome(home);
					match.setKickoff_time(new Date());
					match.setStage(stage);
					match.setVenue(venue);
					match.setResult(result);
					matches.add(match);
				}
				callback.callback(matches);
			}
		});
	}

	public void getMatchAsync(int id, final Callback callback){
		final Match match = new Match(0, null, null, null, null, null, false, 0, false);
		Query q = matches_doc.whereEqualTo("id", id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				final int id = u.getLong("id").intValue();
				final Boolean ended = u.getBoolean("ended");
				final Boolean started = u.getBoolean("started");
				com.google.firebase.Timestamp temp = u.getTimestamp("kickoff_time");
				final String k = temp.toString();
				//TODO fix this date stuff
				//String kickoff_time = u.getString("kickoff_time");
				final String venue = u.getString("venue");
				final int home_team_id = u.getLong("home_team_id").intValue();
				final int away_team_id = u.getLong("away_team_id").intValue();
				final int stage = u.getLong("stage").intValue();
				final int result_id = u.getLong("match_result_id").intValue();

				getTeamAsync(home_team_id, new Callback() {
					@Override
					public void callback(final Object home_obj) {
						getTeamAsync(away_team_id, new Callback() {
							@Override
							public void callback(final Object away_obj) {
								getMatchResultAsync(result_id, new Callback() {
									@Override
									public void callback(Object result_obj) {
										MatchResult result = (MatchResult)  result_obj;
										Team away = (Team) away_obj;
										Team home = (Team) home_obj;

										match.setId(id);
										match.setEnded(ended);
										match.setStarted(started);
										match.setAway(away);
										match.setHome(home);
										match.setKickoff_time(new Date());
										match.setStage(stage);
										match.setVenue(venue);
										match.setResult(result);
										callback.callback(match);
									}
								});
							}
						});
					}
				});
			}
		});
	}

	public void getMatchResultAsync(final int id, final Callback callback){
		final MatchResult matchResult = new MatchResult(0, 0, null, 0);
		Query q = match_results_doc.whereEqualTo("id", id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				int id = u.getLong("id").intValue();
				int home_score = u.getLong("home_score").intValue();
				int away_score = u.getLong("away_score").intValue();
				int match_id = u.getLong("match_id").intValue();
				int winner_id = u.getLong("winner").intValue();
				Team winner = null;

				getTeamAsync(winner_id, new Callback() {
					@Override
					public void callback(Object team_obj) {
						Team winner = (Team) team_obj;
					}
				});

				matchResult.setId(id);
				matchResult.setHome_score(home_score);
				matchResult.setAway_score(away_score);
				matchResult.setWinner(winner);
				matchResult.setMatch(match_id);
				callback.callback(matchResult);
			}
		});
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
    	Manager.getInstance().addMatchPrediction(prediction);
		MatchResult mr = prediction.getPredicted_result();
		submitMatchResult(mr);

		String user_id = Manager.getInstance().getPlayingUser().getId();
		int match_id = prediction.getPredicted_result().getMatch();
		int mr_id = mr.getId();
		boolean calculated = prediction.getCalculated();
		int score = prediction.getScore();

		Map<String, Object> doc = new HashMap<>();

		doc.put("user_id", user_id);
		doc.put("match_id", match_id);
		doc.put("match_result_id", mr_id);
		doc.put("calculated", calculated);
		doc.put("score", score);

		Log.i("DAL", "Added predictions");
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


	public void getUser(final String user_id, final Callback callback){
		Query q = users_doc.whereEqualTo("id", user_id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);

				String email = u.getString("email");
				String name = u.getString("name");
				String photo_url = u.getString("photoUrl");
				int points = u.getLong("points").intValue();

				User user = new User(user_id, points, 0, new Info(user_id, name,
						email, new Date(), "LAHOOOOOORE", 0, photo_url));

				callback.callback(user);
			}
		});
	}

	public void getAllUsers(final Callback callback){
		users_doc.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				ArrayList<User> users = new ArrayList<>();
				for (DocumentSnapshot u : queryDocumentSnapshots.getDocuments()) {

					String user_id = u.getString("id");
					String name = u.getString("name");
					String email = u.getString("email");
					String url = u.getString("photoUrl");
					int points = u.getLong("points").intValue();

					Info info = new Info(user_id, name, email, new Date(), "", 0, url);
					User user = new User(user_id, points, 0, info);
					users.add(user);
				}
				callback.callback(users);;
			}
		});
	}

	public void getAllTournamentResults(final Callback callback){
		users_doc.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				ArrayList<TournamentResult> results = new ArrayList<>();
				for (DocumentSnapshot u : queryDocumentSnapshots.getDocuments()) {
					int id = u.getLong("id").intValue();
					int gold = u.getLong("gold").intValue();
					int silver = u.getLong("silver").intValue();
					int bronze = u.getLong("bronze").intValue();

					Team g = Manager.getInstance().getTeamCached(gold);
					Team s = Manager.getInstance().getTeamCached(silver);
					Team b = Manager.getInstance().getTeamCached(bronze);

					TournamentResult result = new TournamentResult(id, g, s, b);
					results.add(result);

				}
				callback.callback(results);
			}
		});
	}

	public void getTournamentPrediction(final String user_id, final Callback callback){
		final TournamentPrediction prediction = new TournamentPrediction(UUID.randomUUID(), null, null);
		final Query q = tournament_predictions.whereEqualTo("user_id", user_id);

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);

				int result_id = u.getLong("result_id").intValue();
				boolean calculated = u.getBoolean("calculated");
				int score = u.getLong("score").intValue();

				TournamentResult tr = Manager.getInstance().getTournamentResultCached(result_id);
				User user = Manager.getInstance().getUserCached(user_id);

				final TournamentPrediction prediction = new TournamentPrediction(UUID.randomUUID(), tr, user, calculated, score);
				Log.i("DAL", "onSuccess: predictions sent");
				callback.callback(prediction);
			}
		});
	}

	public void getPredictions(final String user_id, final Callback callback){
		final ArrayList<MatchPrediction> predictions = new ArrayList<>();
		final Query q = predictions_doc.whereEqualTo("user_id", user_id);

		getUser(user_id, new Callback() {
			@Override
			public void callback(Object o) {
			final User user = (User) o;
			q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
				@Override
				public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
					for (DocumentSnapshot u : queryDocumentSnapshots.getDocuments()){
						int match_id = u.getLong("match_id").intValue();
						int match_result_id = u.getLong("match_result_id").intValue();
						boolean calculated = u.getBoolean("calculated");
						int score = u.getLong("score").intValue();

						MatchResult mr = Manager.getInstance().getMatchResultCached(match_result_id);

						predictions.add(new MatchPrediction(UUID.randomUUID(), mr, user, calculated, score));

					}
					Log.i("DAL", "onSuccess: predictions sent");
					callback.callback(predictions);
				}
			});
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
						if (user.getPoints() < t1.getPoints()){
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
    public static DAL getInstance(){
        if(instance == null)
            instance = new DAL();
        return instance;
    }
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
					final boolean started = Boolean.parseBoolean(m.get("started").toString());
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
						match.setStarted(started);
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
					final boolean started = Boolean.parseBoolean(m.get("started").toString());
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
						match.setStarted(started);
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

	public void updatePrediction(final MatchPrediction prediction) {
		Log.i("DAL", "updating prediction match: " + prediction.getPredicted_result().getMatch());

		// using user_id and match_id
		Query q = predictions_doc.whereEqualTo("match_id", prediction.getPredicted_result().getMatch())
				.whereEqualTo("user_id", prediction.getPredictor().getId());

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				Map<String, Object> update = new HashMap<>();
				update.put("calculated", prediction.getCalculated());
				update.put("score", prediction.getScore());

				u.getId();
				predictions_doc.document(u.getId()).update(update);
			}
		});
	}

	public void updatePlayer(final User user) {
		Log.i("DAL", "Updateing user " + user.getId());

		Query q = users_doc.whereEqualTo("id", user.getId());

		q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				DocumentSnapshot u = queryDocumentSnapshots.getDocuments().get(0);
				Map<String, Object> update = new HashMap<>();
				update.put("points", user.getPoints());

				users_doc.document(u.getId()).update(update);
			}
		});
	}

	public interface Callback {
		void callback(Object o);
	}
}
