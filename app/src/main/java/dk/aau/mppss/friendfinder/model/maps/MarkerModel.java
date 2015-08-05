package dk.aau.mppss.friendfinder.model.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by adibayoub on 28/07/2015.
 */
public class MarkerModel {
    private double latitude;
    private double longitude;
    private String label;
    private Marker marker;
    //Add color marker!

    public MarkerModel(String label, double latitude, double longitude, Marker marker) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.label = label;
        this.marker = marker;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean updateMarker(double latitude, double longitude) {
        if(this.marker != null) {
            this.marker.setPosition(new LatLng(latitude, longitude));

            return true;
        }
        return false;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public String toString() {
        return "MarkerModel{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", label='" + label + '\'' +
                ", marker=" + marker +
                '}';
    }
}
