package dk.aau.mppss.friendfinder.model.maps;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by adibayoub on 28/07/2015.
 */
public class MapsModel {
    private GoogleMap googleMap;
    private List<Marker> markersList;

    public MapsModel(MapView mapView) {
        this.googleMap = mapView.getMap();
        this.markersList = new ArrayList<Marker>();
    }

    public void updateMapView(MapView mapView) {
        this.googleMap = mapView.getMap();
    }

    //addMarker only on map but not on the list:
    public Marker addMapMarker(MarkerModel markerModel) {
        if (markerModel != null) {

            MarkerOptions markerOptions = new MarkerOptions().position(
                    new LatLng(markerModel.getLatitude(), markerModel.getLongitude())
            ).title(markerModel.getLabel());
            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))

            //addMarker returns a Marker from google maps api:
            Marker marker = this.googleMap.addMarker(markerOptions);
            marker.setDraggable(true);

            return marker;
        }

        return null;
    }

    //addMarker to map and list both:
    public boolean addMarker(MarkerModel markerModel) {
        Marker marker = this.addMapMarker(markerModel);
        if (marker != null) {
            this.markersList.add(marker);

            return true;
        }
        return false;
    }

    public boolean removeMarker(Marker marker) {
        if (marker != null) {
            this.removeMarkerFromList(marker);
            marker.remove();
            return true;
        }
        return false;
    }

    public boolean removeMarkerFromList(Marker marker) {
        if (marker != null) {
            //We must use Iterator instead of a foreach loop to avoid ConcurrentModificationException
            //while removing an item inside the loop. Iterator remove function does it safely:
            Iterator<Marker> itMarker = this.markersList.iterator();
            while (itMarker.hasNext()) {
                Marker currentMarker = itMarker.next();
                //As when an orientation occurred, we lost markers, we restore them
                //by creating new Markers so we must not check id object with .equals:
                if (currentMarker.getPosition().latitude == marker.getPosition().latitude
                        && currentMarker.getPosition().longitude == marker.getPosition().longitude) {
                    itMarker.remove();
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    public int findMarkerFromList(Marker marker) {
        int index = 0;

        for (Marker markerFromList : this.markersList) {
            // does not seem to work well,
            //use .equals instead of "currentMarker == marker" to check if it's same object:
            if (markerFromList.equals(marker)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void moveCamera(CameraModel cameraModel) {
        this.googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(cameraModel.getLatitude(), cameraModel.getLongitude()),
                        cameraModel.getZoom()
                )
        );
    }

    public LatLng getPositionCamera() {
        double latitude = this.googleMap.getCameraPosition().target.latitude;
        double longitude = this.googleMap.getCameraPosition().target.longitude;

        return new LatLng(latitude, longitude);
    }

    //set camera position with animation:
    public void moveAnimatedCamera(CameraModel cameraModel) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(cameraModel.getLatitude(), cameraModel.getLongitude())
        ).zoom(cameraModel.getZoom()).build();

        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public float getZoomCamera() {
        return this.googleMap.getCameraPosition().zoom;
    }

    public CameraModel getCamera() {
        return new CameraModel(
                this.googleMap.getCameraPosition().target.latitude,
                this.googleMap.getCameraPosition().target.longitude,
                this.googleMap.getCameraPosition().zoom
        );
    }

    public CameraPosition getCameraPosition() {
        return this.googleMap.getCameraPosition();
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public List<Marker> getMarkersList() {
        return markersList;
    }

    public void setMarkersList(List<Marker> markersList) {
        this.markersList = markersList;
    }
}
