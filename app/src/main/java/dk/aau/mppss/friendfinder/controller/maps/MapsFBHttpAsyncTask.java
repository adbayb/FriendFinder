package dk.aau.mppss.friendfinder.controller.maps;

import dk.aau.mppss.friendfinder.controller.OnHttpAsyncTask;

/**
 * Created by adibayoub on 05/08/2015.
 */
public class MapsFBHttpAsyncTask implements OnHttpAsyncTask {
    private MapsController mapsController;

    public MapsFBHttpAsyncTask(MapsController mapsController) {
        this.mapsController = mapsController;
    }

    @Override
    public void onHttpAsyncTaskCompleted(String result) {

    }
}
