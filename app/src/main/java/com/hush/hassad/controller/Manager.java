package com.hush.hassad.controller;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.Team;
import com.hush.hassad.controller.competition.results.MatchResult;
import com.hush.hassad.controller.competition.results.TournamentResult;
import com.hush.hassad.controller.player.Info;
import com.hush.hassad.controller.predictions.MatchPrediction;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.TournamentPrediction;
import com.hush.hassad.dal.DAL;
import com.hush.hassad.ui.activities.MainActivity;
import com.hush.hassad.ui.fragments.home.DayFragment;
import com.hush.hassad.ui.fragments.home.HomeFragment;

import java.util.ArrayList; import java.util.Date;
import java.util.UUID;

import static java.lang.Math.abs;

public class Manager {

    private static DAL db;
    private static final Manager instance = new Manager();
    private static User player;
	private ArrayList<User> friends;
	private boolean loaded = false;
	private ArrayList<MatchPrediction> user_predictions;
	private ArrayList<MatchResult> match_results;
	private ArrayList<Match> matches;
	private ArrayList<Team> teams;
	private ArrayList<User> users;
	private ArrayList<TournamentResult> tournament_results;
	private TournamentPrediction tournament_perdiction;

	private Manager(){
		Log.i("Manager", "Created");
        db = DAL.getInstance();
        user_predictions = new ArrayList<>();
    }

    public static Manager getInstance() {
        if (instance == null){
			return new Manager();
        }
		return instance;
    }
    
    
    // call this method when done loading everything
    private void setLoaded() {
		loaded = true;
		notifyObservers();
	}

	public void loading(){
		loadUsers();
	}

	public void loadUsers(){
		DAL.getInstance().getAllUsers(new DAL.Callback() {
			@Override
			public void callback(Object o) {
				users = (ArrayList<User>) o;
				Log.i("Loading", "callback: Users loaded");
				loadTeams();
			}
		});
	}

	public void loadTeams(){
		DAL.getInstance().getAllTeams(new DAL.Callback() {
			@Override
			public void callback(Object o) {
				teams = (ArrayList<Team>) o;
				Log.i("Loading", "callback: Teams loaded");
				loadMatchesResults();
			}
		});
	}

	public void loadMatchesResults(){
		DAL.getInstance().getAllMatchResults(new DAL.Callback() {
			@Override
			public void callback(Object o) {
				match_results = (ArrayList<MatchResult>) o;
				Log.i("Loading", "callback: results loaded");
				loadMatches();
			}
		});
	}

	public void loadMatches(){
		DAL.getInstance().getAllMatches(new DAL.Callback() {
			@Override
			public void callback(Object o) {
				matches = (ArrayList<Match>) o;
				Log.i("Loading", "callback: Matches loaded");
				loadPredictions();
			}
		});
	}


	public void loadPredictions(){
		DAL.getInstance().getPredictions(player.getId(), new DAL.Callback() {
			@Override
			public void callback(Object o) {
				user_predictions = (ArrayList<MatchPrediction>) o;
				Log.i("Loading", "callback: Predictions loaded");
				loadTournamentResults();
				calculatePredictions();
			}
		});
	}

	public void loadTournamentResults(){
		DAL.getInstance().getAllTournamentResults(new DAL.Callback() {
			@Override
			public void callback(Object o) {
				tournament_results = (ArrayList<TournamentResult>) o;
				Log.i("Loading", "callback: TournamentResult loaded");
				loadTournamentPrediction();
			}
		});
	}

	public void loadTournamentPrediction(){
		DAL.getInstance().getTournamentPrediction(player.getId(), new DAL.Callback() {
			@Override
			public void callback(Object o) {
				tournament_perdiction = (TournamentPrediction) o;
				setLoaded();
				Log.i("Loading", "callback: TournamentResult loaded");
				Log.i("DONE" ,"+++++++++++++++++++++++++++++++++++++++++++++ ALL LOADED ++++++++++++++++++++++++++++++++++++++++++++++");
			}
		});
	}

    public void setPlayingUser(User player){
        this.player = player;
        loading();
    }

