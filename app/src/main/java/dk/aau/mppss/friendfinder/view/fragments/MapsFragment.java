package dk.aau.mppss.friendfinder.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import dk.aau.mppss.friendfinder.MapsActivity;
import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.controller.maps.MapsController;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapsFragment extends Fragment {
    private MapsController mapsController;

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
        }
        //same for inflater:
        MapsActivity mapsActivity = (MapsActivity) getActivity();
        if(mapsActivity != null) {
            this.mapsController.enableWindowAdapter(inflater);
        }
        //Log.e("AYOUB", "onCreateView ");
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(this.mapsController != null) {
            this.mapsController.initializeMaps();
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

        if(this.mapsController != null) {
            this.mapsController.addPOIListener();
            this.mapsController.removePOIListener();
        }
        //Log.e("AYOUB", "onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.e("AYOUB", "onPause ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("AYOUB", "onStop ");
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
}