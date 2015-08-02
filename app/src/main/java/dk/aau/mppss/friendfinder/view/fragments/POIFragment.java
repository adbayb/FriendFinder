package dk.aau.mppss.friendfinder.view.fragments;

//Login info

/*
FTP
host: ftp-friendfinder.alwaysdata.net
user: friendfinder_susie
pw: susie
port: 21

SQL:
https://phpmyadmin.alwaysdata.com/
User Id : 110602_user
Pwd : friendfinder
*/

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

import dk.aau.mppss.friendfinder.R;

/**
 * Created by adibayoub on 27/07/2015.
 */
public class POIFragment extends Fragment {
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

        //false to let android attach automatically fragment to the root view (i.e. parent view):
        View parentView = inflater.inflate(R.layout.fragment_poi, container, false);
        //View parentViewSecond = inflater.inflate(R.layout.poi_listview_item, container, false);

        // get reference to the views
        myList = (ListView) parentView.findViewById(R.id.fragment_poi_list_view);

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute("http://friendfinder.alwaysdata.net/FriendFinder/get_all_poi.php");

        return parentView;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            try {
                obj = new JSONObject(result);
                items = obj.getJSONArray("poi");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Context c = getActivity();
            //adapter = new ListViewAdapter(POIFragment.getActivity().getApplicationContext(), items);
            adapter = new ListViewAdapter(getActivity(), items);

           // adapter = new ListViewAdapter();
            if (myList.getAdapter() == null) {
                myList.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }


            /*try {
                js_obj = items.getJSONObject(1);
                //adapter.add(js_obj.getString("title"));
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }
    }

    // the Adapter
    private class ListViewAdapter extends BaseAdapter {

        private Context context = null;
        private List<String> fields = null;
        private JSONObject curr_obj;
        private String title;
        private String description;

      //  public ListViewAdapter(Context context, JSONArray arr) {
      public ListViewAdapter(Activity context, JSONArray arr) {
            //this.context = context;
            this.context = getActivity();
            this.fields = new ArrayList<String>();
            for (int i=0; i<arr.length(); ++i) {
                try {
                    curr_obj = arr.getJSONObject(i);
                    title = curr_obj.getString("title");
                    description = curr_obj.getString("description");
                    fields.add(title + " : " + description);
                    //fields.add(arr.getJSONObject(i).getString("title"));
                    //arr.getJSONObject(i).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getCount() {
            return fields.size();
        }

        @Override
        public Object getItem(int position) {
            return fields.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           // myList = (ListView) parentView.findViewById(R.id.Lists_notificationsListview);

            convertView = inflater.inflate(R.layout.poi_listview_item, null);
            TextView txt = (TextView) convertView.findViewById(R.id.ItemList_txt);
            txt.setText(fields.get(position));
            return convertView;
        }

        public void add(Object o) {
            this.fields.add(o.toString());
        }

    }



}
