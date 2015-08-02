package dk.aau.mppss.friendfinder.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import java.util.List;

/**
 * Created by adibayoub on 01/08/2015.
 */
public class TabController extends FragmentStatePagerAdapter implements ActionBar.TabListener {
    private ViewPager viewPager;
    private List<Fragment> fragments;

    public TabController(ViewPager viewPager, FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        if(viewPager != null)
            this.viewPager = viewPager;
        if(fragments != null)
            this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        /*Fragment fragment;
        switch(i) {
            case 0:
                fragment = new MapsFragment();
                break;
            case 1:
                fragment = new POIFragment();
                break;
            case 2:
                fragment = new FacebookFragment();
                break;
            default:
                fragment = new MapsFragment();
                break;
        };

        return fragment;*/
        if(fragments != null)
            return fragments.get(i);

        return null;
    }

    @Override
    public int getCount() {
        //return 3;
        //Log.e("Ayoub TabController", "" + fragments.size());
        return fragments.size();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //in order to choose fragment from view Pager given tab position:
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }
}