    public void calculatePredictions(){
		/*
		walks through all the predicitoins the user has ever made
		if the prediction is not calculated, it gets the match from the DB
		if the match has ended then the score is calculated
		the score is then updated in the DB along with the calculated boolean
		 */

		for (final MatchPrediction prediction : user_predictions){
			if (!prediction.getCalculated()){
				final int match_id = prediction.getPredicted_result().getMatch();
				Match match = getMatchCached(match_id);
				if (match.isEnded()){
					if (prediction.getCalculated() == false){
						int score = calculatePrediction(prediction.getPredicted_result(), match.getResult());
						prediction.setScore(score);
						prediction.setCalculated(true);
						DAL.getInstance().updatePrediction(prediction);
						Log.i("DAL", "Prediction calculated matchid " + match.getId());
					}
				}
			}
		}
		updateUserScore();
	}

	public void updateUserScore(){
		int score = 0;
		for (final MatchPrediction prediction : user_predictions){
			if (prediction.getCalculated() == true){
				score += prediction.getScore();
			}
		}
		if (player.getPoints() != score){
			player.setPoints(score);
			DAL.getInstance().updatePlayer(player);
		}
		Log.i("Manager", "updateUserScore: score already up to date");
	}

	public int calculatePrediction(MatchResult predicted, MatchResult actual){
		return 100;
	}


	public int compareMatchPrediction(MatchResult predicted, MatchResult actual){
		int predicted_gd = abs(predicted.getAway_score() - predicted.getHome_score());
		int predicted_gs = abs(predicted.getAway_score() + predicted.getHome_score());

		int actual_gd = abs(actual.getAway_score() - actual.getHome_score());
		int actual_gs = abs(actual.getAway_score() + actual.getHome_score());

		int gd_score = Constants.GD_INITIAL_SCORE - (abs(predicted_gd - actual_gd) * Constants.GOAL_DIFFERENCE_WEIGHT);

		int gs_score = Constants.GS_INITIAL_SCORE - (abs(predicted_gs - actual_gs) * Constants.GOAL_SUM_WEIGHT);

		int winner_score = 0;

		if (match_draw(actual)){
			if(match_draw(predicted)){
				winner_score = Constants.WINNER_PREDICTION_SCORE;
			}
			else{
				winner_score = 0;
			}
		}
		else{
			if(actual.getWinner() == predicted.getWinner()){
				winner_score = Constants.WINNER_PREDICTION_SCORE;
			}
			else{
				winner_score = 0;
			}
		}

		if (gs_score < 0){
			gs_score = 0;
		}

		if (gd_score < 0){
			gd_score = 0;
		}

		int score = gs_score + gd_score + winner_score;

		return score;
	}

	public boolean match_draw(MatchResult result){
		if(result.getHome_score() == result.getAway_score()){
			return true;
		}
		return false;
	}


    // ================================ Creators ==============================


    public MatchPrediction createMatchPrediction(Match match, int home_score, int away_score, Team winner){
        MatchResult predicted_result = new MatchResult(home_score, away_score, winner, match.getId());
        MatchPrediction prediction = new MatchPrediction(UUID.randomUUID(), predicted_result, player);

        return prediction;
    }

    // =============================== GETTERS ============================

    public User getPlayingUser() {
        return player;
    }

    public User getUser(String email){
        return null;
        //return db.getUserByEmail(email);
        // TODO make com.hush.hassad.dal.DAL get user for email @usman
        //return com.hush.hassad.dal.DAL.getUser(String email);
    }

    public Team getTeam(int id){
        return db.getTeam(id);
    }

    public Match getMatch(int id){
    	return db.getMatch(id);
    }
    //TODO Prediction
/*

    public ArrayList<Match> getMatches(Date date) throws Exception{
        return db.getMatches(date);
    }
*/

    public ArrayList<MatchPrediction> getPredictedMatches() {
    	return user_predictions;
    }

    public MatchPrediction getPrediction(Match match)throws Exception{
		for (MatchPrediction p : user_predictions){
			if (match.getId() == p.getPredicted_result().getMatch()){
				return p;
			}
		}
        throw new Exception("Match not yet predicted");
    }

	public ArrayList<User> getFriends() {
    	return db.getFriends(player);
	}


    /*
            TODO COMPLETE THE FOLLOWING FUNCTIONS
     */

    // ============================= DAL entries ==============================

    public void submitMatchPrediction(MatchPrediction prediction) throws Exception{
		if (isPredicted(prediction)){
			throw new Exception("You have already predicted this match.");
		}
		else{
			DAL.getInstance().submitPrediction(prediction);
		}

    }

    public boolean isPredicted(int match_id){
		for (MatchPrediction p : user_predictions){
			if (match_id == p.getPredicted_result().getMatch()){
				return true;
			}
		}
		return false;
	}

