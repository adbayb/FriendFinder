package dk.aau.mppss.friendfinder.controller.maps;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.controller.maps.adapter.MapsWindowAdapter;
import dk.aau.mppss.friendfinder.model.maps.CameraModel;
import dk.aau.mppss.friendfinder.model.maps.MapsModel;
import dk.aau.mppss.friendfinder.model.maps.marker.FBMarkerModel;
import dk.aau.mppss.friendfinder.model.maps.marker.MarkerModel;
import dk.aau.mppss.friendfinder.model.maps.marker.POIMarkerModel;
import dk.aau.mppss.friendfinder.view.Gui;
import dk.aau.mppss.friendfinder.view.fragments.EditMarkerFragment;

/**
 * Created by adibayoub on 28/07/2015.
 */
public class MapsController {
    private MapsModel maps;
    private FragmentManager mapsChildFragmentManager;
    private MapsWindowAdapter mapsWindowAdapter;

    public MapsController(FragmentManager mapsChildFragmentManager, LayoutInflater inflater, GoogleMap googleMap) {
        this.maps = new MapsModel(googleMap);
        if(this.mapsChildFragmentManager == null)
            this.mapsChildFragmentManager = mapsChildFragmentManager;

        //Set up WindowAdapter for our marker maps: (i.e. popup box when clicking on a marker):
        if(inflater != null) {
            this.mapsWindowAdapter = new MapsWindowAdapter(this, inflater, true);
            this.maps.getGoogleMap().setInfoWindowAdapter(mapsWindowAdapter);
        }
    }

    public void enableWindowAdapter() {
        this.maps.getGoogleMap().setOnInfoWindowClickListener(mapsWindowAdapter);
    }

