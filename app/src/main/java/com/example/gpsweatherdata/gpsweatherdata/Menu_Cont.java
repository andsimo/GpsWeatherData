package com.example.gpsweatherdata.gpsweatherdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import java.util.ArrayList;


public class Menu_Cont extends Activity{

    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_cont);


    }

    public void showNA(View v){
        value = 1;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }

    public void showEU(View v){
        value = 2;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }

    public void showAS(View v){
        value = 3;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }


    public void showSA(View v){
        value = 4;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }

    public void showAF(View v){
        value = 5;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }


    public void showAUS(View v){
        value = 6;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", value);
        startActivity(intent);


    }



}
