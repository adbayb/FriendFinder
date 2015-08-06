package dk.aau.mppss.friendfinder.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.UtilityClass;
import dk.aau.mppss.friendfinder.controller.HttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.OnHttpAsyncTask;
import dk.aau.mppss.friendfinder.model.maps.marker.POIMarkerModel;
import dk.aau.mppss.friendfinder.view.Gui;

/**
 * Created by adibayoub on 30/07/2015.
 */
public class EditMarkerFragment extends Fragment implements OnHttpAsyncTask {
    private static POIMarkerModel poiMarkerModel;
    private Button saveButton;
    private Button deleteButton;
    //Fragment attributes to edit:
    private LatLng latLng;
    private TextView nameTextView;
    private TextView descriptionTextView;

    //We cannot instantiate from a fragment with public EditMarkerFragment() and parameters,
    //we must save parameters on bundle on "custom" constructor:
    public static EditMarkerFragment Update(POIMarkerModel _poiMarkerModel) {
        EditMarkerFragment editMarkerFragment = new EditMarkerFragment();
        //Ne pas vérifier si _poiMarkerModel est null avant de l'affecter car poiMarkerModel est une
        //valeur statique et on doit l'overwritter à chaque appel de notre appel statique à Create:
        poiMarkerModel = _poiMarkerModel;
        if(poiMarkerModel != null) {
            Marker marker = poiMarkerModel.getMarker();
            if(marker != null) {

                Bundle bundle = new Bundle();
                bundle.putDouble("poi_latitude", marker.getPosition().latitude);
                bundle.putDouble("poi_longitude", marker.getPosition().longitude);
                editMarkerFragment.setArguments(bundle);
            }
        }

        return editMarkerFragment;
    }

    public static EditMarkerFragment Create(LatLng latLng) {
        EditMarkerFragment editMarkerFragment = new EditMarkerFragment();
        poiMarkerModel = null;
        Bundle args = new Bundle();
        if(latLng != null) {
            args.putDouble("poi_latitude", latLng.latitude);
            args.putDouble("poi_longitude", latLng.longitude);
        }
        editMarkerFragment.setArguments(args);

        return editMarkerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //we get constructor custom parameters:
        Bundle bundle = getArguments();
        if(bundle != null)
            this.latLng = new LatLng(
                    bundle.getDouble("poi_latitude", 0.0),
                    bundle.getDouble("poi_longitude", 0.0)
            );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //On met en pause le fragment Maps de toute possibilité d'ajout d'un marker afin d'éviter toute modification
        //durant l'édition de POI. Par contre la map peut être parcourue en mode lecture:
        MapsFragment parentFragment = (MapsFragment) getParentFragment();
        parentFragment.onPause();
        //false to let android attach automatically fragment to the root view (i.e. parent view):
        View parentView = inflater.inflate(R.layout.fragment_edit_marker, container, false);

        //Set up layout components:
        this.saveButton = (Button) parentView.findViewById(R.id.fragment_edit_marker_save_button);
        this.deleteButton = (Button) parentView.findViewById(R.id.fragment_edit_marker_delete_button);
        this.nameTextView = (TextView) parentView.findViewById(R.id.fragment_edit_marker_name);
        this.descriptionTextView = (TextView) parentView.findViewById(R.id.fragment_edit_marker_description);

        if(poiMarkerModel != null) {
            nameTextView.setText(poiMarkerModel.getLabel());
            descriptionTextView.setText(poiMarkerModel.getDescription());
        }

        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.onSaveListener(this.saveButton);
        this.onDeleteListener(this.deleteButton);
    }

