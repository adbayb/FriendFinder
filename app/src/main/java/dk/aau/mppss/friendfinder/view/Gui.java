package dk.aau.mppss.friendfinder.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;

import java.util.List;

import dk.aau.mppss.friendfinder.controller.TabItemListener;

/**
 * Created by adibayoub on 27/07/2015.
 */
public class Gui {
    private ActionBar actionBar;
    //private ActionBar.Tab mapsTab;
    //private ActionBar.Tab poiTab;

    public Gui(ActionBar actionBar) {
        //Fragment mapsFragment = new MapsFragment();
        //Fragment poiFragment = new POIFragment();
        //this.actionBar = actionBar;
        /*
        this.actionBar = Gui.actionBarConfigurations(actionBar);

        this.actionBar = Gui.addTabs(new ArrayList<String>(Arrays.asList("MapsModel View","POI List")),
                                        new ArrayList<Fragment>(Arrays.asList(new MapsFragment(),new POIFragment())),
                                        this.actionBar);
        */
    }

    public static ActionBar actionBarConfigurations(ActionBar _actBar) {
        ActionBar actBar = _actBar;

        if(actBar != null) {
            //Desactivate clickable title (arrow):
            actBar.setDisplayHomeAsUpEnabled(false);
            //Force displaying app title:
            actBar.setDisplayShowTitleEnabled(true);
            //Set up tabs mode:
            actBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            return actBar;
        }
        Log.e("Gui Class -> config.: ", "Null ActionBar");
        return null;
    }

    public static ActionBar addTabs(List<String> names, List<Fragment> fragments, ActionBar _actBar) {
        ActionBar actBar = _actBar;
        int index = 0;

        if(actBar != null) {
            //Log.d("Ayoub0", names.size() + "-" + fragments.size());
            //We need to have same number of tabs and fragments to succeed in attaching listener to each tab:
            if(names.size() == fragments.size()) {
                for(String name : names) {
                    //Log.d("AyoubFragments:", fragments.get(index).toString()+"//"+index);
                    ActionBar.Tab tab = actBar.newTab().setText(name);
                    tab.setTabListener(new TabItemListener(fragments.get(index)));

                    actBar.addTab(tab);
                    index++;
                }

                return actBar;
            }
            Log.e("Gui Class -> addTabs: ", "More tabs than listeners (or in return)");
            return null;
        }
        Log.e("Gui Class -> addTabs: ", "Null ActionBar");
        return null;
    }
}
