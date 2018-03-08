package player;

public class User {
    private int id;
    private int points;
    private int coins;

    public User(int id, int points, int coins) {
        this.id = id;
        this.points = points;
        this.coins = coins;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", points=" + points +
                ", coins=" + coins +
                '}';
    }
}
