package dk.aau.mppss.friendfinder.model.maps;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by adibayoub on 04/08/2015.
 */
public class FBMarkerModel extends MarkerModel {
    private String picture;

    public FBMarkerModel(String label, String picture, double latitude, double longitude, Marker marker) {
        super(label, latitude, longitude, marker);
        if(picture != null)
            this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return super.toString() +
                "FBMarkerModel{" +
                "picture='" + picture + '\'' +
                '}';
    }
}
