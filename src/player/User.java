package player;

public class User {
    private int id;
    private int points;
    private int coins;
    private Info info;

    public User(int id, int points, int coins, Info info) {
        this.id = id;
        this.points = points;
        this.coins = coins;
        this.info = info;
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

    public Info getInfo() {return info;}

    public void setInfo(Info info) {this.info = info;}

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
