package dk.aau.mppss.friendfinder.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.view.Gui;

/**
 * Created by adibayoub on 30/07/2015.
 */
public class EditMarkerFragment extends Fragment {
    private Button buttonSave;
    //Fragment attributes to edit:
    private String name;

    //We cannot instantiate from a fragment with public EditMarkerFragment() and parameters,
    //we must save parameters on bundle on "custom" constructor:
    public static EditMarkerFragment EditMarkerFragmentInstance(String name) {
        EditMarkerFragment editMarkerFragment = new EditMarkerFragment();

        Bundle args = new Bundle();
        args.putString("name", name);
        editMarkerFragment.setArguments(args);

        return editMarkerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //we get constructor custom parameters:
        Bundle bundle = getArguments();
        if(bundle != null)
            this.name = getArguments().getString("name", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //false to let android attach automatically fragment to the root view (i.e. parent view):
        View parentView = inflater.inflate(R.layout.fragment_edit_marker, container, false);

        this.buttonSave = (Button) parentView.findViewById(R.id.fragment_edit_marker_button);
        TextView nameTextView = (TextView) parentView.findViewById(R.id.fragment_edit_marker_name);
        if(this.name != null)
            nameTextView.setText(this.name);

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
                            //We get parent fragment since EditMarkerFragment will be
                            //added inside a fragment and not like a root fragment. For example inside
                            //MapsFragment via getChildFragmentManager
                            Fragment parentFragment = getParentFragment();
                            if(parentFragment != null) {
                                parentFragment.onStop();
                                Gui.popFragment(
                                        getParentFragment().getChildFragmentManager()
                                );
                            }
                        }
                    }
            );
        }
    }
}

