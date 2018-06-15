package com.hush.hassad.controller;

import android.util.Log;

import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.Team;
import com.hush.hassad.controller.competition.results.MatchResult;
import com.hush.hassad.controller.player.Info;
import com.hush.hassad.controller.predictions.MatchPrediction;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.controller.predictions.TournamentPrediction;
import com.hush.hassad.dal.DAL;
import com.hush.hassad.ui.activities.MainActivity;

import java.util.ArrayList; import java.util.Date;
import java.util.UUID;

public class Manager {

    private static DAL db;
    private static final Manager instance = new Manager();
    private static User player;
	private ArrayList<User> friends;
	private boolean loaded = false;
	private ArrayList<MatchPrediction> user_predictions;

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

	public void loading(){
		loadPredictions();
	}

	public void loadPredictions(){
		DAL.getInstance().getPredictions(player.getId(), new DAL.Callback() {
			@Override
			public void callback(Object o) {
				ArrayList<MatchPrediction> temp = (ArrayList<MatchPrediction>) o;
				for (MatchPrediction i : temp){
					user_predictions.add(i);
				}
				Log.i("Manager", "callback: Predictions loaded");
				loaded = true;
			}
		});
	}

    public void setPlayingUser(User player){
        this.player = player;
        loading();
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

    public ArrayList<MatchPrediction> getPredictedMatches(User user) {
        return db.getPredictedMatches(user);
    }

    public TournamentPrediction getTournamentPrediction(User user) {
        return db.getTournamentPrediction(user);
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

    public void editMatchPrediciton(MatchPrediction prediction)throws Exception{
        /*
        TODO com.hush.hassad.dal.DAL.submitPrediction(predicion) @usman
        This will update entry in the table with prediction id, match info, result info and user info
        this function returns exception if prediction not already made
         */
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

    private int compareScoreline(Match m, MatchResult mr){
        int similarity = 0;

        if (m.getResult().getAway_score() == mr.getAway_score()){
            similarity ++;
        }

        if (m.getResult().getHome_score() == mr.getHome_score()){
            similarity ++;
        }

        return similarity;
    }

    private int compareWinner(Match m, MatchResult mr){
        if (m.getResult().getWinner().getId() == mr.getWinner().getId()){
            return 1;
        }
        return 0;
    }

    private boolean userExists(String email){
        if (getUser(email) == null){
            return false;
        }
        return true;
    }

	public boolean isPredicted(Match match) {
        ArrayList<MatchPrediction> predictions = getPredictedMatches(player);
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
}
