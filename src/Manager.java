/* NOTES:

    Manager Notes
    - Use throw exception when an error occurs
    - Exception must have appropriate message since that message will be displayed to the user

    - Make all utility methods private
    - All constants must be used from the Constant static class
    - All public methods must take either primitive data types
        or
    - All public methods must take in data types that are returned by some other Manager method

    Project Notes
    - All classes must have a to_string method

 */

import competition.Match;
import competition.Team;
import competition.results.MatchResult;
import competition.results.Result;
import player.Info;
import player.Prediction;
import player.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static java.awt.SystemColor.info;

public class Manager {

    private static Manager instance;
    private User player;

    private Manager(){
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
        // TODO make DAL get user for email @usman
        //return DAL.getUser(String email);
    }

    /*
        TODO
        Create serach___ functions for each class that
        takes a query and returns array of possible matches
     */

    public Prediction createMatchPrediction (Match match, int home_score, int away_score, Team winner){
        Result result = new MatchResult(home_score, away_score, winner, match);
        Prediction  prediction = new Prediction(UUID.randomUUID(), result, this.player);
        return prediction;
    }

    public void submitPrediction(Prediction prediction) throws Exception{
        /*
        TODO DAL.submitPrediction(predicion) @usman
        This will create a new entry in the table with prediction id, match info, result info and user info
        this function returns exception if prediction already made
         */
        if (this.player.getCoins() < Constants.PREDICTION_COST){
            throw new Exception("Not enough Coins! Wallet: " + this.player.getCoins() + " Requierd: " + Constants.PREDICTION_COST + ".");
        }
        this.player.removeCoins(Constants.PREDICTION_COST);
    }

    public void editPrediciton(Prediction prediction)throws Exception{
        /*
        TODO DAL.submitPrediction(predicion) @usman
        This will update entry in the table with prediction id, match info, result info and user info
        this function returns exception if prediction not already made
         */
    }

    public void register(String name, String email, Date DOB, String location, int timezone) throws Exception{
        if (userExists(email)){
            throw new Exception("User with email address: " + email + " already exists.");
        }
        Info info = new Info(name, email, DOB, location, timezone);
        User u = new User(UUID.randomUUID(), Constants.INITIAL_POINTS, Constants.INITIAL_COINS, info);
        setPlayingUser(u);

        /*
            TODO
            DAL.addUser(u)
         */
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
            update user in DAL
         */
    }

    public void compareMatchPrediction(Prediction prediction) throws Exception{
        MatchResult predicted_result;
        Match match;

        try{
            predicted_result = ((MatchResult) prediction.getPredicted_result());
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
