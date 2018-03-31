package com.hush.hassad.controller.player;

import java.util.Date;
import java.util.UUID;

public class Info {

    private UUID userID;
    private String name;
    private String email;
    private Date DOB;
    private String location;
    private int timezone;

    public Info(UUID userID, String name, String email, Date DOB, String location, int timezone) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.DOB = DOB;
        this.location = location;
        this.timezone = timezone;
    }

    public UUID getUserID() {
        return userID;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Date getDOB() { return DOB; }

    public void setDOB(Date DOB) { this.DOB = DOB;    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public int getTimezone() { return timezone; }

    public void setTimezone(int timezone) { this.timezone = timezone; }

    @Override
    public String toString() {
        return "Info{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", DOB=" + DOB +
                ", location='" + location + '\'' +
                ", timezone=" + timezone +
                '}';
    }
}