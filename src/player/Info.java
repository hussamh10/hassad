package player;

import java.util.Date;

public class Info {

    private int userID;
    private String name;
    private String email;
    private Date DOB;
    private String location;


    public Info(int userID, String name, String email, Date DOB, String location) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.DOB = DOB;
        this.location = location;
    }

    public Info(String name, String email, Date DOB, String location) {
        this.name = name;
        this.email = email;
        this.DOB = DOB;
        this.location = location;
    }

    public int getUserID() {
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


    @Override
    public String toString() {
        return "Info{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", DOB=" + DOB +
                ", location='" + location + '\'' +
                '}';
    }
}