    public void onDeleteListener(Button deleteButton) {
        if(this.deleteButton != null) {
            this.deleteButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Log.e("EditMarker AYOUB", "CLICK DELETE");
                            if(deletePOIMarker() != null) {
                                MapsFragment parentFragment = (MapsFragment) getParentFragment();
                                hideEditor(parentFragment);
                            }
                        }
                    }
            );
        }
    }

    public void onSaveListener(Button saveButton) {
        if(this.saveButton != null) {
            this.saveButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            POIMarkerModel poiMarker;
                            //si null, on est en mode création d'un poi sinon update:
                            if(poiMarkerModel == null)
                                poiMarker = createPOIMarker();
                            else {
                                poiMarker = updatePOIMarker();
                            }

                            //We get parent fragment since EditMarkerFragment will be
                            //added inside a fragment and not like a root fragment. For example inside
                            //MapsFragment via getChildFragmentManager
                            MapsFragment parentFragment = (MapsFragment) getParentFragment();
                            if(parentFragment != null) {
                                parentFragment.getMapsController()
                                        .addPOIMarker(poiMarker, R.drawable.user_poi);
                                //Nous enlevons l'éditeur et on remet le listener d'ajout POI en place:
                                hideEditor(parentFragment);
                            }

                        }
                    }
            );
        }
    }

    public void hideEditor(MapsFragment mapsFragment) {
        //parentFragment.onStop();
        //We remove editor poi fragment from stack:
        Gui.popFragment(
                getParentFragment().getChildFragmentManager()
        );
        //We stop button listener:
        this.saveButton.setOnClickListener(null);
        this.deleteButton.setOnClickListener(null);
        //we restore google maps listener with onExitPause:
        mapsFragment.onExitPause();
        mapsFragment.onResume();

        return;
    }

    private void onHttpDeletePOI() {
        try {
            HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
                    EditMarkerFragment.this,
                    UtilityClass.urlDeletePOI,
                    new HashMap<String, Object>() {{
                        put("userId", UtilityClass.getUserID());
                        put("latitude", new Double(latLng.latitude).toString());
                        put("longitude", new Double(latLng.longitude).toString());
                    }}
            );
            httpAsyncTask.execute();
        }
        catch(Exception e) {
            //Log.e("Fail", e.toString());
        }

        return;
    }

    private void onHttpCreatePOI() {
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
                EditMarkerFragment.this,
                UtilityClass.urlCreatePOI,
                new HashMap<String, Object>() {{
                    put("userId", UtilityClass.getUserID());
                    put("title", nameTextView.getText().toString());
                    put("description", descriptionTextView.getText().toString());
                    put("latitude", new Double(latLng.latitude).toString());
                    put("longitude", new Double(latLng.longitude).toString());
                }}
        );
        httpAsyncTask.execute();

        return;
    }

    private void onHttpUpdatePOI() {
        try {
            HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
                    EditMarkerFragment.this,
                    UtilityClass.urlUpdatePOI,
                    new HashMap<String, Object>() {{
                        put("userId", UtilityClass.getUserID());
                        put("title", nameTextView.getText().toString());
                        put("description", descriptionTextView.getText().toString());
                        put("latitude", new Double(latLng.latitude).toString());
                        put("longitude", new Double(latLng.longitude).toString());
                    }}
            );
            httpAsyncTask.execute();
        }
        catch(Exception e) {
            //Log.e("Fail", e.toString());
        }

        return;
    }

    private POIMarkerModel createPOIMarker() {
        this.onHttpCreatePOI();

        poiMarkerModel = new POIMarkerModel(
                nameTextView.getText().toString(),
                descriptionTextView.getText().toString(),
                latLng.latitude,
                latLng.longitude,
                null,
                true
        );

        return poiMarkerModel;
    }

    private POIMarkerModel updatePOIMarker() {
        if(poiMarkerModel != null) {
            this.onHttpUpdatePOI();

            poiMarkerModel.setLabel(nameTextView.getText().toString());
            poiMarkerModel.setDescription(
                    descriptionTextView.getText()
                            .toString()
            );
            //We remove the old marker:
            if(poiMarkerModel.getMarker() != null)
                poiMarkerModel.getMarker().remove();

            return poiMarkerModel;
        }
        return null;
    }

    private POIMarkerModel deletePOIMarker() {
        if(poiMarkerModel != null) {
            this.onHttpDeletePOI();

            //We remove the old marker:
            if(poiMarkerModel != null)
                poiMarkerModel.getMarker().remove();

            return poiMarkerModel;
        }
        return null;
    }

    @Override
    public void onHttpAsyncTaskCompleted(String result) {
        return;
    }
}

