package com.example.gpsweatherdata.gpsweatherdata;

import android.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Simon on 2015-02-18.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener{

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id){


        selectItem(position);
    }

    private void selectItem(int position) {

        //fragment transaction

    }


}
