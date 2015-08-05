package dk.aau.mppss.friendfinder.controller.maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import dk.aau.mppss.friendfinder.UtilityClass;
import dk.aau.mppss.friendfinder.controller.OnHttpAsyncTask;
import dk.aau.mppss.friendfinder.model.maps.FBMarkerModel;

/**
 * Created by adibayoub on 05/08/2015.
 */
public class MapsFBHttpAsyncTask implements OnHttpAsyncTask {
    private MapsController mapsController;
    private int idIcon;

    public MapsFBHttpAsyncTask(MapsController mapsController, int idRessourceIcon) {
        this.mapsController = mapsController;
        this.idIcon = idRessourceIcon;
    }

    @Override
    public void onHttpAsyncTaskCompleted(String result) {
        //Log.e("AYOUB", "onPostExecute"+result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("user");
                if(jsonArray != null) {
                    Map<String, Object> fbSQLList = null;
                    for(int index = 0; index < jsonArray.length(); index++) {
                        fbSQLList = UtilityClass.parseJSONObject(jsonArray.getJSONObject(index));
                        if(this.mapsController != null) {
                            if(fbSQLList != null) {
                                //Log.e("OUTPUT", ""+fbSQLList);
                                mapsController.addFBMarker(
                                        new FBMarkerModel(
                                                fbSQLList.get("name").toString(),
                                                null,
                                                Double.parseDouble(
                                                        fbSQLList.get("lastKnownLocationLat")
                                                                .toString()
                                                ),
                                                Double.parseDouble(
                                                        fbSQLList.get("lastKnownLocationLong")
                                                                .toString()
                                                ),
                                                null
                                        ),
                                        idIcon
                                );
                            }
                        }
                    }
                    //Log.e("AYOUB List POI", this.mapsController.getPOIList().toString());
                    //Log.e("AYOUB List FBBBB", this.mapsController.getFBList().toString());
                }
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public MapsController getMapsController() {
        return mapsController;
    }

    public void setMapsController(MapsController mapsController) {
        this.mapsController = mapsController;
    }

    public int getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(int idIcon) {
        this.idIcon = idIcon;
    }
}

