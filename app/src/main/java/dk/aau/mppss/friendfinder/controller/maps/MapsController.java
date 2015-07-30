package dk.aau.mppss.friendfinder.controller.maps;

import android.view.LayoutInflater;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import dk.aau.mppss.friendfinder.MainActivity;
import dk.aau.mppss.friendfinder.model.maps.CameraModel;
import dk.aau.mppss.friendfinder.model.maps.MapsModel;
import dk.aau.mppss.friendfinder.model.maps.MarkerModel;
import dk.aau.mppss.friendfinder.view.fragments.EditMarkerFragment;

/**
 * Created by adibayoub on 28/07/2015.
 */
public class MapsController {
    private MapsModel maps;
    private MainActivity mainActivity;

    public MapsController(MapView mapView, LayoutInflater inflater) {
        this.maps = new MapsModel(mapView);
    }

    public void enableWindowAdapter(MainActivity mainActivity, LayoutInflater inflater) {
        if(this.mainActivity == null)
            this.mainActivity = mainActivity;
        //Set up WindowAdapter for our marker maps: (i.e. popup box when clicking on a marker):
        MapsWindowAdapter mapsWindowAdapter = new MapsWindowAdapter(mainActivity, inflater, true);
        this.maps.getGoogleMap().setInfoWindowAdapter(mapsWindowAdapter);
        this.maps.getGoogleMap().setOnInfoWindowClickListener(mapsWindowAdapter);
    }

    public void updateMapView(MapView mapView) {
        this.maps.updateMapView(mapView);
    }

    public void initializeMaps() {
        //gui maps settings:
        this.maps.getGoogleMap().getUiSettings().setAllGesturesEnabled(true);
        this.maps.getGoogleMap().getUiSettings().setCompassEnabled(true);
        this.maps.getGoogleMap().getUiSettings().setMyLocationButtonEnabled(true);
        this.maps.getGoogleMap().getUiSettings().setZoomControlsEnabled(true);
        //map settings itself:
        this.maps.getGoogleMap().setIndoorEnabled(true);
        this.maps.getGoogleMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.maps.getGoogleMap().setBuildingsEnabled(true);
        this.maps.getGoogleMap().setTrafficEnabled(true);
        this.maps.getGoogleMap().setMyLocationEnabled(true);
        //this.maps.addMarker(new MarkerModel("TOTO",25.6, 27.8));
        //this.maps.setCameraPosition(25.6, 27.8, 6);
    }

    public void addPOIListener() {
        this.maps.getGoogleMap().setOnMapClickListener(
                new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //Log.e("Ayoub log: ", "Maps clicked! "+latLng.latitude+"-"+latLng.longitude);
                        if(mainActivity != null)
                            mainActivity.replaceFragment(new EditMarkerFragment());

                        maps.addMarker(new MarkerModel("test POI", latLng.latitude, latLng.longitude));
                    }
                }
        );

        return;
    }

    //No need to move a friend point! fixed coordinates in database:
    //@TODO implement it on a next version?
    public void movePOIListener() {

    }

    public void removePOIListener() {
        this.maps.getGoogleMap().setOnMarkerDragListener(
                new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        //We simulate a long click on marker with dragger:
                        if(marker != null) {
                            //Log.d("Before: ", maps.getMarkersList().toString());
                            //maps.removeMarkerFromList(marker.getPosition().latitude, marker.getPosition().longitude);
                            getMapsModel().removeMarker(marker);
                            //Log.d("After: ", maps.getMarkersList().toString());
                        }
                        return;
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                    }
                }
        );

        return;
    }

    //get Informations with a popup box from a click on POI marker:
    public void getPOIListener() {
        this.maps.getGoogleMap().setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker != null) {
                            //Log.d("Before: ", maps.getMarkersList().toString());
                            //maps.removeMarkerFromList(marker.getPosition().latitude, marker.getPosition().longitude);
                            //marker.remove();
                            //Log.d("After: ", maps.getMarkersList().toString());
                            marker.showInfoWindow();
                            return true;
                        }
                        return false;
                    }
                }
        );
        //Info panel:
        //googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

        return;
    }

    //TODO: getFriendsList:
    public List<Marker> getPOIList() {
        return this.maps.getMarkersList();
    }

    public boolean updateMapMarkers(List<Marker> poiMarkers) {
        if(poiMarkers != null) {
            for(Marker poiMarker : poiMarkers) {
                //Not .addMarker because we do not need to update list with new marker:
                this.getMapsModel()
                        .addMapMarker(
                                new MarkerModel(
                                        poiMarker.getTitle(),
                                        poiMarker.getPosition().latitude,
                                        poiMarker.getPosition().longitude
                                )
                        );
            }
            return true;
        }
        return false;
    }

    public void moveAnimatedCamera(double latitude, double longitude, float zoom) {
        this.maps.moveAnimatedCamera(new CameraModel(latitude, longitude, zoom));
    }

    public void moveCamera(double latitude, double longitude, float zoom) {
        this.maps.moveCamera(new CameraModel(latitude, longitude, zoom));
    }

    public CameraModel getCamera() {
        return this.maps.getCamera();
    }

    public MapsModel getMapsModel() {
        return maps;
    }

    public MapsModel getMaps() {
        return maps;
    }

    public void setMaps(MapsModel maps) {
        this.maps = maps;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
