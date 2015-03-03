package com.example.gpsweatherdata.gpsweatherdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class NavigationDrawerAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;


    public NavigationDrawerAdapter(Context context, String[] values) {
        super(context, R.layout.drawer_layout ,values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.drawer_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.item_txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_ic);
        textView.setText(values[position]);

        String s = values[position];
        System.out.println(s);

        switch (s) {
            case "Map":
                imageView.setImageResource(R.drawable.ic_action_map);
                break;
            case "Update":
                imageView.setImageResource(R.drawable.ic_action_refresh_light);
                break;
            case "Navigate Locations":
                imageView.setImageResource(R.drawable.ic_action_view_as_list);
                break;
            case "Visit Parans Website":
                imageView.setImageResource(R.drawable.ic_action_web_site);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_launcher);
                break;
        }

        return rowView;
    }
}
