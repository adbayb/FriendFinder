package dk.aau.mppss.friendfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
    private MapsFragment mapsFragment;
    private POIFragment poiFragment;
    private FacebookFragment facebookFragment;
    private ViewPager viewPager;

    @Override
    public void onBackPressed() {
        if(this.viewPager != null) {
            //We only test if EditMarkerFragment is visible inside the first tab (ie 0 that contains MapsFragment):
            if(this.viewPager.getCurrentItem() == 0) {
                //We only allow back pressed when EditMarker Fragment is displayed:
                /*if(this.mapsFragment.isVisibleEditMarkerFragment() == true) {
                    super.onBackPressed();
                }*/
                //We override BackPressed capabilities when an EditMarkerFragment is displayed by hiding the editor UI:
                this.mapsFragment.popEditMarkerFragment();
            }
        }
        //else no BackPressed allowed to avoid back to FB login:
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle fromFBActivity = getIntent().getExtras();
        if(fromFBActivity != null) {
            //UtilityClass.setUserID(fromFBActivity.getString("userFBId"));
            UtilityClass.setFriendsUserID(fromFBActivity.getStringArrayList("friendsFBId"));
            SharedPreferences userSettings = getSharedPreferences(".userSettingsFile", this.MODE_PRIVATE);
            UtilityClass.setUserID(userSettings.getString("userId", null));
            //Log.e("Bundle MainActivity", "" + UtilityClass.getFriendsUserID());
            //Log.e("Bundle MainActivity(2)", UtilityClass.getUserID());
        }

        this.mapsFragment = new MapsFragment();
        this.poiFragment = new POIFragment();
        this.facebookFragment = new FacebookFragment();

        this.viewPager = Gui.createGui(
                this,
                new ArrayList<String>(Arrays.asList("Maps View", "POI List", "FB List")),
                new ArrayList<Fragment>(Arrays.asList(this.mapsFragment, this.poiFragment, this.facebookFragment)),
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
