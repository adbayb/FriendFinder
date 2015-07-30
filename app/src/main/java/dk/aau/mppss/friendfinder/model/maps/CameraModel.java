package dk.aau.mppss.friendfinder.model.maps;

/**
 * Created by adibayoub on 29/07/2015.
 */
public class CameraModel {
    private double latitude;
    private double longitude;
    private float zoom;

    public CameraModel(double latitude, double longitude, float zoom) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoom = zoom;
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

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}
