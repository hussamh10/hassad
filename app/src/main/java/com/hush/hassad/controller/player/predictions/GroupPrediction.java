package com.hush.hassad.controller.player.predictions;

import com.hush.hassad.controller.competition.results.GroupResult;

import com.hush.hassad.controller.player.User;

import java.util.UUID;

public class GroupPrediction extends Prediction{
    private GroupResult predicted_result;

    public GroupPrediction(UUID id, GroupResult predicted_result, User predictor){
        super(id, predictor);
        this.predicted_result = predicted_result;
    }

    public GroupResult getPredicted_result() {
        return predicted_result;
    }

    @Override
    public String toString() {
        return "MatchPrediction{" +
                "predicted_result=" + predicted_result +
                '}' + super.toString();
    }
}