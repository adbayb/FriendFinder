package dk.aau.mppss.friendfinder.model.facebook;

/**
 * Created by Sekou on 30/07/15.
 */
public class Friend extends User {

    public Friend(String id, String name, String email, String picture) {
        super(id, name, email, picture);
    }

    @Override
    public String toString() {
        return super.toString() +
                "Friend{}";
    }
}




