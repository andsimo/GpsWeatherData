package com.example.gpsweatherdata.gpsweatherdata;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class MainActivity extends Activity {

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


        worker.schedule(task, 700, TimeUnit.MILLISECONDS);



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
