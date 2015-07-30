package dk.aau.mppss.friendfinder.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import dk.aau.mppss.friendfinder.MainActivity;
import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.controller.maps.MapsController;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapsFragment extends Fragment {
    private MapView mapView;
    private MapsController mapsController;
    private List<Marker> poiList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //setRetainInstance allows to save class property before android system event (like orientation...).
        //But it only backup properties that are instance of Parcelable (Marker cannot be saved, so let's use Bundle instead).
        //See http://stackoverflow.com/questions/26223770/setretaininstance-with-fragment:
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        //Log.e("AYOUB", "onCreate ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //false in order to allow android system to attach fragment view inside the
        //parent view automatically in function of resolution etc...:
        View parentView = inflater.inflate(R.layout.fragment_maps, container, false);

        this.mapView = (MapView) parentView.findViewById(R.id.fragment_maps_mapView);
        this.mapView.onCreate(savedInstanceState);
        /*
        //From Android Developer Platform: Initializes the Google MapsModel Android API so that its classes are ready for use.
        //If you are using MapFragment or MapView and have already obtained a (non-null) GoogleMap by calling getMap()
        //on either of these classes, then it is not necessary to call this.
        //So we don't this initializer since we have no
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        //Controller Initialization:
        if(this.mapsController == null) {
            this.mapsController = new MapsController(this.mapView, inflater);
        }
        //Each onCreateView updates MapView so we must update
        //our MapView for Model-Controler-View interactions):
        this.mapsController.updateMapView(mapView);
        //same for inflater:
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null) {
            this.mapsController.enableWindowAdapter(mainActivity, inflater);
        }
        //Log.e("AYOUB", "onCreateView ");
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(this.mapsController != null) {
            this.mapsController.initializeMaps();
            //Restore Camera state from our saved bundle:
            if(savedInstanceState != null) {
                double latitude = savedInstanceState.getDouble("latitude");
                double longitude = savedInstanceState.getDouble("longitude");
                float zoom = savedInstanceState.getFloat("zoom");

                this.mapsController.moveCamera(latitude, longitude, zoom);
            } else {
                //We set a defaut camera position
                this.mapsController.moveAnimatedCamera(58.0, 9.0, 4);
            }
            //Redraw Markers if there is an orientation modification or something else:
            if(this.poiList != null) {
                this.mapsController.updateMapMarkers(this.poiList);
            }
        }
        //Log.e("AYOUB", "onActivityCreated "+ this.poiList);
    }

    @Override
    public void onResume() {
        //onResume allows interaction with user (a sort of main for a fragment but it's not a while loop!).
        //onCreate -> onStart -> Activity becomes visible -> onResume -> Interaction with user -> Activity is running...
        //Since we do not do a while loop, onResume will be executed each time an activity or fragment here change its states.
        //From Android developer:
        //onResume isn't limited to being called after the activity has been paused, it's called whenever the activity goes to the
        //top of the activity stack. That includes the first time it's shown after it's been created.
        super.onResume();
        this.mapView.onResume();

        if(this.mapsController != null) {
            this.mapsController.addPOIListener();
            this.mapsController.removePOIListener();
            //We update POI list in order to backup our Marker View via class variable and setRetainInstance:
            //We affect for this getPOIList reference value to poiList variable:
            this.poiList = this.mapsController.getPOIList();
        }
        //Log.e("AYOUB", "onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mapView.onPause();
        //Log.e("AYOUB", "onPause ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
        //Log.e("AYOUB", "onDestroy ");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.mapView.onLowMemory();
        //Log.e("AYOUB", "onLowMemory ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save states for camera:
        outState.putDouble("latitude", this.mapsController.getCamera().getLatitude());
        outState.putDouble("longitude", this.mapsController.getCamera().getLongitude());
        outState.putFloat("zoom", this.mapsController.getCamera().getZoom());
    }
}