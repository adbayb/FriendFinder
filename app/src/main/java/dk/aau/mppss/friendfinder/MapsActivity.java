package dk.aau.mppss.friendfinder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
public class MapsActivity extends ActionBarActivity {
    private MapsFragment mapsFragment;
    private POIFragment poiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        this.displayHomePage(savedInstanceState);
    }

    private void initializeFragments(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            this.mapsFragment = (MapsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MapsFragment_State");
        } else {
            this.mapsFragment = new MapsFragment();
        }
        this.poiFragment = new POIFragment();

        return;
    }

    private void displayHomePage(Bundle savedInstanceState) {
        this.initializeFragments(savedInstanceState);
        //ActionBar SetUp:
        ActionBar actionBar = getSupportActionBar();
        //new Gui(actionBar);
        actionBar = Gui.actionBarConfigurations(actionBar);
        actionBar = Gui.addTabs(
                new ArrayList<String>(Arrays.asList("MapsModel View", "POI List")),
                new ArrayList<Fragment>(Arrays.asList(this.mapsFragment, this.poiFragment)),
                actionBar
        );
    }

    //Allows to replace a current fragment with another:
    public void replaceFragment(Fragment fragment) {
        //We check if activity is finished to avoid
        //java.lang.IllegalStateException: Activity has been destroyed exception:
        if(isFinishing() == false) {
            //.commitAllowingStateLoss(); allows to commit after the previous fragment saved its Instance
            //.commit(); doesn't ensure it and we can get exception with it:
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }

        return;
    }

    //Attach (set) a fragment to the activity view:
    public void attachFragment(Fragment fragment) {
        if(isFinishing() == false) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commitAllowingStateLoss();
        }

        return;
    }

    public void detachFragment(Fragment fragment) {
        if(isFinishing() == false) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();
        }

        return;
    }


    //Allowing to return to previous displayed Fragment after switching:
    public void previousFragment() {
        this.getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //We saved MapsFragment in order to allow fragment backup data (for example after a
        //rotation, it resets map so we need to store data map (marker...)):
        if(this.mapsFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.putFragment(outState, "MapsFragment_State", this.mapsFragment);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
