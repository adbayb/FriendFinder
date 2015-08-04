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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.aau.mppss.friendfinder.StaticValue;

/**
 * Created by EDF-NOMEDE on 03/08/2015.
 */
public class HttpAsyncTask extends AsyncTask<String, String, String> {
    private String urlRequest;
    private Map<String, Object> sqlFields;

    public HttpAsyncTask(String urlRequest, Map<String, Object> sqlFields) {
        if (sqlFields != null)
            this.sqlFields = sqlFields;

        if (urlRequest != null)
            this.urlRequest = urlRequest;
    }


    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.urlRequest);

        try {
            List<NameValuePair> args = new ArrayList<NameValuePair>(2);

            for (Map.Entry<String, Object> sqlField : sqlFields.entrySet()) {
                args.add(new BasicNameValuePair(sqlField.getKey(), (String) sqlField.getValue()));
            }

            httppost.setEntity(new UrlEncodedFormEntity(args));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            //entity.getContent();
            Log.e("pass 1", this.urlRequest);
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
