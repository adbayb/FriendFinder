package dk.aau.mppss.friendfinder.controller;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by EDF-NOMEDE on 03/08/2015.
 */
public class HttpAsyncTask extends AsyncTask<String, String, String> {
    private String urlRequest;
    private Map<String, Object> postSQLFields;
    private OnHttpAsyncTask onHttpAsyncTask;

    public HttpAsyncTask(OnHttpAsyncTask onHttpAsyncTask, String urlRequest) {
        //Constructor GET:
        this.postSQLFields = null;

        if(urlRequest != null)
            this.urlRequest = urlRequest;

        if(onHttpAsyncTask != null)
            this.onHttpAsyncTask = onHttpAsyncTask;
    }

    public HttpAsyncTask(OnHttpAsyncTask onHttpAsyncTask, String urlRequest, Map<String, Object> postSQLFields) {
        //Constructor POST:
        this.postSQLFields = postSQLFields;

        if(urlRequest != null)
            this.urlRequest = urlRequest;

        if(onHttpAsyncTask != null)
            this.onHttpAsyncTask = onHttpAsyncTask;
    }


    @Override
    protected String doInBackground(String... params) {
        if(this.postSQLFields != null)
            //Post request:
            return this.postRequest(params);
        else
            //get Request without post:
            return this.getRequest(params);
    }

    private String postRequest(String... params) {
        InputStream inputStream = null;
        String result = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.urlRequest);

        try {
            if(this.postSQLFields != null) {
                List<NameValuePair> args = new ArrayList<NameValuePair>(2);

                for(Map.Entry<String, Object> sqlField : this.postSQLFields.entrySet()) {
                    args.add(new BasicNameValuePair(sqlField.getKey(), (String) sqlField.getValue()));
                }

                httppost.setEntity(new UrlEncodedFormEntity(args));
                HttpResponse httpResponse = httpclient.execute(httppost);
                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null) {
                    //Log.e("AYOUBBB","getRequest Success");
                    result = this.inputStreamToString(inputStream);
                } else
                    result = "Error: HttpAsyncTask.postRequest null inputStream";

                return result;
            }
        }
        catch(Exception e) {
            //Log.e("HttpAsyncTask Fail", e.toString());
        }
        return null;
    }

    private String getRequest(String... params) {
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(this.urlRequest));
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if(inputStream != null) {
                //Log.e("AYOUBBB","getRequest Success");
                result = this.inputStreamToString(inputStream);
            } else
                result = "Error: HttpAsyncTask.getRequest null inputStream";

            return result;
        }
        catch(Exception e) {
            Log.d("HttpAsyncTask get: ", e.getLocalizedMessage());
        }
        return null;
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //Pas besoin de new pour String: on peut faire des String littéraux sans créer d'objet dans le heap:
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(this.onHttpAsyncTask != null)
            this.onHttpAsyncTask.onHttpAsyncTaskCompleted(s);
    }
}
