package com.example.glutenfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AlertDialog.Builder adb;
    AlertDialog ad;
    Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public EndDrawerToggle drawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_register) {
            Intent i = new Intent(MainActivity.this, Registration.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_login) {
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_logout) {
            adb = new AlertDialog.Builder(this);
            adb.setTitle("??????????????");
            adb.setMessage("?????? ???????????? ?????????????");
            adb.setCancelable(false);
            adb.setPositiveButton("????", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int i) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    editor = sharedPreferences.edit();
                    editor.putBoolean("remember_me", false);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            adb.setNegativeButton("????", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int i) {
                }
            });
            ad = adb.create();
            ad.show();

        }
        if (id == R.id.nav_profile) {
            Intent i = new Intent(MainActivity.this, Profile.class);
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
            Intent i = new Intent(MainActivity.this, information.class);
            startActivity(i);
        }
        if (id == R.id.nav_videos) {
            Intent i = new Intent(MainActivity.this, videos.class);
            startActivity(i);
        }
        if (id == R.id.nav_links) {
            Intent i = new Intent(MainActivity.this, links.class);
            startActivity(i);
        }
        if (id == R.id.nav_celiac_day) {
            Intent i = new Intent(MainActivity.this, celiacDay.class);
            startActivity(i);
        }
        if (id == R.id.nav_bakeries) {
            Intent i = new Intent(MainActivity.this, bakeries.class);
            i.putExtra("bakeries", "bakeries");
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_restaurants) {
            Intent i = new Intent(MainActivity.this, bakeries.class);
            i.putExtra("restaurants", "restaurants");
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_shops) {
            Intent i = new Intent(MainActivity.this, bakeries.class);
            i.putExtra("shops", "shops");
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