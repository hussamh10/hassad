package player.predictions;

import competition.results.MatchResult;
import competition.results.Result;
import player.User;

import java.util.UUID;

public class MatchPrediction extends Prediction{
    private MatchResult predicted_result;

    public MatchPrediction(UUID id, MatchResult predicted_result, User predictor){
        super(id, predictor);
        this.predicted_result = predicted_result;
    }

    public MatchResult getPredicted_result() {
        return predicted_result;
    }

    @Override
    public String toString() {
        return "MatchPrediction{" +
                "predicted_result=" + predicted_result +
                '}' + super.toString();
    }
}
