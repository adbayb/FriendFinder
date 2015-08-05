package dk.aau.mppss.friendfinder.controller.maps;

import android.location.Location;

/**
 * Created by adibayoub on 05/08/2015.
 */
public interface OnMapsLocationListener {
    public void getInitialLocation(Location location);

    public void onUpdatedLocation(Location location);
}
