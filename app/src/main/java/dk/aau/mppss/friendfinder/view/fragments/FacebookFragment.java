package dk.aau.mppss.friendfinder.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dk.aau.mppss.friendfinder.R;
import dk.aau.mppss.friendfinder.UtilityClass;
import dk.aau.mppss.friendfinder.controller.HttpAsyncTask;
import dk.aau.mppss.friendfinder.controller.OnHttpAsyncTask;

/**
 * Created by sekou on 04/08/2015.
 */
public class FacebookFragment extends Fragment implements OnHttpAsyncTask {
    private ListView myList;
    private ListViewAdapter adapter;
    private JSONObject obj;
    private JSONArray items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_facebook, container, false);

        myList = (ListView) parentView.findViewById(R.id.fragment_facebook_list_view);

        //Perform querie to Db to get users table
        this.httpOnSelectFriend(UtilityClass.urlGetFriendsFB);

        return parentView;
    }

    public void httpOnSelectFriend(String url) {
        try {
            String requestIDFriends = "";
            Iterator<String> iterator = UtilityClass.getFriendsUserID().iterator();
            while(iterator.hasNext()) {
                requestIDFriends += "'" + iterator.next() + "'";
                if(iterator.hasNext())
                    requestIDFriends += ",";
            }
            final String requestIDs = requestIDFriends;
            //Log.e("Ayoubbbbb",requestIDFriends);
            HttpAsyncTask httpAsyncTask = new HttpAsyncTask(
                    FacebookFragment.this,
                    url,
                    new HashMap<String, Object>() {{
                        put("friendId", requestIDs);
                    }}
            );
            httpAsyncTask.execute();
        }
        catch(Exception e) {
            //Log.e("Fail", e.toString());
        }

        return;
    }

    @Override
    public void onHttpAsyncTaskCompleted(String result) {
        //Log.e("FB AyoubLog", result);
        try {
            obj = new JSONObject(result);
            if(obj != null)
                items = obj.getJSONArray("user");
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        if(items != null) {
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

        return;
    }

    // the Adapter
    private class ListViewAdapter extends BaseAdapter {

        private Context context = null;
        private List<String> fields = null;
        private JSONObject curr_obj;
        private String name;
        //private String lastname;


        //  public ListViewAdapter(Context context, JSONArray arr) {
        public ListViewAdapter(Activity context, JSONArray arr) {
            this.context = context;
            if(context != null) {
                if(arr != null) {
                    this.fields = new ArrayList<String>();
                    for(int i = 0; i < arr.length(); ++i) {
                        try {
                            curr_obj = arr.getJSONObject(i);
                            if(curr_obj != null) {
                                //lastname = curr_obj.getString("lastname");
                                name = curr_obj.getString("name");
                                fields.add(name);
                                //fields.add(arr.getJSONObject(i).getString("title"));
                                //arr.getJSONObject(i).toString();
                            }
                        }
                        catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
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

            convertView = inflater.inflate(R.layout.facebook_listview_item, null);
            TextView txt = (TextView) convertView.findViewById(R.id.UserList_txt);
            txt.setText(fields.get(position));
            return convertView;
        }

        public void add(Object o) {
            this.fields.add(o.toString());
        }


    }
}
