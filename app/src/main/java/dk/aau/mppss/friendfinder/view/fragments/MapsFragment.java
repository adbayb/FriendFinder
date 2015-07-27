package dk.aau.mppss.friendfinder.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.aau.mppss.friendfinder.R;

/**
 * Created by adibayoub on 27/07/2015.
 */
public class MapsFragment extends Fragment {
    //private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //false to let android attach automatically fragment to the root view (i.e. parent view):
        View parentView = inflater.inflate(R.layout.fragment_maps, container, false);

        /*
        mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        Marker test = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        */

        return parentView;
    }
}