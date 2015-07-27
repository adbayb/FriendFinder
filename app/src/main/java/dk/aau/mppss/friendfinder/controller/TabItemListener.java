package dk.aau.mppss.friendfinder.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import dk.aau.mppss.friendfinder.R;

/**
 * Created by adibayoub on 27/07/2015.
 */
public class TabItemListener implements ActionBar.TabListener {
    private Fragment fragment;

    public TabItemListener(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.replace(R.id.fragment_container, fragment);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.remove(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //Nothing todo: fragment is already displayed.
    }
}