package dk.aau.mppss.friendfinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adibayoub on 03/08/2015.
 */
public final class UtilityClass {
    public static final String urlCreatePOI = "http://friendfinder.alwaysdata.net/FriendFinder/create_poi.php";
    public static final String urlUpdatePOI = "http://friendfinder.alwaysdata.net/FriendFinder/update_poi.php";
    public static final String urlDeletePOI = "http://friendfinder.alwaysdata.net/FriendFinder/delete_poi.php";
    public static final String urlGetAllUserPOI = "http://friendfinder.alwaysdata.net/FriendFinder/get_all_my_poi.php";
    public static final String urlGetFriendsFB = "http://friendfinder.alwaysdata.net/FriendFinder/list_users.php";
    public static final String urlGetAllFriendsPOI = "http://friendfinder.alwaysdata.net/FriendFinder/get_all_friends_poi.php";

    //Utility Constants:
    private static String userID;
    private static String userName;

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

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UtilityClass.userName = userName;
    }
}
