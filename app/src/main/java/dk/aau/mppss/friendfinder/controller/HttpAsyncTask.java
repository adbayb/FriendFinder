package dk.aau.mppss.friendfinder.controller;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by EDF-NOMEDE on 03/08/2015.
 */
public class HttpAsyncTask extends AsyncTask<String, String, String> {

    private String urlRequest;
    private Map<String, Object> sqlFields;

    private InputStream is;


    public HttpAsyncTask(String urlRequest, Map<String, Object> sqlFields) {
        if (sqlFields != null)
            this.sqlFields = sqlFields;

        if (urlRequest != null)
            this.urlRequest = urlRequest;
    }


    @Override
    protected String doInBackground(String... params) {
        //String title = POI_title.getText().toString();
        //String description = POI_description.getText().toString();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://friendfinder.alwaysdata.net/FriendFinder/create_poi.php");

        try {
            List<NameValuePair> args = new ArrayList<NameValuePair>(2);

            for (Map.Entry<String, Object> sqlField : sqlFields.entrySet()) {
                args.add(new BasicNameValuePair(sqlField.getKey(), (String) sqlField.getValue()));
            }
            // args.add(new BasicNameValuePair("title", "New Tile Nydia"));
            //args.add(new BasicNameValuePair("description", "New Description Nydia"));

            httppost.setEntity(new UrlEncodedFormEntity(args));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
