package dk.aau.mppss.friendfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dk.aau.mppss.friendfinder.controller.HttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.OnHttpAsyncTask;
import dk.aau.mppss.friendfinder.view.Gui;
import dk.aau.mppss.friendfinder.view.fragments.FacebookFragment;
import dk.aau.mppss.friendfinder.view.fragments.MapsFragment;
import dk.aau.mppss.friendfinder.view.fragments.POIFragment;

/*
FragmentActivity extends ActionBarActivity so we only need to extend ActionBarActivity
to have controls on ActionBar and FragmentActivity
Since Android support Library, we need to extends AppCompatActivity instead of ActionBarActivity (deprecated)
 */
public class MapsActivity extends AppCompatActivity implements OnHttpAsyncTask {
    private MapsFragment mapsFragment;
    private POIFragment poiFragment;
    private FacebookFragment facebookFragment;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(this.getDataMainActivity() == true) {
            //httpRequest will call automatically gui when request is finished
            //(cf onHttpAsyncTaskCompleted function and OnHttpAsyncTask Interface):
            this.initializeUserOnDatabase();
        }
    }

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

    @Override
    public void onHttpAsyncTaskCompleted(String result) {
        //If request is finished we can display Map:
        this.mapsFragment = new MapsFragment();
        this.poiFragment = new POIFragment();
        this.facebookFragment = new FacebookFragment();

        this.viewPager = Gui.createGui(
                this,
                new ArrayList<String>(Arrays.asList("Maps View", "POI List", "FB List")),
                new ArrayList<Fragment>(Arrays.asList(this.mapsFragment, this.poiFragment, this.facebookFragment)),
                2
        );

        return;
    }

    private boolean getDataMainActivity() {
        Bundle fromFBActivity = getIntent().getExtras();
        if(fromFBActivity != null) {
            UtilityClass.setFriendsList(fromFBActivity.getStringArrayList("friendsFBId"));
            SharedPreferences userSettings = getSharedPreferences(".userSettingsFile", this.MODE_PRIVATE);
            UtilityClass.setUserID(userSettings.getString("userId", null));
            UtilityClass.setUserName(userSettings.getString("userName", null));
            //Log.e("Bundle MainActivity", "" + UtilityClass.getFriendsList());
            //Log.e("Bundle MainActivity(2)", UtilityClass.getUserID());
            //Log.e("Bundle MainActivity(3)", UtilityClass.getUserName());

            return true;
        }

        return false;
    }

    private boolean initializeUserOnDatabase() {
        final String userID = UtilityClass.getUserID();
        final String userName = UtilityClass.getUserName();
        final List<String> friends = UtilityClass.getFriendsList();
        if(userID != null) {
            if(friends != null) {
                //We insert fb user if not exists on DB and match its friends (do relations) on table facebook_user:
                HttpAsyncTask userHttpTask = new HttpAsyncTask(
                        MapsActivity.this,
                        UtilityClass.urlCreateUser,
                        new HashMap<String, Object>() {{
                            put("userId", userID);
                            put("name", userName);
                            put("lastname", UtilityClass.getUserName());
                            put("friendsId", UtilityClass.listStringTOLine(friends));
                        }}
                );
                userHttpTask.execute();

                return true;
            }
        }

        return false;
    }
}
