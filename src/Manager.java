import competition.Match;
import competition.Team;
import competition.results.MatchResult;
import competition.results.Result;
import player.Prediction;
import player.User;

import java.util.UUID;

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

    public void setPlayer(User player){
        this.player = player;
    }

    public User getPlayer() {
        return player;
    }

    /*
        TODO
        Create get___ functions for each class that
        takes a unique identifier as input
     */

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
        TODO DAL.submitPrediction(predicion)
        This will create a new entry in the table with prediction id, match info, result info and user info
        this function returns exception if prediction already made
         */
        if (this.player.getCoins() < Constants.PREDICTION_COST){
            throw new Exception("Not enough Coins! Wallet: " + this.player.getCoins() + " Requierd: " + Constants.PREDICTION_COST);
        }
        this.player.removeCoins(Constants.PREDICTION_COST);
    }

    public void editPrediciton(Prediction prediction)throws Exception{
        /*
        TODO DAL.submitPrediction(predicion)
        This will update entry in the table with prediction id, match info, result info and user info
        this function returns exception if prediction not already made
         */
    }
}
