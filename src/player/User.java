package player;

import java.util.UUID;

public class User {
    private UUID id;
    private int points;
    private int coins;
    private Info info;

    public User(UUID id, int points, int coins, Info info) {
        this.id = id;
        this.points = points;
        this.coins = coins;
        this.info = info;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Info getInfo() {return info;}

    public void setInfo(Info info) {this.info = info;}


    public void addCoins(int amount){
        this.coins += amount;
    }

    public void removeCoins(int amount){
        this.coins -= amount;
    }

    public void addPoints(int amount){
        this.points += amount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", points=" + points +
                ", coins=" + coins +
                ", info=" + info +
                '}';
    }
}
