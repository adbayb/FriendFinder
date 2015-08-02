package dk.aau.mppss.friendfinder.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.controller.TabController;

/**
 * Created by adibayoub on 27/07/2015.
 */
//Les classes static n'existent pas en Java: on peut en simuler son comportement en définissant notre classe en final
//(ne peut pas être dérivée) et contructeur en private (ne peut pas être instancié en dehors de la classe)
//et en définissant toutes nos fonctions en static:
public final class Gui {
    private Gui() {

    }

    public static void createGui(AppCompatActivity context, List<String> tabNames, List<Fragment> tabFragments, int numberTabsToSave) {
        ActionBar actionBar = Gui.initializeActionBar(context);
        ViewPager viewPager = Gui.initializeViewPager(context, R.id.activity_maps_view_pager, numberTabsToSave);
        if(viewPager != null) {
            //Set up tabController:
            TabController tabListener = new TabController(
                    viewPager,
                    context.getSupportFragmentManager(),
                    tabFragments
            );
            viewPager.setAdapter(tabListener);
            if(actionBar != null) {
                actionBar = Gui.addTabs(tabNames, tabListener, actionBar);
            }
        }
    }

    public static ViewPager initializeViewPager(final AppCompatActivity context, int idViewPager, int numberTabToSave) {
        if(context != null) {
            ViewPager viewPager = (ViewPager) context.findViewById(idViewPager);
            if(viewPager != null) {
                //Set the number of pages that should be retained to either side of the current page. (from Android developer).
                //So setOffscreenPageLimit allows to save the state of fragments near the active one.
                //For example for a total of 3 tabs, We choose 2 since a tab has maximum 2 tabs near it (whatever side):
                viewPager.setOffscreenPageLimit(numberTabToSave);
                //Listener that allows to mark a tab selected when we swipe the tab
                viewPager.setOnPageChangeListener(
                        new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                //In order to show tab color selection (given tab position):
                                context.getSupportActionBar().setSelectedNavigationItem(position);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        }
                );
                return viewPager;
            }
            Log.e("Gui -> initViewPager: ", "null ViewPager");
            return null;
        }
        Log.e("Gui -> initViewPager: ", "null Activity");
        return null;
    }

    public static ActionBar initializeActionBar(final AppCompatActivity context) {
        if(context != null) {
            ActionBar actionBar = context.getSupportActionBar();
            if(actionBar != null) {
                //Desactivate clickable title (arrow):
                actionBar.setDisplayHomeAsUpEnabled(false);
                //Force displaying app title:
                actionBar.setDisplayShowTitleEnabled(true);
                //Set up tabs navigation mode:
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

                return actionBar;
            }
            Log.e("Gui -> initActionBar: ", "null ActionBar");
            return null;
        }
        Log.e("Gui -> initActionBar: ", "null Activity");
        return null;
    }

    public static ActionBar addTabs(List<String> tabNames, TabController tabListener, ActionBar actionBar) {
        int index = 0;

        if(actionBar != null) {
            //Log.d("Ayoub0", names.size() + "-" + fragments.size());
            //We need to have same number of tabs and fragments to succeed in attaching listener to each tab:
            if(tabListener.getFragments().size() == tabNames.size()) {
                for(String name : tabNames) {
                    //Log.d("AyoubFragments:", fragments.get(index).toString()+"//"+index);
                    ActionBar.Tab tab = actionBar.newTab()
                            .setText(name)
                            .setTabListener(tabListener);
                    actionBar.addTab(tab);
                    index++;
                }

                return actionBar;
            }
            Log.e("Gui -> addTabs: ", "More tabs than listeners (or in return)");
        }
        Log.e("Gui -> addTabs: ", "null ActionBar");

        return null;
    }

    public static void replaceFragment(FragmentManager parentFragmentManager, int idContainerFragment, Fragment newfragment) {
        if(parentFragmentManager != null) {
            if(newfragment != null)
                //We check if activity is finished to avoid
                //java.lang.IllegalStateException: Activity has been destroyed exception:
                //.commitAllowingStateLoss(); allows to commit after the previous fragment saved its Instance
                //.commit(); doesn't ensure it and we can get exception with it:
                parentFragmentManager.beginTransaction()
                        .replace(idContainerFragment, newfragment)
                        .addToBackStack(null)
                        .commit();
        }

        return;
    }

    //Attach (set) a fragment to the activity view:
    public static void attachFragment(FragmentManager parentFragmentManager, int idContainerFragment, Fragment fragment) {
        if(parentFragmentManager != null) {
            if(fragment != null)
                parentFragmentManager.beginTransaction()
                        .add(idContainerFragment, fragment)
                        .commit();
        }

        return;
    }

    public static void detachFragment(FragmentManager parentFragmentManager, Fragment fragment) {
        if(parentFragmentManager != null) {
            if(fragment != null)
                parentFragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit();
        }

        return;
    }


    //Allowing to return to previous displayed Fragment after switching:
    public static void popFragment(FragmentManager parentFragmentManager) {
        if(parentFragmentManager != null)
            parentFragmentManager.popBackStack();

        return;
    }
}
