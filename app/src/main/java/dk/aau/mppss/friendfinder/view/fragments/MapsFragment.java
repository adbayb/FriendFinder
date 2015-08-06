package dk.aau.mppss.friendfinder.view.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.HashMap;
import java.util.List;

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.UtilityClass;
import dk.aau.mppss.friendfinder.controller.HttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.OnHttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.maps.MapsController;
import dk.aau.mppss.friendfinder.controller.maps.MapsNotificationController;
import dk.aau.mppss.friendfinder.controller.maps.http.MapsFBHttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.maps.http.MapsPOIHttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.maps.location.MapsLocationListener;
import dk.aau.mppss.friendfinder.controller.maps.location.OnMapsLocationListener;
import dk.aau.mppss.friendfinder.model.maps.MapsModel;
import dk.aau.mppss.friendfinder.model.maps.marker.FBMarkerModel;
import dk.aau.mppss.friendfinder.model.maps.marker.MarkerModel;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapsFragment extends Fragment implements OnHttpAsyncTask, OnMapsLocationListener {
    private MapsController mapsController;
    private List<? extends MarkerModel> sqlMarkers;
    private FBMarkerModel userMarker;
    private Button updateButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //false in order to allow android system to attach fragment view inside the
        //parent view automatically in function of resolution etc...:
        View parentView = inflater.inflate(R.layout.fragment_maps, container, false);

        this.updateButton = (Button) parentView.findViewById(R.id.fragment_maps_update_button);
        GoogleMap googleMap = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_maps_map_view))
                .getMap();
        //Controller with View Initialization:
        if(this.mapsController == null) {
            this.mapsController = new MapsController(this.getChildFragmentManager(), inflater, googleMap);
        }
        //Log.e("AYOUB", "onCreateView ");
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //onActivityCreated called after onCreateView in Fragment Lifecycle:
        //Log.e("AYOUB", "onActivityCreated ");
        //Controller Initialization:
        if(this.mapsController != null) {
            this.mapsController.initializeMaps();
            //We set a defaut camera position with animation:
            this.mapsController.moveAnimatedCamera(58.0, 9.0, 4);
            //Setup location Listener:
            this.mapsController.getMapsModel()
                    .getGoogleMap()
                    .setOnMyLocationChangeListener(new MapsLocationListener(getActivity(), this));
            //Setup listeners that it will be used during all cycle (if there will be used only in onResume just call them in onResume):
            this.onUpdateButtonListener();
            this.mapsController.enableWindowAdapter();
            this.mapsController.addPOIListener();
            //Get view marker contents from DB and add it to current map:
            this.updateMapView();
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

    public boolean onUpdateButtonListener() {
        if(this.updateButton != null) {
            this.updateButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateMapView();
                        }
                    }
            );
            return true;
        }
        return false;
    }

    public boolean updateMapView() {
        if(this.mapsController != null) {

            return this.updateMapFromHttpRequests();
        }
        return false;
    }

    public boolean updateMapFromHttpRequests() {
        if(this.mapsController != null) {
            //Reset Map Model:
            MapsModel mapsModel = this.mapsController.getMaps();
            if(mapsModel != null) {
                GoogleMap googleMap = mapsModel.getGoogleMap();
                mapsModel.setMarkersList(new HashMap<String, MarkerModel>());
                if(googleMap != null) {
                    googleMap.clear();
                }
            }
            //Reinitialize Map:
            HttpAsyncTask poiUserHttpAsyncTask = new HttpAsyncTask(
                    new MapsPOIHttpAsyncTask(this.mapsController, R.drawable.user_poi, true),
                    UtilityClass.urlGetAllUserPOI,
                    new HashMap<String, Object>() {{
                        put("idFacebook", UtilityClass.getUserID());
                    }}
            );
            poiUserHttpAsyncTask.execute();

            HttpAsyncTask poiFriendsHttpAsyncTask = new HttpAsyncTask(
                    new MapsPOIHttpAsyncTask(this.mapsController, R.drawable.friend_poi, false),
                    UtilityClass.urlGetAllFriendsPOI,
                    new HashMap<String, Object>() {{
                        put("idFacebook", UtilityClass.getUserID());
                    }}
            );
            poiFriendsHttpAsyncTask.execute();

            HttpAsyncTask fbHttpAsyncTask = new HttpAsyncTask(
                    new MapsFBHttpAsyncTask(this.mapsController, R.drawable.friend),
                    UtilityClass.urlGetFriendsFB,
                    new HashMap<String, Object>() {{
                        put("idUser", UtilityClass.getUserID());
                    }}
            );
            fbHttpAsyncTask.execute();

            if(this.userMarker != null) {
                this.userMarker = this.mapsController.setUserMarker(
                        new FBMarkerModel(
                                UtilityClass.getUserName(), null, this.userMarker.getLatitude(), this.userMarker
                                .getLongitude(), null
                        ),
                        R.drawable.user
                );
            }

            return true;
        }

        return false;
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

    private boolean updateUserLocation(Location location) {
        if(location != null) {
            final Location currentLoc = location;
            //Attention: for HashMap we need to send objects so send a value like that
            //currentLoc.getLatitude() will not work since it's a litteral value!:
            HttpAsyncTask coordUserHttpAsyncTask = new HttpAsyncTask(
                    MapsFragment.this,
                    UtilityClass.urlUpdateUser,
                    new HashMap<String, Object>() {{
                        put("idFacebook", UtilityClass.getUserID());
                        put("lastKnownLocationLat", new Double(currentLoc.getLatitude()).toString());
                        put("lastKnownLocationLong", new Double(currentLoc.getLongitude()).toString());
                    }}
            );
            coordUserHttpAsyncTask.execute();

            //Update User Marker Location:
            if(this.userMarker == null) {
                //We set a default position for current user since it will be updated by location listener:
                this.userMarker = this.mapsController.setUserMarker(
                        new FBMarkerModel(
                                UtilityClass.getUserName(), null, location.getLatitude(), location.getLongitude(), null
                        ),
                        R.drawable.user
                );

                return true;
            } else {
                if(this.mapsController.updateFBMarker(
                        this.userMarker, location.getLatitude(), location.getLongitude()
                ) != null) {

                    return true;
                }
            }

            return false;
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
    public void onGetLocation(Location location) {
        //Set User Marker:
        if(location != null) {

            this.updateUserLocation(location);
        }

        return;
    }

    @Override
    public void onUpdatedLocation(Location location) {
        if(location != null) {
            //User map update + DB update:
            this.updateUserLocation(location);
            //Notification builder:
            new MapsNotificationController(this.getActivity()).newNotification(
                    0,
                    R.drawable.user,
                    "FriendFinder Notification: Updated Location",
                    "New Location more than 3 meters from previous one"
            );
            //Log.e("Ayoub", "updated Localisation"+location.getLatitude()+"-"+location.getLongitude());
        }
    }

    @Override
    public void onHttpAsyncTaskCompleted(String result) {
        return;
    }
}