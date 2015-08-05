package dk.aau.mppss.friendfinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adibayoub on 03/08/2015.
 */
public final class UtilityClass {
    public static final String urlCreatePOI = "http://friendfinder.alwaysdata.net/FriendFinder/create_poi.php";
    public static final String urlUpdatePOI = "http://friendfinder.alwaysdata.net/FriendFinder/update_poi.php";
    public static final String urlDeletePOI = "http://friendfinder.alwaysdata.net/FriendFinder/delete_poi.php";
    public static final String urlGetAllPOI = "http://friendfinder.alwaysdata.net/FriendFinder/get_all_poi.php";
    //Utility Constants:
    private static String userID;
    private static List<String> friendsUserID;

    private UtilityClass() {

    }

    //Utility Functions:
    public static Map<String, Object> parseJSONObject(JSONObject jsonObject) {
        Map<String, Object> listValues = new HashMap<String, Object>();
        //jsonObject.names() : names <=> key
        for(int index = 0; index < jsonObject.names().length(); index++) {
            try {
                String key = jsonObject.names().getString(index);
                Object value = jsonObject.get(jsonObject.names().getString(index));
                listValues.put(key, value);
                //Log.d("JSONObject parsing", "key=" + key + " value=" + value);
                //get user profile picture TO IMPLEMENT inside Friend Model!!:
                /*
                try {
                    URL profilePicture = new URL(
                            "http://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=large"
                    );
                    Log.d("JSONObject picture", "" + profilePicture);
                }
                catch(MalformedURLException e) {
                    e.printStackTrace();
                }
                */
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return listValues;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        UtilityClass.userID = userID;
    }

    public static List<String> getFriendsUserID() {
        return friendsUserID;
    }

    public static void setFriendsUserID(List<String> friendsUserID) {
        UtilityClass.friendsUserID = friendsUserID;
    }
}
