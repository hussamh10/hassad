package com.hush.hassad.controller.competition;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.BitSet;

public class Team implements Serializable {
    private int id;
    private String name;
    private Bitmap image_url;

    public Team() {
        this(0, null, null);
    }
    public Team(int id, String name, Bitmap image_url) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
    }

    // Auto Generated Code

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

    public Bitmap getImage_url() {
        return image_url;
    }

    public void setImage_url(Bitmap image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "Team{" +
                "ID=" + id +
                ", name='" + name + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
