package com.example.gpsweatherdata.gpsweatherdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import java.util.ArrayList;


public class Menu_Cont extends Activity{

    Integer cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_cont);


    }

    public void showNA(View v){
        cont = 1;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", new String("NorthAM"));
        startActivity(intent);


    }

    public void showEU(View v){
        cont = 2;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", cont);
        startActivity(intent);


    }

    public void showAS(View v){
        cont = 3;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", cont);
        startActivity(intent);


    }


    public void showSA(View v){
        cont = 4;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", cont);
        startActivity(intent);


    }

    public void showAF(View v){
        cont = 5;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", cont);
        startActivity(intent);


    }


    public void showAUS(View v){
        cont = 6;
        Intent intent = new Intent(this, Menu_countries.class);
        intent.putExtra("continent", cont);
        startActivity(intent);


    }



}
