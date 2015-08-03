package dk.aau.mppss.friendfinder;

import java.util.List;

import dk.aau.mppss.friendfinder.model.maps.MarkerModel;

/**
 * Created by adibayoub on 03/08/2015.
 */
public final class StaticValue {
    public static final int userID = 1;
    public static final String urlCreatePOI = "http://friendfinder.alwaysdata.net/FriendFinder/create_poi.php";
    public static final String urlGetAllPOI = "http://friendfinder.alwaysdata.net/FriendFinder/get_all_poi.php";
    private List<? extends MarkerModel> markers;

    private StaticValue() {

    }

    public List<? extends MarkerModel> getMarkers() {
        return markers;
    }

    public void setMarkers(List<? extends MarkerModel> markers) {
        this.markers = markers;
    }
}
