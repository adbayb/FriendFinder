package dk.aau.mppss.friendfinder.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.UtilityClass;
import dk.aau.mppss.friendfinder.controller.HttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.OnHttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.maps.adapter.ListViewAdapter;

/**
 * Created by adibayoub on 27/07/2015.
 */
public class POIFragment extends Fragment implements OnHttpAsyncTask {
    private ListViewAdapter adapter;
    private ListView myList;
    private JSONObject obj;
    private JSONArray items;

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_poi, container, false);
        //get reference to the list view:
        myList = (ListView) parentView.findViewById(R.id.fragment_poi_list_view);

        //call AsynTask to perform network operation on separate thread
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
                POIFragment.this,
                UtilityClass.urlGetAllUserFriendsPOI,
                new HashMap<String, Object>() {{
                    put("idFacebook", UtilityClass.getUserID());
                }}
        );
        httpAsyncTask.execute();

        return parentView;
    }

    @Override
    public void onHttpAsyncTaskCompleted(String result) {
        try {
            obj = new JSONObject(result);
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        if(obj != null) {
            if(obj.has("poi")) {
                try {
                    items = obj.getJSONArray("poi");
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }

                adapter = new ListViewAdapter(getActivity(), items);
                if(adapter != null) {
                    // adapter = new ListViewAdapter();
                    if(myList != null) {
                        if(myList.getAdapter() == null) {
                            myList.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }

        return;
    }
}
