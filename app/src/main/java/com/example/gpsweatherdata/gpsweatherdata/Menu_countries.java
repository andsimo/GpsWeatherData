package com.example.gpsweatherdata.gpsweatherdata;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import android.app.ListActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu_countries extends ListActivity{
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;


    private HashMap<String, ArrayList<String>> countries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countries);

        countries = new HashMap<>();
        fillCountries();

        lv = (ListView) findViewById(android.R.id.list);



        Intent intent = getIntent();
        Integer cont = intent.getExtras().getInt("continent");
        System.out.println(""+cont); System.out.println(""+cont); System.out.println(""+cont);
        if(cont.equals(1))
                setCountries("NorthAM".toString());
            else if(cont.equals(2))
                    setCountries("Europe".toString());
            else if(cont.equals(3))
                    setCountries("Asia".toString());
            else if(cont.equals(4))
                    setCountries("SouthAM".toString());
            else if(cont.equals(5))
                    setCountries("Africa".toString());
            else if(cont.equals(6))
                    setCountries("Australia".toString());





        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object item = lv.getItemAtPosition(position);
                String country = item.toString();
                startAct(country);
            }
        });


    }

    public void setCountries(String continent){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                countries.get("" + continent));

        lv.setAdapter(arrayAdapter);
    }

    public void startAct(String pick){
            Intent intent = new Intent(this, Cities.class);
            intent.putExtra("country", pick);
        System.out.println(""+pick);System.out.println(""+pick);System.out.println(""+pick);System.out.println(""+pick);
            startActivity(intent);
    }

    public void fillCountries(){
        countries.put("NorthAM", new ArrayList<String>());
        countries.get("NorthAM").add("Antigua and Barbuda");
        countries.get("NorthAM").add("Bahamas");
        countries.get("NorthAM").add("Barbados");
        countries.get("NorthAM").add("Belize");
        countries.get("NorthAM").add("Canada");
        countries.get("NorthAM").add("Costa Rica");
        countries.get("NorthAM").add("Cuba");
        countries.get("NorthAM").add("Dominica");
        countries.get("NorthAM").add("Dominican Republic");
        countries.get("NorthAM").add("El Salvador");
        countries.get("NorthAM").add("Grenada");
        countries.get("NorthAM").add("Guatemala");
        countries.get("NorthAM").add("Haiti");
        countries.get("NorthAM").add("Honduras");
        countries.get("NorthAM").add("Jamaica");
        countries.get("NorthAM").add("Mexico");
        countries.get("NorthAM").add("Nicaragua");
        countries.get("NorthAM").add("Panama");
        countries.get("NorthAM").add("Saint Kitts and Nevis");
        countries.get("NorthAM").add("Saint Lucia");
        countries.get("NorthAM").add("Saint Vincent and the Grenadines");
        countries.get("NorthAM").add("Trinidad and Tobago");
        countries.get("NorthAM").add("United States");

        countries.put("Europe", new ArrayList<String>());
        countries.get("Europe").add("Albania");
        countries.get("Europe").add("Andorra");
        countries.get("Europe").add("Armenia");
        countries.get("Europe").add("Austria");
        countries.get("Europe").add("Azerbaijan");
        countries.get("Europe").add("Belarus");
        countries.get("Europe").add("Belgium");
        countries.get("Europe").add("Bosnia and Herzegovina");
        countries.get("Europe").add("Bulgaria");
        countries.get("Europe").add("Croatia");
        countries.get("Europe").add("Cyprus");
        countries.get("Europe").add("Czech Republic");
        countries.get("Europe").add("Denmark");
        countries.get("Europe").add("Estonia");
        countries.get("Europe").add("Finland");
        countries.get("Europe").add("France");
        countries.get("Europe").add("Georgia");
        countries.get("Europe").add("Germany");
        countries.get("Europe").add("Greece");
        countries.get("Europe").add("Hungary");
        countries.get("Europe").add("Iceland");
        countries.get("Europe").add("Ireland");
        countries.get("Europe").add("Italy");
        countries.get("Europe").add("Latvia");
        countries.get("Europe").add("Liechtenstein");
        countries.get("Europe").add("Lithuania");
        countries.get("Europe").add("Luxembourg");
        countries.get("Europe").add("Macedonia");
        countries.get("Europe").add("Malta");
        countries.get("Europe").add("Moldova");
        countries.get("Europe").add("Monaco");
        countries.get("Europe").add("Montenegro");
        countries.get("Europe").add("Netherlands");
        countries.get("Europe").add("Norway");
        countries.get("Europe").add("Poland");
        countries.get("Europe").add("Portugal");
        countries.get("Europe").add("Romania");
        countries.get("Europe").add("San Marino");
        countries.get("Europe").add("Serbia");
        countries.get("Europe").add("Slovakia");
        countries.get("Europe").add("Slovenia");
        countries.get("Europe").add("Spain");
        countries.get("Europe").add("Sweden");
        countries.get("Europe").add("Switzerland");
        countries.get("Europe").add("Ukraine");
        countries.get("Europe").add("United Kingdom");
        countries.get("Europe").add("Vatican City");

        countries.put("Asia", new ArrayList<String>());
        countries.get("Asia").add("Afghanistan");
        countries.get("Asia").add("Bahrain");
        countries.get("Asia").add("Bangladesh");
        countries.get("Asia").add("Bhutan");
        countries.get("Asia").add("Brunei");
        countries.get("Asia").add("Burma (Myanmar)");
        countries.get("Asia").add("Cambodia");
        countries.get("Asia").add("China");
        countries.get("Asia").add("East Timor");
        countries.get("Asia").add("India");
        countries.get("Asia").add("Indonesia");
        countries.get("Asia").add("Iran");
        countries.get("Asia").add("Iraq");
        countries.get("Asia").add("Israel");
        countries.get("Asia").add("Japan");
        countries.get("Asia").add("Jordan");
        countries.get("Asia").add("Kazakhstan");
        countries.get("Asia").add("Korea, North");
        countries.get("Asia").add("Korea, South");
        countries.get("Asia").add("Kuwait");
        countries.get("Asia").add("Kyrgyzstan");
        countries.get("Asia").add("Laos");
        countries.get("Asia").add("Lebanon");
        countries.get("Asia").add("Malaysia");
        countries.get("Asia").add("Maldives");
        countries.get("Asia").add("Mongolia");
        countries.get("Asia").add("Nepal");
        countries.get("Asia").add("Oman");
        countries.get("Asia").add("Pakistan");
        countries.get("Asia").add("Philippines");
        countries.get("Asia").add("Qatar");
        countries.get("Asia").add("Russian Federation");
        countries.get("Asia").add("Saudi Arabia");
        countries.get("Asia").add("Singapore");
        countries.get("Asia").add("Sri Lanka");
        countries.get("Asia").add("Syria");
        countries.get("Asia").add("Tajikistan");
        countries.get("Asia").add("Thailand");
        countries.get("Asia").add("Turkey");
        countries.get("Asia").add("Turkmenistan");
        countries.get("Asia").add("United Arab Emirates");
        countries.get("Asia").add("Uzbekistan");
        countries.get("Asia").add("Vietnam");
        countries.get("Asia").add("Yemen");

        countries.put("SouthAM", new ArrayList<String>());
        countries.get("SouthAM").add("Argentina");
        countries.get("SouthAM").add("Bolivia");
        countries.get("SouthAM").add("Brazil");
        countries.get("SouthAM").add("Chile");
        countries.get("SouthAM").add("Colombia");
        countries.get("SouthAM").add("Ecuador");
        countries.get("SouthAM").add("Guyana");
        countries.get("SouthAM").add("Paraguay");
        countries.get("SouthAM").add("Peru");
        countries.get("SouthAM").add("Suriname");
        countries.get("SouthAM").add("Uruguay");
        countries.get("SouthAM").add("Venezuela");

        countries.put("Africa", new ArrayList<String>());
        countries.get("Africa").add("Algeria");
        countries.get("Africa").add("Angola");
        countries.get("Africa").add("Benin");
        countries.get("Africa").add("Botswana");
        countries.get("Africa").add("Burkina");
        countries.get("Africa").add("Burundi");
        countries.get("Africa").add("Cameroon");
        countries.get("Africa").add("Cape Verde");
        countries.get("Africa").add("Central African Republic");
        countries.get("Africa").add("Chad");
        countries.get("Africa").add("Comoros");
        countries.get("Africa").add("Congo");
        countries.get("Africa").add("Congo, Democratic Republic of");
        countries.get("Africa").add("Djibouti");
        countries.get("Africa").add("Egypt");
        countries.get("Africa").add("Equatorial Guinea");
        countries.get("Africa").add("Eritrea");
        countries.get("Africa").add("Ethiopia");
        countries.get("Africa").add("Gabon");
        countries.get("Africa").add("Gambia");
        countries.get("Africa").add("Ghana");
        countries.get("Africa").add("Guinea");
        countries.get("Africa").add("Guinea-Bissau");
        countries.get("Africa").add("Ivory Coast");
        countries.get("Africa").add("Kenya");
        countries.get("Africa").add("Lesotho");
        countries.get("Africa").add("Liberia");
        countries.get("Africa").add("Libya");
        countries.get("Africa").add("Madagascar");
        countries.get("Africa").add("Malawi");
        countries.get("Africa").add("Mali");
        countries.get("Africa").add("Mauritania");
        countries.get("Africa").add("Mauritius");
        countries.get("Africa").add("Morocco");
        countries.get("Africa").add("Mozambique");
        countries.get("Africa").add("Namibia");
        countries.get("Africa").add("Niger");
        countries.get("Africa").add("Nigeria");
        countries.get("Africa").add("Rwanda");
        countries.get("Africa").add("Sao Tome and Principe");
        countries.get("Africa").add("Senegal");
        countries.get("Africa").add("Seychelles");
        countries.get("Africa").add("Sierra Leone");
        countries.get("Africa").add("Somalia");
        countries.get("Africa").add("South Africa");
        countries.get("Africa").add("South Sudan");
        countries.get("Africa").add("Sudan");
        countries.get("Africa").add("Swaziland");
        countries.get("Africa").add("Tanzania");
        countries.get("Africa").add("Togo");
        countries.get("Africa").add("Tunisia");
        countries.get("Africa").add("Uganda");
        countries.get("Africa").add("Zambia");
        countries.get("Africa").add("Zimbabwe");

        countries.put("Australia", new ArrayList<String>());
        countries.get("Australia").add("Australia");
        countries.get("Australia").add("Fiji");
        countries.get("Australia").add("Kiribati");
        countries.get("Australia").add("Marshall Islands");
        countries.get("Australia").add("Micronesia");
        countries.get("Australia").add("Nauru");
        countries.get("Australia").add("New Zealand");
        countries.get("Australia").add("Palau");
        countries.get("Australia").add("Papua New Guinea");
        countries.get("Australia").add("Samoa");
        countries.get("Australia").add(" Solomon Islands");
        countries.get("Australia").add("Tonga");
        countries.get("Australia").add("Tuvalu");
        countries.get("Australia").add("Vanuatu");
    }



}
