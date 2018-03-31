package com.hush.hassad.predictions;

import com.hush.hassad.player.User;

import java.util.UUID;

public abstract class Prediction {

    private UUID id;
    private User predictor;

    public Prediction(UUID id, User predictor) {
        this.id = id;
        this.predictor = predictor;
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

    @Override
    public String toString() {
        return "Prediction{" +
                "id=" + id +
                ", predictor=" + predictor +
                '}';
    }
}
