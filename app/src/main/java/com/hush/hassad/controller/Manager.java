package com.hush.hassad.controller;/* NOTES:

    com.hush.hassad.controller.Manager Notes
    - Use throw exception when an error occurs
    - Exception must have appropriate message since that message will be displayed to the user

    - Make all utility methods private
    - All constants must be used from the Constant static class
    - All public methods must take either primitive data types
        or
    - All public methods must take in data types that are returned by some other com.hush.hassad.controller.Manager method

    Project Notes
    - All classes must have a to_string method

 */

import com.hush.hassad.controller.competition.Group;
import com.hush.hassad.controller.competition.Match;
import com.hush.hassad.controller.competition.Team;
import com.hush.hassad.controller.competition.results.GroupResult;
import com.hush.hassad.controller.competition.results.MatchResult;
import com.hush.hassad.controller.player.Info;
import com.hush.hassad.controller.predictions.GroupPrediction;
import com.hush.hassad.controller.predictions.MatchPrediction;
import com.hush.hassad.controller.player.User;
import com.hush.hassad.dal.DAL;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Manager {

    private static DAL db;
    private static Manager instance;
    private User player;

    private Manager(){
        db = DAL.getInstance();
    }

    public static Manager getInstance() {
        if (instance != null){
            return instance;
        }
        return new Manager();
    }

    public void setPlayingUser(User player){
        this.player = player;
    }

    // ------------------------- Funs ---------------------------------

    public ArrayList<Match> getMatches(Date date) throws Exception{
        return db.getMatches(date);
    }

    // -------------------------- End ---------------------------------


    public User getPlayingUser() {
        return player;
    }

    /*
        TODO
        Create get___ functions for each class that
        takes a unique identifier as input
     */

    public User getUser(String email){
        return null;
        //return db.getUserByEmail(email);
        // TODO make com.hush.hassad.dal.DAL get user for email @usman
        //return com.hush.hassad.dal.DAL.getUser(String email);
    }

    public MatchPrediction createMatchPrediction (Match match, int home_score, int away_score, Team winner){
        MatchResult result = new MatchResult(home_score, away_score, winner, match);
        MatchPrediction prediction = new MatchPrediction(UUID.randomUUID(), result, this.player);
        return prediction;
    }

    public void submitMatchPrediction(MatchPrediction prediction) throws Exception{
        /*
        TODO com.hush.hassad.dal.DAL.submitMatchPrediction(predicion) @usman
        This will create a new entry in the table with prediction id, match info, result info and user info
        this function returns exception if prediction already made
         */
        if (this.player.getCoins() < Constants.GROUP_PREDICTION_COST){
            throw new Exception("Not enough Coins! Wallet: " + this.player.getCoins() + " Requierd: " + Constants.MATCH_PREDICTION_COST + ".");
        }
        this.player.removeCoins(Constants.MATCH_PREDICTION_COST);
    }

    public void editMatchPrediciton(MatchPrediction prediction)throws Exception{
        /*
        TODO com.hush.hassad.dal.DAL.submitPrediction(predicion) @usman
        This will update entry in the table with prediction id, match info, result info and user info
        this function returns exception if prediction not already made
         */
    }

    public GroupPrediction createGroupPredicrtion(String team1_name , String team2_name, String group_name){
        //  TODO Group grp = db.getGroupByName(group_name)
        Group grp = null;
        // TODO Team t1 = db.getTeamByName(team1_name);
        // TODO Team t2 = db.getTeamByName(team2_name);

        Team t1 = null;
        Team t2 = null;

        GroupResult gr = new GroupResult(t1, t2, grp);
        GroupPrediction gp = new GroupPrediction(UUID.randomUUID(), gr, player);

        return gp;
    }

    public void submitGroupPrediction(GroupPrediction prediction)throws Exception{
        /*
        TODO com.hush.hassad.dal.DAL.submitGroupPrediction(predicion) @usman
        */

        if (this.player.getCoins() < Constants.GROUP_PREDICTION_COST){
            throw new Exception("Not enough Coins! Wallet: " + this.player.getCoins() + " Requierd: " + Constants.GROUP_PREDICTION_COST + ".");
        }
        this.player.removeCoins(Constants.GROUP_PREDICTION_COST);
    }

    public void register(String name, String email, Date DOB, String location, int timezone) throws Exception{
        if (userExists(email)){
            throw new Exception("User with email address: " + email + " already exists.");
        }
        UUID id = UUID.randomUUID();
        Info info = new Info(id, name, email, DOB, location, timezone);
        User u = new User(id, Constants.INITIAL_POINTS, Constants.INITIAL_COINS, info);
        setPlayingUser(u);
        //db.addUser(u);
    }

    public void addFriend(String email)throws Exception{
        if (!userExists(email)){
            throw new Exception("User with email address:" + email + " does not exist.");
        }
        ArrayList<UUID> friends = player.getFriends();
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

    public void compareMatchPrediction(MatchPrediction prediction) throws Exception{
        MatchResult predicted_result;
        Match match;

        try{
            predicted_result = prediction.getPredicted_result();
            match = predicted_result.getMatch();
        }
        catch (Exception e){
            throw e;
        }

        if (!match.isEnded()){
            throw new Exception("Match has not ended yet.");
        }

        int score = 0;

        score = compareScoreline(match, predicted_result) * Constants.SCORE_PREDICTION_POINTS_WEIGHT;
        score  += compareWinner(match, predicted_result) * Constants.WINNER_PREDICTION_POINTS_WEIGHT;

        int coins = score * Constants.SCORE_TO_COIN_RATIO;
        int points = score * Constants.SCORE_TO_POINT_RATIO;

        player.addCoins(coins);
        player.addPoints(points);

        /*
        TODO Update User in com.hush.hassad.dal.DAL?
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
}
