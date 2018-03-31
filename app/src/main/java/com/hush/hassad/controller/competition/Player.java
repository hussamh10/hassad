package com.hush.hassad.controller.competition;

import com.hush.hassad.controller.competition.Team;

public class Player {
    private int id;
    private String name;
    private String role;
    private Team team;
    private String image_url;

    public Player(int id, String name, String role, Team team, String image_url) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.team = team;
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", team=" + team +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
