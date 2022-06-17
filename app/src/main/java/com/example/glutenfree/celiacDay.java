package com.example.glutenfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;

public class celiacDay extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AlertDialog.Builder adb;
    AlertDialog ad;
    public DrawerLayout drawerLayout;
    public EndDrawerToggle drawerToggle;
    NavigationView navigationView;
    Toolbar toolbar;
    CountdownView countdownView;
    SimpleDateFormat sdf;
    String countDate;
    SliderView sliderView;
    int[] images = {R.drawable.celiac_day1,
            R.drawable.celiac_day2,R.drawable.celiac_day3,R.drawable.celiac_day4,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_celiac_day);
            sliderView = findViewById(R.id.image_slider);
            sliderView = findViewById(R.id.image_slider);
            SliderAdapter2 sliderAdapter = new SliderAdapter2(images);
            sliderView.setSliderAdapter(sliderAdapter);
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

            countdownView = findViewById(R.id.countdownView);
            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            countDate = "13-09-2022 00:00:00";
            Date now = new Date();
            try {
                Date date = sdf.parse(countDate);
                long currentTime = now.getTime();
                long celiacDayDate = date.getTime();
                long countdownToCeliacDay = celiacDayDate - currentTime;

                countdownView.start(countdownToCeliacDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent i = new Intent(celiacDay.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_register) {
            Intent i = new Intent(celiacDay.this, Registration.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_login) {
            Intent i = new Intent(celiacDay.this, Login.class);
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
                    Intent intent = new Intent(celiacDay.this, MainActivity.class);
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
            Intent i = new Intent(celiacDay.this, Profile.class);
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
            Intent i = new Intent(celiacDay.this, information.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_links) {
            Intent i = new Intent(celiacDay.this, links.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_videos) {
            Intent i = new Intent(celiacDay.this, videos.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_celiac_day) {
            Intent i = new Intent(celiacDay.this, celiacDay.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_bakeries) {
            Intent i = new Intent(celiacDay.this, bakeries.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_restaurants) {
            Intent i = new Intent(celiacDay.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_shops) {
            Intent i = new Intent(celiacDay.this, MainActivity.class);
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
        Dialog dialog=new Dialog(this);
        registerReceiver(new WifiReceiver(dialog,toolbar), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(new WifiReceiver(dialog,toolbar), new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(new WifiReceiver());
    }

}