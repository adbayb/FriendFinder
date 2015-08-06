package dk.aau.mppss.friendfinder.controller.maps.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dk.aau.mppss.friendfinder.R;

/**
 * Created by adibayoub on 06/08/2015.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context = null;
    private List<String> fields = null;
    private JSONObject curr_obj;
    private String title;
    private String description;

    //  public ListViewAdapter(Context context, JSONArray arr) {
    public ListViewAdapter(Activity context, JSONArray arr) {
        //this.context = context;
        if(arr != null) {
            this.context = context;
            this.fields = new ArrayList<String>();
            for(int i = 0; i < arr.length(); ++i) {
                try {
                    curr_obj = arr.getJSONObject(i);
                    title = curr_obj.getString("title");
                    description = curr_obj.getString("description");
                    fields.add(title + " : " + description);
                    //fields.add(arr.getJSONObject(i).getString("title"));
                    //arr.getJSONObject(i).toString();
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getCount() {
        if(fields != null)
            return fields.size();
        return 0;
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
