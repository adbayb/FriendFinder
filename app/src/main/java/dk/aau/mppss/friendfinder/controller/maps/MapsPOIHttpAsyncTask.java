package dk.aau.mppss.friendfinder.controller.maps;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import dk.aau.mppss.friendfinder.UtilityClass;
import dk.aau.mppss.friendfinder.controller.OnHttpAsyncTask;
import dk.aau.mppss.friendfinder.model.maps.POIMarkerModel;

/**
 * Created by adibayoub on 05/08/2015.
 */
public class MapsPOIHttpAsyncTask implements OnHttpAsyncTask {
    private MapsController mapsController;

    public MapsPOIHttpAsyncTask(MapsController mapsController) {
        this.mapsController = mapsController;
    }

    @Override
    public void onHttpAsyncTaskCompleted(String result) {
        //Log.e("AYOUB", "onPostExecute");
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("poi");
                if(jsonArray != null) {
                    Map<String, Object> poiSQLList = null;
                    for(int index = 0; index < jsonArray.length(); index++) {
                        poiSQLList = UtilityClass.parseJSONObject(jsonArray.getJSONObject(index));
                        if(this.mapsController != null) {
                            if(poiSQLList != null) {
                                mapsController.addPOIMarker(
                                        new POIMarkerModel(
                                                poiSQLList.get("title").toString(),
                                                poiSQLList.get("description").toString(),
                                                Double.parseDouble(
                                                        poiSQLList.get("latitude")
                                                                .toString()
                                                ),
                                                Double.parseDouble(
                                                        poiSQLList.get("longitude")
                                                                .toString()
                                                ),
                                                null
                                        )
                                );
                            }
                        }
                    }
                    Log.e("AYOUB List POI", this.mapsController.getPOIList().toString());
                    Log.e("AYOUB List FBBBB", this.mapsController.getFBList().toString());
                }
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
