package dk.aau.mppss.friendfinder.controller.facebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.aau.mppss.friendfinder.MapsActivity;
import dk.aau.mppss.friendfinder.UtilityClass;
import dk.aau.mppss.friendfinder.model.facebook.Friend;
import dk.aau.mppss.friendfinder.model.facebook.User;

/**
 * Created by adibayoub on 30/07/2015.
 */
public class FacebookController {
    private List<Friend> friendsList;
    private User user;

    public FacebookController() {
        this.friendsList = new ArrayList<Friend>();
        this.user = null;
    }

    public void getResultRequest(AppCompatActivity context) {
        this.friendsRequest(context);
        this.meRequest(context);

        return;
    }

    public void friendsRequest(AppCompatActivity context) {
        //Log.e("Token out", "" + AccessToken.getCurrentAccessToken().getToken());
        GraphRequest.newMyFriendsRequest(
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
                    }
                }
        ).executeAsync();

        return;
    }

    public void meRequest(AppCompatActivity context) {
        final AppCompatActivity activity = context;
        GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        Map<String, Object> jsonKeyValue = UtilityClass.parseJSONObject(jsonObject);
                        user = new User(
                                jsonKeyValue.get("id").toString(), jsonKeyValue.get("name")
                                .toString(), null, null
                        );
                        //We must switch activities only after completed FB Request:
                        Intent switchMapActivity = new Intent(activity, MapsActivity.class);
                        switchMapActivity.putExtra(
                                "userFBId", user.getId().toString()
                        );
                        activity.startActivity(switchMapActivity);
                    }
                }
        ).executeAsync();

        return;
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
            friendsList.add(
                    new Friend(
                            jsonKeyValue.get("id").toString(),
                            jsonKeyValue.get("name").toString(),
                            null,
                            null
                    )
            );
            Log.e("Ayoub", friendsList.toString());

            return true;
        }

        return false;
    }

    public List<Friend> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<Friend> friendsList) {
        this.friendsList = friendsList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
