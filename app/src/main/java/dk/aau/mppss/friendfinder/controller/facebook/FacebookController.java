package dk.aau.mppss.friendfinder.controller.facebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.aau.mppss.friendfinder.MapsActivity;
import dk.aau.mppss.friendfinder.UtilityClass;
import dk.aau.mppss.friendfinder.model.facebook.User;

/**
 * Created by adibayoub on 30/07/2015.
 */
public class FacebookController {
    private List<String> friendsIDList;
    private User user;

    public FacebookController() {
        this.friendsIDList = new ArrayList<String>();
        this.user = null;
    }

    public void getResultRequest(AppCompatActivity _context) {
        this.friendsRequest(_context);
        this.meRequest(_context);

        return;
    }

    public boolean friendsRequest(AppCompatActivity _context) {
        //Log.e("Token out", "" + AccessToken.getCurrentAccessToken().getToken());
        final AppCompatActivity context = _context;
        if(context != null) {
            GraphRequestAsyncTask test = GraphRequest.newMyFriendsRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONArrayCallback() {
                        @Override
                        public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                            for(int index = 0; index < jsonArray.length(); index++) {
                                try {
                                    Map<String, Object> jsonMapObject = UtilityClass.parseJSONObject(
                                            jsonArray.getJSONObject(index)
                                    );
                                    populateFriendsList(jsonMapObject);
                                }
                                catch(JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            return;
                        }
                    }
            ).executeAsync();

            return true;
        }

        return false;
    }

    public boolean meRequest(AppCompatActivity _context) {
        final AppCompatActivity context = _context;
        if(context != null) {
            GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                            //Log.e("Ayoub0", "Pass0" + user.toString());
                            Map<String, Object> jsonKeyValue = UtilityClass.parseJSONObject(jsonObject);
                            user = new User(
                                    jsonKeyValue.get("id").toString(), jsonKeyValue.get("name")
                                    .toString(), null, null
                            );

                            if(user != null) {
                                /*
                                //We save user Id inside shared Preferences file:
                                SharedPreferences userSettings = context.getSharedPreferences(".userSettingsFile", context.MODE_PRIVATE);
                                SharedPreferences.Editor sharedPrefEditor = userSettings.edit();
                                sharedPrefEditor.putString("userId", user.getId());
                                sharedPrefEditor.putString("userName", user.getName());
                                sharedPrefEditor.commit();
                                */
                                //We must switch activities only after completed FB Request:
                                Intent switchMapActivity = new Intent(context, MapsActivity.class);
                                switchMapActivity.putExtra("userId", user.getId());
                                switchMapActivity.putExtra("userName", user.getName());
                                context.startActivity(switchMapActivity);
                            }
                            //Log.e("Ayoub0", "Pass1" + user.toString());
                        }
                    }
            ).executeAsync();

            return true;
        }

        return false;
    }

    //Private functions: utility class functions:
    private boolean populateFriendsList(Map<String, Object> jsonKeyValue) {
        if(jsonKeyValue != null) {
            /*
            String id = new String();
            String name = new String();
            for(Map.Entry<String, Object> jsonEntry : jsonKeyValue.entrySet()) {
                String key = jsonEntry.getKey();
                //see http://perso.ensta-paristech.fr/~diam/java/online/notes-java/data/expressions/22compareobjects.html
                if(key.equals("id")) id = jsonEntry.getValue().toString();
                else if(key.equals("name")) name = jsonEntry.getValue().toString();
                //and so on...
            }
            */
            this.friendsIDList.add(
                    jsonKeyValue.get("id").toString()
            );
            //Log.e("Ayoub", this.friendsIDList.toString());

            return true;
        }

        return false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getFriendsIDList() {
        return friendsIDList;
    }

    public void setFriendsIDList(List<String> friendsIDList) {
        this.friendsIDList = friendsIDList;
    }
}
