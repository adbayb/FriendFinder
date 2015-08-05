package dk.aau.mppss.friendfinder.model.maps;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adibayoub on 28/07/2015.
 */
public class MapsModel {
    private GoogleMap googleMap;
    //Changement List en Map pour optimiser la recherche d'un
    //MarkerModel depuis la latitude et longitude d'un Marker en clé:
    private Map<String, MarkerModel> markersList;

    public MapsModel(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.markersList = new HashMap<String, MarkerModel>();
    }

    public void updateMapView(MapView mapView) {
        this.googleMap = mapView.getMap();
    }

    //addMarker only on map but not on the list:
    public MarkerModel addMapMarker(MarkerModel markerModel, int idIconImage) {
        if(markerModel != null) {

            MarkerOptions markerOptions = new MarkerOptions().position(
                    new LatLng(markerModel.getLatitude(), markerModel.getLongitude())
            )
                    .title(markerModel.getLabel())
                    .icon(BitmapDescriptorFactory.fromResource(idIconImage));
            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))

            //addMarker returns a Marker from google maps api:
            Marker marker = this.googleMap.addMarker(markerOptions);
            //We add Marker reference inside MarkerModel to be able to remove it easily after:
            markerModel.setMarker(marker);
            //Enable Drag & Drop on marker:
            marker.setDraggable(true);

            return markerModel;
        }

        return null;
    }

    //addMarker to map and list both:
    public MarkerModel addMarker(MarkerModel _markerModel, int idIconImage) {
        MarkerModel markerModel = this.addMapMarker(_markerModel, idIconImage);
        if(markerModel != null) {
            if(this.markersList != null) {
                String key = markerModel.getLatitude() + "-" + markerModel.getLongitude();
                this.markersList.put(key, markerModel);

                return markerModel;
            }
            return null;
        }
        return null;
    }

    public boolean removeMarker(Marker marker) {
        if(marker != null) {
            this.removeMarkerFromList(marker);
            marker.remove();
            return true;
        }
        return false;
    }

    public boolean removeMarkerFromList(Marker marker) {
        String searchKey = marker.getPosition().latitude + "-" + marker.getPosition().longitude;

        return (
                (this.markersList.get(searchKey) == null) ? false : true
        );
    }

    //Optimisation recherche marker spécifique dans Map via une clé unique définit par latitude et longitude:
    public MarkerModel findMarkerModelFromMarker(Marker marker) {
        Log.e("DEBUG33333", "PASSSSS");
        //Research with key:
        if(marker != null) {
            String searchKey = marker.getPosition().latitude + "-" + marker.getPosition().longitude;
            Log.e("DEBUG33333", searchKey);
            //returns null if not found:
            return this.markersList.get(searchKey);
        }
        return null;
    }

    public MarkerModel findMarkerModelFromPosition(LatLng latLng) {
        if(latLng != null) {
            String searchKey = latLng.latitude + "-" + latLng.longitude;

            return this.markersList.get(searchKey);
        }
        return null;
    }

    /*
    public boolean removeMarkerFromList(Marker marker) {
        if (marker != null) {
            if(this.markersList != null) {
                //We must use Iterator instead of a foreach loop to avoid ConcurrentModificationException
                //while removing an item inside the loop. Iterator remove function does it safely:
                Iterator<MarkerModel> itMarker = this.markersList.iterator();
                while(itMarker.hasNext()) {
                    MarkerModel currentMarkerModel = itMarker.next();
                    //As when an orientation occurred, we lost markers, we restore them
                    //by creating new Markers so we must not check id object with .equals:
                    if(currentMarkerModel.getLatitude() == marker.getPosition().latitude
                            && currentMarkerModel.getLongitude() == marker.getPosition().longitude) {
                        itMarker.remove();
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public MarkerModel findMarkerModelFromList(Marker marker) {
        for(MarkerModel markerModelFromList : this.markersList) {
            // does not seem to work well,
            //use .equals instead of "currentMarker == marker" to check if it's same object:
            if(markerModelFromList.getLatitude() == marker.getPosition().latitude
                    && markerModelFromList.getLongitude() == marker.getPosition().longitude) {
                return markerModelFromList;
            }
        }
        return null;
    }
    */

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

    public Map<String, MarkerModel> getMarkersList() {
        return markersList;
    }

    public void setMarkersList(Map<String, MarkerModel> markersList) {
        this.markersList = markersList;
    }
}
