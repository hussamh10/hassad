package com.hush.hassad.controller.predictions;

import com.hush.hassad.controller.player.User;

import java.util.UUID;

public abstract class Prediction {

    private UUID id;
    private User predictor;
    protected boolean calculated;
    private int score;

    public Prediction(UUID id, User predictor) {
        this.id = id;
        this.predictor = predictor;
        calculated = false;
        score = 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getPredictor() {
        return predictor;
    }

    public void setPredictor(User predictor) {
        this.predictor = predictor;
    }

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
    public String toString() {
        return "Prediction{" +
                "id=" + id +
                ", predictor=" + predictor +
				", calculated=" + calculated +
                '}';
    }
}
