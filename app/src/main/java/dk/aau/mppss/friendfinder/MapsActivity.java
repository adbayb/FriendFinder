package dk.aau.mppss.friendfinder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import dk.aau.mppss.friendfinder.view.Gui;
import dk.aau.mppss.friendfinder.view.fragments.FacebookFragment;
import dk.aau.mppss.friendfinder.view.fragments.MapsFragment;
import dk.aau.mppss.friendfinder.view.fragments.POIFragment;

/*
FragmentActivity extends ActionBarActivity so we only need to extend ActionBarActivity
to have controls on ActionBar and FragmentActivity
Since Android support Library, we need to extends AppCompatActivity instead of ActionBarActivity (deprecated)
 */
public class MapsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Gui.createGui(
                this,
                new ArrayList<String>(Arrays.asList("Maps View", "POI List", "FB List")),
                new ArrayList<Fragment>(Arrays.asList(new MapsFragment(), new POIFragment(), new FacebookFragment())),
                2
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
