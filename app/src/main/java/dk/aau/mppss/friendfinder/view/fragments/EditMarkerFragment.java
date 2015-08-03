package dk.aau.mppss.friendfinder.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.StaticValue;
import dk.aau.mppss.friendfinder.controller.HttpAsyncTask;
import dk.aau.mppss.friendfinder.model.maps.MarkerModel;
import dk.aau.mppss.friendfinder.view.Gui;

/**
 * Created by adibayoub on 30/07/2015.
 */
public class EditMarkerFragment extends Fragment {
    private Button buttonSave;
    //Fragment attributes to edit:
    private String name;
    private String description;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private LatLng latLng;

    //We cannot instantiate from a fragment with public EditMarkerFragment() and parameters,
    //we must save parameters on bundle on "custom" constructor:
    public static EditMarkerFragment EditMarkerFragmentInstance(String name, String description, LatLng latLng) {
        EditMarkerFragment editMarkerFragment = new EditMarkerFragment();

        Bundle args = new Bundle();
        if(name != null)
            args.putString("name", name);
        if(latLng != null) {
            args.putDouble("latlng_latitude", latLng.latitude);
            args.putDouble("latlng_longitude", latLng.longitude);
        }
        editMarkerFragment.setArguments(args);

        return editMarkerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //we get constructor custom parameters:
        Bundle bundle = getArguments();
        if(bundle != null) {
            this.name = getArguments().getString("name", "");
            this.description = getArguments().getString("description", "");
        }
        this.latLng = new LatLng(
                getArguments().getDouble("latlng_latitude", 0.0),
                getArguments().getDouble("latlng_longitude", 0.0)
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //false to let android attach automatically fragment to the root view (i.e. parent view):
        View parentView = inflater.inflate(R.layout.fragment_edit_marker, container, false);

        this.buttonSave = (Button) parentView.findViewById(R.id.fragment_edit_marker_button);
        this.nameTextView = (TextView) parentView.findViewById(R.id.fragment_edit_marker_name);
        this.descriptionTextView = (TextView) parentView.findViewById(R.id.fragment_edit_marker_description);

        if(this.name != null)
            nameTextView.setText(this.name);

        if(this.description != null)
            descriptionTextView.setText(this.description);


        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.buttonSave != null) {
            this.buttonSave.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /*Map<String, Object> test = new HashMap<String, Object>();
                            test.put("title", nameTextView.getText().toString());
                            test.put("description", descriptionTextView.getText().toString());
                            test.put("latitude", new Double(latLng.latitude).toString());
                            test.put("longitude", new Double(latLng.longitude).toString());*/

                            try {
                                HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
                                        StaticValue.urlCreatePOI,
                                        new HashMap<String, Object>() {{
                                            put("title", nameTextView.getText().toString());
                                            put(
                                                    "description", descriptionTextView.getText()
                                                            .toString()
                                            );
                                            put("latitude", new Double(latLng.latitude).toString());
                                            put("longitude", new Double(latLng.longitude).toString());
                                        }}
                                );
                                httpAsyncTask.execute();
                                //Log.e("Nydiaaaaaaaaa OK 1", "OKKKKKKKKKKKK");
                            }
                            catch(Exception e) {
                                //Log.e("Nydiaaaaaaaaa Fail 1", e.toString());
                            }

                            //We get parent fragment since EditMarkerFragment will be
                            //added inside a fragment and not like a root fragment. For example inside
                            //MapsFragment via getChildFragmentManager
                            MapsFragment parentFragment = (MapsFragment) getParentFragment();
                            if(parentFragment != null) {
                                parentFragment.onStop();
                                Gui.popFragment(
                                        getParentFragment().getChildFragmentManager()
                                );
                            }

                            parentFragment.getMapsController()
                                    .getMapsModel()
                                    .addMarker(
                                            new MarkerModel(
                                                    nameTextView.getText()
                                                            .toString(), latLng.latitude, latLng.longitude
                                            )
                                    );
                        }
                    }
            );
        }
    }
}

