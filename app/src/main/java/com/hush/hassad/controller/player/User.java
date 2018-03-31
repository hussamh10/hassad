package com.hush.hassad.controller.player;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    private UUID id;
    private int points;
    private int coins;
    private Info info;
    private ArrayList<UUID> friends_ids;

    public User(UUID id, int points, int coins, Info info) {
        this.id = id;
        this.points = points;
        this.coins = coins;
        this.info = info;
        friends_ids = new ArrayList<UUID>();
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

    public void addFriend(UUID friend_id){
        friends_ids.add(friend_id);
    }

    public ArrayList<UUID> getFriends(){
        return friends_ids;
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
