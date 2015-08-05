package dk.aau.mppss.friendfinder.view.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.controller.HttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.maps.MapsController;
import dk.aau.mppss.friendfinder.controller.maps.MapsFBHttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.maps.MapsLocationListener;
import dk.aau.mppss.friendfinder.controller.maps.MapsNotificationController;
import dk.aau.mppss.friendfinder.controller.maps.MapsPOIHttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.maps.OnMapsLocationListener;
import dk.aau.mppss.friendfinder.model.maps.FBMarkerModel;
import dk.aau.mppss.friendfinder.model.maps.MarkerModel;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapsFragment extends Fragment implements OnMapsLocationListener {
    private MapsController mapsController;
    private List<? extends MarkerModel> sqlMarkers;
    private FBMarkerModel userMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //false in order to allow android system to attach fragment view inside the
        //parent view automatically in function of resolution etc...:
        View parentView = inflater.inflate(R.layout.fragment_maps, container, false);

        GoogleMap googleMap = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_maps_map_view))
                .getMap();
        //Controller Initialization:
        if(this.mapsController == null) {
            this.mapsController = new MapsController(this.getChildFragmentManager(), inflater, googleMap);
            //We set a defaut camera position with animation:
            this.mapsController.moveAnimatedCamera(58.0, 9.0, 4);

            this.mapsController.getMapsModel()
                    .getGoogleMap()
                    .setOnMyLocationChangeListener(new MapsLocationListener(getActivity(), this));
        }
        //Log.e("AYOUB", "onCreateView ");
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(this.mapsController != null) {
            this.mapsController.initializeMaps();

            HttpAsyncTask poiHttpAsyncTask = new HttpAsyncTask(
                    new MapsPOIHttpAsyncTask(this.mapsController),
                    "http://friendfinder.alwaysdata.net/FriendFinder/get_all_poi.php"
            );
            poiHttpAsyncTask.execute();

            //TODO Sekou modifier URL php WS:
            HttpAsyncTask fbHttpAsyncTask = new HttpAsyncTask(
                    new MapsFBHttpAsyncTask(this.mapsController),
                    "http://friendfinder.alwaysdata.net/FriendFinder/get_all_poi.php"
            );
            fbHttpAsyncTask.execute();

            //TODO retrieve informations from DB see with Sekou:
            this.userMarker = this.mapsController.setUserMarker(new FBMarkerModel("AYOUBBBB", "MyMarker", 1.2, 3.4, null));
            //Log.e("AYOUB", "onActivityCreated ");
        }
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

        if(this.mapsController != null) {
            this.mapsController.enableWindowAdapter();
            this.mapsController.addPOIListener();
            //this.mapsController.removePOIListener();
        }
        //Log.e("AYOUB", "onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mapsController.stopAddPOIListener();
        this.mapsController.disableWindowAdapter();
        //Log.e("AYOUB", "onPause ");
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.e("AYOUB", "onStop ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.e("AYOUB", "onDestroy ");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //Log.e("AYOUB", "onLowMemory ");
    }

    public EditMarkerFragment isVisibleEditMarkerFragment() {
        FragmentManager childFragmentManager = this.getChildFragmentManager();
        if(childFragmentManager != null) {
            for(Fragment fragment : childFragmentManager.getFragments()) {
                if(fragment instanceof EditMarkerFragment) {
                    return (EditMarkerFragment) fragment;
                }
            }
        }
        //return ((EditMarkerFragment)this.getChildFragmentManager().getFragments()).isVisible();
        return null;
    }

    public boolean popEditMarkerFragment() {
        EditMarkerFragment editMarkerFragment = this.isVisibleEditMarkerFragment();
        if(editMarkerFragment != null) {
            //since editMarkerFragment will be always at the top of fragment stack child of MapsFragment,
            //we can simply popBackStack (ie last child fragment) by calling hideEdit function:
            editMarkerFragment.hideEditor(this);
            return true;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public MapsController getMapsController() {
        return mapsController;
    }

    public void setMapsController(MapsController mapsController) {
        this.mapsController = mapsController;
    }

    @Override
    public void getInitialLocation(Location location) {
        //Log.e("AAAAAAA", "initial Localisation" + location.getLatitude() + "-" + location.getLongitude());
        //Set User Marker:
        if(this.userMarker != null) {
            FBMarkerModel test = (FBMarkerModel) this.mapsController.getMapsModel()
                    .getMarkersList()
                    .get(this.userMarker.getLatitude() + "-" + this.userMarker.getLongitude());
            Log.e(
                    "DEBUGGG", "" + test + "BEFORE" + this.mapsController.getMapsModel()
                            .getMarkersList()
                            .toString()
            );
            if(test != null)
                this.mapsController.getMapsModel().getMarkersList().remove(test);
            this.userMarker.setLatitude(location.getLatitude());
            this.userMarker.setLongitude(location.getLongitude());
            this.userMarker.updateMarker(location.getLatitude(), location.getLongitude());
            Log.e(
                    "DEBUGGG", "" + this.userMarker.getLatitude() + "-" + this.userMarker.getLongitude() + "AFTER" + this.mapsController
                            .getMapsModel()
                            .getMarkersList()
                            .toString()
            );
            this.mapsController.getMapsModel()
                    .getMarkersList()
                    .put(this.userMarker.getLatitude() + "-" + this.userMarker.getLongitude(), this.userMarker);
        }
    }

    @Override
    public void onUpdatedLocation(Location location) {
        new MapsNotificationController(this.getActivity()).newNotification(
                0,
                R.drawable.user,
                "FriendFinder Notification: Updated Location",
                "New Location more than 3 meters from previous one"
        );
        //Log.e("AAAAAAA2", "updated Localisation"+location.getLatitude()+"-"+location.getLongitude());
        //Update User Marker Location:
        if(this.userMarker != null) {
            this.userMarker.setLatitude(location.getLatitude());
            this.userMarker.setLongitude(location.getLongitude());
            this.userMarker.updateMarker(location.getLatitude(), location.getLongitude());
        }
    }
}