package player;

import competition.Player;
import competition.results.Result;

public class Prediction {
    int id;
    Result predicted_result;
    Player predictor;

    public Prediction(int id, Result predicted_result, Player predictor) {
        this.id = id;
        this.predicted_result = predicted_result;
        this.predictor = predictor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Result getPredicted_result() {
        return predicted_result;
    }

    public void setPredicted_result(Result predicted_result) {
        this.predicted_result = predicted_result;
    }

    public Player getPredictor() {
        return predictor;
    }

    public void setPredictor(Player predictor) {
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
