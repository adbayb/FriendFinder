package dk.aau.mppss.friendfinder.model.maps;

/**
 * Created by adibayoub on 28/07/2015.
 */
public class MarkerModel {
    private double latitude;
    private double longitude;
    private String label;
    //Add color marker!

    public MarkerModel(String label, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.label = label;
    }

    @Override
    public String toString() {
        return "MarkerModel{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", label='" + label + '\'' +
                '}';
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
