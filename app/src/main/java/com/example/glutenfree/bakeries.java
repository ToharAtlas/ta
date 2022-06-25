package com.example.glutenfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class bakeries extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    TextView textFilter;
    Button search;
    Spinner filter;
    String chosen;
    Spinner cities;
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayList<String> businessFilter;
    AlertDialog.Builder adb;
    AlertDialog ad;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public DrawerLayout drawerLayout;
    public EndDrawerToggle drawerToggle;
    NavigationView navigationView;
    Toolbar toolbar;
    ArrayList<business> businesses = new ArrayList<>();
    ArrayList<business> byCity = new ArrayList<>();
    ArrayList<business> byDelivery = new ArrayList<>();
    ArrayList<business> byBoth = new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("business");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bakeries);
            search = findViewById(R.id.search);
            search.setOnClickListener(this);
            cities = findViewById(R.id.cities);
            cities.setVisibility(View.INVISIBLE);
            readCityData();
            ArrayAdapter<String> newadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list1);
            cities.setAdapter(newadapter);
            cities.setOnItemSelectedListener(this);
            filter = findViewById(R.id.filter);
            businessFilter = new ArrayList<>();
            businessFilter.add("בחר");
            businessFilter.add("סינון לפי עיר");
            businessFilter.add("מבצעים משלוחים");
            businessFilter.add("גם וגם");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, businessFilter);
            filter.setAdapter(dataAdapter);
            filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!((TextView) view).getText().toString().equals("בחר")) {
                        chosen = ((TextView) view).getText().toString();
                        if (((TextView) view).getText().toString().equals("סינון לפי עיר") || ((TextView) view).getText().toString().equals("גם וגם"))
                            cities.setVisibility(View.VISIBLE);
                    } else if (((TextView) view).getText().toString().equals("בחר")) {
                        Toast.makeText(bakeries.this, "יש לבחור דרך סינון", Toast.LENGTH_SHORT).show();
                        cities.setVisibility(View.INVISIBLE);
                    }
                    if (((TextView) view).getText().toString().equals("מבצעים משלוחים")) {
                        cities.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });


            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigationView);
            toolbar = findViewById(R.id.toolbar);
            View headerView = navigationView.inflateHeaderView(R.layout.header);
            sharedPreferences = getSharedPreferences("remember_me1", 0);
            if (sharedPreferences.getBoolean("remember_me", false))
                navigationView.inflateMenu(R.menu.menu_connected);
            else
                navigationView.inflateMenu(R.menu.main_menu);
            navigationView.setNavigationItemSelectedListener(this);
            drawerToggle = new EndDrawerToggle(drawerLayout, toolbar, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(drawerToggle);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot
                            singleSnapshot : snapshot.getChildren()) {
                        if (singleSnapshot.child("type").getValue().toString().equals("מאפיה/קונדיטוריה")) {
                            businesses.add(new business(
                                    singleSnapshot.child("type").getValue().toString(),
                                    singleSnapshot.child("name").getValue().toString(),
                                    singleSnapshot.child("description").getValue().toString(),
                                    singleSnapshot.child("address").getValue().toString(),
                                    singleSnapshot.child("addressCity").getValue().toString(),
                                    singleSnapshot.child("opening_hours").getValue().toString(),
                                    singleSnapshot.child("site_link").getValue().toString(),
                                    singleSnapshot.child("facebook_link").getValue().toString(),
                                    singleSnapshot.child("prices").getValue().toString(),
                                    singleSnapshot.child("delivery_services").getValue().toString(),
                                    singleSnapshot.child("phone").getValue().toString(),
                                    singleSnapshot.child("rating").getValue().toString(),
                                    (ArrayList<String>) singleSnapshot.child("pictures").getValue(),
                                    singleSnapshot.child("video").getValue().toString(),
                                    singleSnapshot.child("key").getValue().toString(),
                                    null));

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == search) {

            if (chosen.equals("סינון לפי עיר")) {
                String cityName = "";
                for (int j = 0; j < CityDataread.size() && CityDataread.get(j).getCity_Name().compareTo(cityName.trim()) <= 0; j++) {
                    cityName = CityDataread.get(j).getCity_Name();
                }
                for (int i = 0; i < businesses.size(); i++) {
                    if (CityDataread.get(i).getCity_Name().equals(businesses.get(i).getAddressCity())) {
                        byCity.add(businesses.get(i));
                    }
                    businessAdapter businessAdapter = new businessAdapter(byCity, bakeries.this);
                }

            }
            if (chosen.equals("מבצעים משלוחים")) {

                for (int i = 0; i < businesses.size(); i++) {
                    if (!businesses.get(i).getDelivery_services().equals("false")) {
                        byDelivery.add(businesses.get(i));
                    }
                }
                businessAdapter businessAdapter = new businessAdapter(byDelivery, bakeries.this);
            }
            if (chosen.equals("גם וגם")) {
                String cityName = "";
                for (int j = 0; j < CityDataread.size() && CityDataread.get(j).getCity_Name().compareTo(cityName.trim()) <= 0; j++) {
                    cityName = CityDataread.get(j).getCity_Name();
                }
                for (int i = 0; i < businesses.size(); i++) {
                    if (CityDataread.get(i).getCity_Name().equals(businesses.get(i).getAddressCity()) && !businesses.get(i).getDelivery_services().equals(false)) {
                        byBoth.add(businesses.get(i));
                    }
                }
                businessAdapter businessAdapter = new businessAdapter(byBoth, bakeries.this);
            }
        }
    }

    private List<CityData> CityDataread = new ArrayList<>();

    private void readCityData() {
        InputStream is = getResources().openRawResource(R.raw.data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("windows-1255"))
        );
        String line = "";
        try {
            reader.readLine();
            while ((line = reader.readLine()) != null) {

                String[] tokens = line.split(",");
                CityData sample = new CityData();
                sample.setCity_Name(tokens[1].trim());
                sample.setId_Street(tokens[2]);
                sample.setStreet_name(tokens[3]);
                CityDataread.add(sample);
                if (!list1.contains(tokens[1]))
                    list1.add(tokens[1]);


            }
        } catch (IOException e) {
            Log.wtf("findData", "Error while reading data file" + line, e);
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == cities) {
            String item = list1.get(i);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent i = new Intent(bakeries.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_register) {
            Intent i = new Intent(bakeries.this, Registration.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_login) {
            Intent i = new Intent(bakeries.this, Login.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_logout) {
            adb = new AlertDialog.Builder(this);
            adb.setTitle("התנתקות");
            adb.setMessage("האם ברצונך להתנתק?");
            adb.setCancelable(false);
            adb.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int i) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    editor = sharedPreferences.edit();
                    editor.putBoolean("remember_me", false);
                    editor.commit();
                    Intent intent = new Intent(bakeries.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            adb.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int i) {
                }
            });
            ad = adb.create();
            ad.show();

        }
        if (id == R.id.nav_profile) {
            Intent i = new Intent(bakeries.this, Profile.class);
            startActivity(i);
            finish();
        }
        /*
        if (id == R.id.nav_info) {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }*/
        if (id == R.id.nav_inform) {
            Intent i = new Intent(bakeries.this, information.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_videos) {
            Intent i = new Intent(bakeries.this, videos.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_links) {
            Intent i = new Intent(bakeries.this, links.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_celiac_day) {
            Intent i = new Intent(bakeries.this, celiacDay.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_bakeries) {
            Intent i = new Intent(bakeries.this, bakeries.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_restaurants) {
            Intent i = new Intent(bakeries.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_shops) {
            Intent i = new Intent(bakeries.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Dialog dialog = new Dialog(this);
        registerReceiver(new WifiReceiver(dialog, toolbar), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(new WifiReceiver(dialog, toolbar), new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(new WifiReceiver());
    }

}