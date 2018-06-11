package com.hush.hassad.controller.predictions;

import com.hush.hassad.controller.Constants;
import com.hush.hassad.controller.competition.results.MatchResult;

import com.hush.hassad.controller.player.User;

import java.util.UUID;

import static java.lang.Math.abs;

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
