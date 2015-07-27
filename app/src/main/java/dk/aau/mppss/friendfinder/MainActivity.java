package dk.aau.mppss.friendfinder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.Arrays;

import dk.aau.mppss.friendfinder.view.Gui;
import dk.aau.mppss.friendfinder.view.fragments.MapsFragment;
import dk.aau.mppss.friendfinder.view.fragments.POIFragment;

/*
FragmentActivity extends ActionBarActivity so we only need to extend ActionBarActivity
to have controls on ActionBar and FragmentActivity
 */
public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar SetUp:
        ActionBar actionBar = getSupportActionBar();
        //new Gui(actionBar);
        actionBar = Gui.actionBarConfigurations(actionBar);
        actionBar = Gui.addTabs(new ArrayList<String>(Arrays.asList("Maps View", "POI List")),
                new ArrayList<Fragment>(Arrays.asList(new MapsFragment(), new POIFragment())),
                actionBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