    public void disableWindowAdapter() {
        this.maps.getGoogleMap().setOnInfoWindowClickListener(null);
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
                        if(mapsChildFragmentManager != null)
                            Gui.replaceFragment(
                                    mapsChildFragmentManager,
                                    R.id.fragment_container,
                                    EditMarkerFragment.Create(latLng)
                            );
                        //maps.addMarker(new MarkerModel("test POI", latLng.latitude, latLng.longitude));
                    }
                }
        );

        return;
    }

    //Nous stoppons l'ajout de POI avec Edit Fragment en overwrittant le listener afin d'effectuer aucune action:
    public void stopAddPOIListener() {
        this.maps.getGoogleMap().setOnMapClickListener(
                new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        return;
                    }
                }
        );

        return;
    }

    //No need to move a friend point! fixed coordinates in database:
    //@TODO implement it on a next version?
    public void movePOIListener() {

    }

    public POIMarkerModel addPOIMarker(POIMarkerModel poiMarkerModel, int idRessourceIcon) {
        POIMarkerModel markerModel = (POIMarkerModel) this.maps.addMarker(poiMarkerModel, idRessourceIcon);
        //Log.e("Avant", poiMarkerModel.toString());
        //Log.e("Après", "" + this.maps.findMarkerModelFromMarker(mark).toString());

        return markerModel;
    }

    public POIMarkerModel updatePOIMarker(POIMarkerModel poiMarkerModel, double latitude, double longitude) {
        if(poiMarkerModel != null) {
            POIMarkerModel oldPOIMarkerModel = (POIMarkerModel) this.getMapsModel()
                    .findMarkerModelFromPosition(new LatLng(latitude, longitude));

            //Remove old MarkerModel from Map List:
            if(oldPOIMarkerModel != null)
                this.getMapsModel().getMarkersList().remove(oldPOIMarkerModel);

            //Update Marker Model + marker position attached to it:
            poiMarkerModel.setLatitude(latitude);
            poiMarkerModel.setLongitude(longitude);
            poiMarkerModel.updateMarker(latitude, longitude);

            //Add the new updated MarkerModel inside Map List:
            return (POIMarkerModel) this.getMapsModel()
                    .getMarkersList()
                    .put(latitude + "-" + longitude, (POIMarkerModel) poiMarkerModel);
        }
        return null;
    }

    public FBMarkerModel addFBMarker(FBMarkerModel fbMarkerModel, int idRessourceIcon) {
        FBMarkerModel markerModel = (FBMarkerModel) this.maps.addMarker(fbMarkerModel, idRessourceIcon);
        //Log.e("Avant", poiMarkerModel.toString());
        //Log.e("Après", "" + this.maps.findMarkerModelFromMarker(mark).toString());

        return markerModel;
    }

    public FBMarkerModel updateFBMarker(FBMarkerModel fbMarkerModel, double latitude, double longitude) {
        if(fbMarkerModel != null) {
            FBMarkerModel oldFBMarkerModel = (FBMarkerModel) this.getMapsModel()
                    .findMarkerModelFromPosition(new LatLng(latitude, longitude));

            //Remove old MarkerModel from Map List:
            if(oldFBMarkerModel != null)
                this.getMapsModel().getMarkersList().remove(oldFBMarkerModel);

            //Update Marker Model + marker position attached to it:
            fbMarkerModel.setLatitude(latitude);
            fbMarkerModel.setLongitude(longitude);
            fbMarkerModel.updateMarker(latitude, longitude);

            //Add the new updated MarkerModel inside Map List:
            return (FBMarkerModel) this.getMapsModel()
                    .getMarkersList()
                    .put(latitude + "-" + longitude, (FBMarkerModel) fbMarkerModel);
        }
        return null;
    }

    public FBMarkerModel setUserMarker(FBMarkerModel fbMarkerModel, int idRessourceIcon) {
        return this.addFBMarker(fbMarkerModel, idRessourceIcon);
    }

    /*
    //@ implémenter plus tard (facile car chaque MarkerModel stocke la référence vers le marqueur:
    //il suffit alors de récupérer la position du marker avant drag & drop et d'updater la position en BDD avec ces positions:
    //Attention, au préalable il faut setter le marker avec la propriété setDraggable à true (cf. addMapMarker dans MapsModel):
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
    */

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

    public POIMarkerModel findPOIMarkerModelFromList(Marker marker) {
        MarkerModel markerModel = this.maps.findMarkerModelFromMarker(marker);
        if(markerModel != null) {
            if(markerModel instanceof POIMarkerModel) {
                return (POIMarkerModel) markerModel;
            }
        }

        return null;
    }

    public MarkerModel findMarkerModelFromList(Marker marker) {
        return this.maps.findMarkerModelFromMarker(marker);
    }

    public List<POIMarkerModel> getPOIList() {
        List<POIMarkerModel> poiMarkerModels = new ArrayList<POIMarkerModel>();
        Map<String, MarkerModel> markersList = this.maps.getMarkersList();

        for(Map.Entry markerList : markersList.entrySet()) {
            MarkerModel markerModel = (MarkerModel) markerList.getValue();
            if(markerModel instanceof POIMarkerModel) {
                //Log.e("Key POI List", markerList.getKey().toString());
                poiMarkerModels.add((POIMarkerModel) markerModel);
            }
        }
        return poiMarkerModels;
    }

    public List<FBMarkerModel> getFBList() {
        List<FBMarkerModel> fbMarkerModels = new ArrayList<FBMarkerModel>();
        Map<String, MarkerModel> markersList = this.maps.getMarkersList();

        for(Map.Entry markerList : markersList.entrySet()) {
            MarkerModel markerModel = (MarkerModel) markerList.getValue();
            if(markerModel instanceof FBMarkerModel) {
                //Log.e("Key FB List", markerList.getKey().toString());
                fbMarkerModels.add((FBMarkerModel) markerModel);
            }
        }
        return fbMarkerModels;
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

    public FragmentManager getMapsChildFragmentManager() {
        return mapsChildFragmentManager;
    }

    public void setMapsChildFragmentManager(FragmentManager mapsChildFragmentManager) {
        this.mapsChildFragmentManager = mapsChildFragmentManager;
    }
}
