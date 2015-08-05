package dk.aau.mppss.friendfinder.controller.maps;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;


public class MapsLocationListener implements GoogleMap.OnMyLocationChangeListener {
    private Context context;
    private Location lastLocation;
    private Location currLocation;
    private OnMapsLocationListener onMapsLocationListener;

    public MapsLocationListener(Context aContext, OnMapsLocationListener onMapsLocationListener) {
        if(aContext != null)
            context = aContext;
        if(onMapsLocationListener != null)
            this.onMapsLocationListener = onMapsLocationListener;
    }

    @Override
    public void onMyLocationChange(Location location) {
        //only register as changed location if user location has changed more than 50 meters
        if(lastLocation == null) {
            this.onMapsLocationListener.getInitialLocation(location);
            lastLocation = location;
            /*Toast.makeText(
                    context, "INITIAL LOCATION SET TO LAST LOCATION:" + location.getLatitude() +
                            ": " + location.getLongitude() + ". ACCURACY: " + location.getAccuracy(), Toast.LENGTH_LONG
            ).show();*/
        } else {
            //We set precision to 3 meters even if it's not real:
            if(lastLocation.distanceTo(location) > 3) {
                lastLocation = currLocation;
                currLocation = location;
                this.onMapsLocationListener.onUpdatedLocation(location);

            /*Toast.makeText(
                    context, "NEW LOCATION REGISTERED: " + location.getLatitude() + " - " + location
                    .getLongitude(), Toast.LENGTH_LONG
            ).show();*/
            } else {
                //do nothing - not enough change in location to register!!!
            /*String dist = String.valueOf(lastLocation.distanceTo(location));
            Toast.makeText(
                    context, "DISTANCE" + dist + ". REGISTERED, BUT NOT NEW: " + location.getLatitude() + " - " + location
                    .getLongitude(), Toast.LENGTH_LONG
            ).show();*/
            }
        }
    }
}