    public boolean isPredicted(MatchPrediction prediction){
    	for (MatchPrediction p : user_predictions){
    		if (prediction.getPredicted_result().getMatch() == p.getPredicted_result().getMatch()){
    			return true;
			}
		}
    	return false;
	}

    public void register(String name, String email, Date DOB, String location, int timezone) throws Exception{
        if (userExists(email)){
            throw new Exception("User with email address: " + email + " already exists.");
        }
        String id = UUID.randomUUID().toString();
        String photoUrl = "https://www.google.com/imgres?imgurl=https%3A%2F%2Flibrary.stanford.edu%2Fsites%2Fdefault%2Ffiles%2Fperson%2Fimage%2Fimg_0017.jpg&imgrefurl=https%3A%2F%2Flibrary.stanford.edu%2Fpeople%2Faalsum&docid=2ZcGmzIUiq9mtM&tbnid=GB9HnX-iGA45LM%3A&vet=1&w=4213&h=3602&safe=off&client=safari&bih=907&biw=1680&ved=0ahUKEwjxnerb0sHbAhWhK5oKHbiWCioQMwgzKAAwAA&iact=c&ictx=1";
        Info info = new Info(id, name, email, DOB, location, timezone, photoUrl);
        User u = new User(id, Constants.INITIAL_POINTS, Constants.INITIAL_COINS, info);
        setPlayingUser(u);
        //db.addUser(u);
    }

    public void addFriend(String email)throws Exception{
        if (!userExists(email)){
            throw new Exception("User with email address:" + email + " does not exist.");
        }
        ArrayList<String> friends = player.getFriends();
        User friend = getUser(email);

        if (friends.contains(friend.getId())){
            throw new Exception("Already following " + friend.getInfo().getName() + ".");
        }

        player.addFriend(friend.getId());

        /*
            TODO
            update user in com.hush.hassad.dal.DAL
         */
    }

    // ------------------------------------- Utility ------------------------------------------


    private boolean userExists(String email){
        if (getUser(email) == null){
            return false;
        }
        return true;
    }

	public boolean isPredicted(Match match) {
        ArrayList<MatchPrediction> predictions = getPredictedMatches();
        for(MatchPrediction prediction : predictions){
            if(prediction.getPredicted_result().getMatch() == match.getId()){
                return true;
            }
        }
        return false;
	}

	public void addMatchPrediction(MatchPrediction prediction) {
    	user_predictions.add(prediction);
	}

	public boolean isLoaded() {
		return loaded;
	}

	public User getUserCached(String id) {
		for (User user : users){
			if(user.getId() == id){
				return user;
			}
		}
		Log.e("Error", "getTeamCached: No user found with id" + id );
		return new User("", 0, 0, null);
	}

	public TournamentResult getTournamentResultCached(int id) {
		for (TournamentResult result : tournament_results){
			if(result.getId() == id){
				return result;
			}
		}
		Log.e("Error", "getTeamCached: No tournament_result found with id" + id );
		return new TournamentResult(-1, null, null, null);
	}


	public Team getTeamCached(int id) {
    	for (Team team : teams){
    		if(team.getId() == id){
    			return team;
			}
		}
		Log.e("Error", "getTeamCached: No team found with id" + id );
		return new Team();
	}

	public MatchResult getMatchResultCached(int result_id) {
		for (MatchResult mr : match_results){
			if(mr.getId() == result_id){
				return mr;
			}
		}
		Log.e("Error", "getMatchResultCached: No result found with id" + result_id );
		return new MatchResult();
	}

	public Match getMatchCached(int id) {
		for (Match match : matches){
			if(match.getId() == id){
				return match;
			}
		}
		Log.e("Error", "getMatchCached: No result found with id" + id );
		return new Match();
	}

	public TournamentPrediction getTournamentPredictionCached(){
		return tournament_perdiction;
	}
	
	
	// code for notifying observers
	
	private ArrayList<IMatchObserver> matchObservers = new ArrayList<>();
	
	public void notifyMeWhenMatchesLoaded(IMatchObserver observer) {
    	if (loaded) {
    		observer.matchesLoaded(matches);
		} else {
    		matchObservers.add(observer);
		}
	}
	
	private void notifyObservers() {
		for (IMatchObserver observer : matchObservers) {
			observer.matchesLoaded(matches);
		}
		
		// call other observers here if need be
	}
	
	public interface IMatchObserver {
    	void matchesLoaded(ArrayList<Match> m);
	}
	

	
}
