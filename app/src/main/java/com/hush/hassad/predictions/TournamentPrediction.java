package com.hush.hassad.predictions;

import com.hush.hassad.competition.results.TournamentResult;
import com.hush.hassad.player.User;

import java.util.UUID;

public class TournamentPrediction extends Prediction{
    private TournamentResult predicted_result;

    public TournamentPrediction(UUID id, TournamentResult predicted_result, User predictor){
        super(id, predictor);
        this.predicted_result = predicted_result;
    }

    public TournamentResult getPredicted_result() {
        return predicted_result;
    }

    @Override
    public String toString() {
        return "MatchPrediction{" +
                "predicted_result=" + predicted_result +
                '}' + super.toString();
    }
}
