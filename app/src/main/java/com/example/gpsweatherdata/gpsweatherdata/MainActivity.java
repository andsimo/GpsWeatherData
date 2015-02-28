package com.example.gpsweatherdata.gpsweatherdata;


import android.content.Intent;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class MainActivity extends ActionBarActivity {

    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();

    private boolean clicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Runnable task = new Runnable(){

            @Override
            public void run() {

                if(!clicked)
                    showMap();
            }
        };


        worker.schedule(task, 1, TimeUnit.SECONDS);



    }

    @Override
    protected void onResume(){
        super.onResume();
        clicked = false;
    }




    public void showMap(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void showMap(View v){
        clicked = true;
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }


}
