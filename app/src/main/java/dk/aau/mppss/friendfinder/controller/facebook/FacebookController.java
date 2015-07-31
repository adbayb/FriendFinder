package dk.aau.mppss.friendfinder.controller.facebook;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.aau.mppss.friendfinder.model.facebook.Friend;

/**
 * Created by adibayoub on 30/07/2015.
 */
public class FacebookController {
    private List<Friend> friendsList;

    public FacebookController() {
        this.friendsList = new ArrayList<Friend>();
    }

    public void getResultRequest() {
        this.friendsRequest();
        this.meRequest();

        return;
    }

    public void friendsRequest() {
        GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                        for(int index = 0; index < jsonArray.length(); index++) {
                            try {
                                Map<String, Object> jsonMapObject = parseJSONObject(jsonArray.getJSONObject(index));
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

    public void meRequest() {
        GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        parseJSONObject(jsonObject);
                    }
                }
        ).executeAsync();

        return;
    }

    public List<Friend> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<Friend> friendsList) {
        this.friendsList = friendsList;
    }

    //Private functions: utility class functions:
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

    private boolean populateFriendsList(Map<String, Object> jsonKeyValue) {
        if(jsonKeyValue != null) {
            String id = new String();
            String name = new String();
            for(Map.Entry<String, Object> jsonEntry : jsonKeyValue.entrySet()) {
                String key = jsonEntry.getKey();
                //see http://perso.ensta-paristech.fr/~diam/java/online/notes-java/data/expressions/22compareobjects.html
                if(key.equals("id")) id = jsonEntry.getValue().toString();
                else if(key.equals("name")) name = jsonEntry.getValue().toString();
                //and so on...
            }
            friendsList.add(new Friend(id, name));
            Log.e("Ayoub", friendsList.toString());

            return true;
        }

        return false;
    }
}
