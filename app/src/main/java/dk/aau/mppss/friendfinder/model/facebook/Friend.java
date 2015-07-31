package dk.aau.mppss.friendfinder.model.facebook;

import android.location.Location;

/**
 * Created by Sekou on 30/07/15.
 */
public class Friend {
    private String id;
    private String name;
    private String email;
    private String birthday;
    private Location location;

    public Friend(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Friend(String id, String name, String email, String birthday, Location location) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", location=" + location +
                '}';
    }
}




