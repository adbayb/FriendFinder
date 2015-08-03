package dk.aau.mppss.friendfinder.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.aau.mppss.friendfinder.MapsActivity;
import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.StaticValue;
import dk.aau.mppss.friendfinder.controller.maps.MapsController;
import dk.aau.mppss.friendfinder.model.maps.MarkerModel;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapsFragment extends Fragment {
    private MapsController mapsController;
    private List<? extends MarkerModel> sqlMarkers;

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //false in order to allow android system to attach fragment view inside the
        //parent view automatically in function of resolution etc...:
        View parentView = inflater.inflate(R.layout.fragment_maps, container, false);

        GoogleMap googleMap = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_maps_map_view))
                .getMap();
        //Controller Initialization:
        if(this.mapsController == null) {
            this.mapsController = new MapsController(this.getChildFragmentManager(), inflater, googleMap);
            //We set a defaut camera position with animation:
            this.mapsController.moveAnimatedCamera(58.0, 9.0, 4);
        }
        //same for inflater:
        MapsActivity mapsActivity = (MapsActivity) getActivity();
        if(mapsActivity != null) {
            this.mapsController.enableWindowAdapter(inflater);
        }
        //Log.e("AYOUB", "onCreateView ");
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(this.mapsController != null) {
            this.mapsController.initializeMaps();
        }

        new HttpAsyncTask().execute("http://friendfinder.alwaysdata.net/FriendFinder/get_all_poi.php");
        //Log.e("AYOUB", "onActivityCreated "+ this.poiList);
    }

    @Override
    public void onResume() {
        //onResume allows interaction with user (a sort of main for a fragment but it's not a while loop!).
        //onCreate -> onStart -> Activity becomes visible -> onResume -> Interaction with user -> Activity is running...
        //Since we do not do a while loop, onResume will be executed each time an activity or fragment here change its states.
        //From Android developer:
        //onResume isn't limited to being called after the activity has been paused, it's called whenever the activity goes to the
        //top of the activity stack. That includes the first time it's shown after it's been created.
        super.onResume();

        if(this.mapsController != null) {
            this.mapsController.addPOIListener();
            this.mapsController.removePOIListener();
        }
        //Log.e("AYOUB", "onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.e("AYOUB", "onPause ");
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.e("AYOUB", "onStop ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.e("AYOUB", "onDestroy ");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //Log.e("AYOUB", "onLowMemory ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public MapsController getMapsController() {
        return mapsController;
    }

    public void setMapsController(MapsController mapsController) {
        this.mapsController = mapsController;
    }

    private Map<String, Object> parseJSONObject(JSONObject jsonObject) {
        Map<String, Object> listValues = new HashMap<String, Object>();
        //jsonObject.names() : names <=> key
        for(int index = 0; index < jsonObject.names().length(); index++) {
            try {
                String key = jsonObject.names().getString(index);
                Object value = jsonObject.get(jsonObject.names().getString(index));
                listValues.put(key, value);
                Log.d("JSONObject parsing", "key=" + key + " value=" + value);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return listValues;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(StaticValue.urlGetAllPOI));
                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
            }
            catch(Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("poi");
                Map<String, Object> poiSQLList = null;
                for(int index = 0; index < jsonArray.length(); index++) {
                    poiSQLList = parseJSONObject(jsonArray.getJSONObject(index));
                    mapsController.addMarker(
                            new MarkerModel(
                                    poiSQLList.get("title").toString(),
                                    Double.parseDouble(poiSQLList.get("latitude").toString()),
                                    Double.parseDouble(poiSQLList.get("longitude").toString())
                            )
                    );
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}