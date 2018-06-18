package com.hush.hassad.controller.predictions;

import com.hush.hassad.controller.competition.results.TournamentResult;
import com.hush.hassad.controller.player.User;

import java.util.UUID;

public class TournamentPrediction extends Prediction{
    private TournamentResult predicted_result;

	public TournamentPrediction(UUID id, TournamentResult predicted_result, User predictor, boolean calculated, int score) {
		super(id, predictor);
		this.predicted_result = predicted_result;
		this.calculated = calculated;
		setScore(score);
	}


    public TournamentPrediction(UUID id, TournamentResult predicted_result, User predictor){
        super(id, predictor);
        calculated = false;
        setScore(0);
        this.predicted_result = predicted_result;
    }

    public TournamentResult getPredicted_result() {
        return predicted_result;
    }

    public void setScore(int score){
    	super.setScore(score);
	}

	public int getScore(){
    	return super.getScore();
	}

	public void setCalculated(boolean calculated){
    	super.calculated = calculated;
	}

	public boolean getCalculated(){
    	return super.calculated;
	}

    @Override
    public String toString() {
        return "MatchPrediction{" +
                "predicted_result=" + predicted_result +
                '}' + super.toString();
    }
}
