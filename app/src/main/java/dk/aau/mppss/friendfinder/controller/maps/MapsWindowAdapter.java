package dk.aau.mppss.friendfinder.controller.maps;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.view.Gui;
import dk.aau.mppss.friendfinder.view.fragments.EditMarkerFragment;

/**
 * Created by adibayoub on 29/07/2015.
 */

//Class that extends
public class MapsWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
    //Inflater permettant de rattacher notre fênetre Popup à la vue:
    private LayoutInflater inflater;
    private boolean isShortOverview;
    private FragmentManager mapsChildFragmentManager;

    //Add MarkerModel to get information on marker (MarkerPOIModel and MarkerUserModel must extends MarkerModel!):
    public MapsWindowAdapter(FragmentManager mapsChildFragmentManager, LayoutInflater inflater, boolean isShortOverview) {
        if(this.mapsChildFragmentManager == null)
            this.mapsChildFragmentManager = mapsChildFragmentManager;
        this.inflater = inflater;
        this.isShortOverview = isShortOverview;
    }

    //getInfoWindow permet d'afficher une window_adapter seulement
    //avec l'information sans mise en forme de la window_adapter:
    @Override
    public View getInfoWindow(Marker marker) {
        //Pas d'implémentation, nous choisissons getInfoContents:
        return null;
    }

    //avec mise en forme de la window_adapter, choix de l'implémentation ici:
    @Override
    public View getInfoContents(Marker marker) {
        View windowView = this.inflater.inflate(R.layout.window_adapter, null);

        if(this.isShortOverview == true) {
            this.setShortOverview(windowView, marker);
        } else {
            //TODO for Long card overview?
        }

        return windowView;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(this.mapsChildFragmentManager != null) {
            Gui.replaceFragment(
                    mapsChildFragmentManager,
                    R.id.fragment_container,
                    EditMarkerFragment.EditMarkerFragmentInstance(
                            "test POI"
                    )
            );
        }
        //Log.e("AYOUB onInfoWinClick",""+this.mapsFragment);
        return;
    }

    private void setShortOverview(View view, Marker marker) {
        TextView title = (TextView) view.findViewById(R.id.window_adapter_title);
        TextView description = (TextView) view.findViewById(R.id.window_adapter_description);
        ImageView icon = (ImageView) view.findViewById(R.id.window_adapter_icon);

        title.setText(marker.getTitle());
        description.setText("Description test " + marker.getId());
        //TODO gestion icon:
        //icon.setImageDrawable();
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public boolean isShortOverview() {
        return isShortOverview;
    }

    public void setIsShortOverview(boolean isShortOverview) {
        this.isShortOverview = isShortOverview;
    }

    public FragmentManager getMapsChildFragmentManager() {
        return mapsChildFragmentManager;
    }

    public void setMapsChildFragmentManager(FragmentManager mapsChildFragmentManager) {
        this.mapsChildFragmentManager = mapsChildFragmentManager;
    }
}
