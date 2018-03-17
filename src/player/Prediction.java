package player;

import player.User;
import competition.results.Result;

import java.util.UUID;

public class Prediction {

    private UUID id;
    private Result predicted_result;
    private User predictor;

    public Prediction(UUID id, Result predicted_result, User predictor) {
        this.id = id;
        this.predicted_result = predicted_result;
        this.predictor = predictor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Result getPredicted_result() {
        return predicted_result;
    }

    public void setPredicted_result(Result predicted_result) {
        this.predicted_result = predicted_result;
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
                ", predicted_result=" + predicted_result +
                ", predictor=" + predictor +
                '}';
    }
}
