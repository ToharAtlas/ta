package com.example.glutenfree;

import androidx.annotation.NonNull;
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
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class links extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        AlertDialog.Builder adb;
        AlertDialog ad;
        Toolbar toolbar;
public DrawerLayout drawerLayout;
public EndDrawerToggle drawerToggle;
        NavigationView navigationView;
        ImageView link1,link2,link3,link4,link5,link6,link7,link8,link9,link10,link11,link12,link13,link14,link15;

@Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        link1 = findViewById(R.id.link1_shnider);
        link1.setOnClickListener(this);
        link2 = findViewById(R.id.link2_clalit);
        link2.setOnClickListener(this);
        link3 = findViewById(R.id.link3_celiacrights);
        link3.setOnClickListener(this);
        link4 = findViewById(R.id.link4_wikipedia);
        link4.setOnClickListener(this);
        link5 = findViewById(R.id.link5_ugionet);
        link5.setOnClickListener(this);
        link6 = findViewById(R.id.link6_10min);
        link6.setOnClickListener(this);
        link7 = findViewById(R.id.link7_mako);
        link7.setOnClickListener(this);
        link8 = findViewById(R.id.link8_wheatout);
        link8.setOnClickListener(this);
        link9 = findViewById(R.id.link9_helimaman);
        link9.setOnClickListener(this);
        link10 = findViewById(R.id.link10_celiacrights);
        link10.setOnClickListener(this);
        link11 = findViewById(R.id.link11_dietmaster);
        link11.setOnClickListener(this);
        link12 = findViewById(R.id.link12_focus);
        link12.setOnClickListener(this);
        link13 = findViewById(R.id.link13_book1);
        link13.setOnClickListener(this);
        link14 = findViewById(R.id.link14_book2);
        link14.setOnClickListener(this);
        link15 = findViewById(R.id.link15_book3);
        link15.setOnClickListener(this);

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
        }

        @Override
        public void onClick(View v) {
        if (v == link1) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.schneider.org.il/?CategoryID=832&ArticleID=2439"));
        startActivity(intent);
        }
        if (v == link2) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.clalit.co.il/he/medical/medical_diagnosis/Pages/celiac_disease.aspx"));
        startActivity(intent);
        }
        if (v == link3) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://celiacrights.org.il/"));
        startActivity(intent);
        }
        if (v == link4) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://he.wikipedia.org/wiki/%D7%A6%D7%9C%D7%99%D7%90%D7%A7"));
        startActivity(intent);
        }
        if (v == link5) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.oogio.net/category/%D7%A2%D7%95%D7%93/%D7%9C%D7%9C%D7%90-%D7%92%D7%9C%D7%95%D7%98%D7%9F/"));
        startActivity(intent);
        }
        if (v == link6) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.10dakot.co.il/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%9C%D7%9C%D7%90-%D7%92%D7%9C%D7%95%D7%98%D7%9F/"));
        startActivity(intent);
        }
        if (v == link7) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.mako.co.il/food-recipes/recipes_column-gluten-free"));
        startActivity(intent);
        }
        if (v == link8) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.wheatout.co.il/%d7%9e%d7%aa%d7%9b%d7%95%d7%a0%d7%99%d7%9d/"));
        startActivity(intent);
        }
        if (v == link9) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.heli-group.co.il/menu/%D7%AA%D7%A4%D7%A8%D7%99%D7%98-%D7%9C%D7%9C%D7%90-%D7%92%D7%9C%D7%95%D7%98%D7%9F-1800-%D7%A7%D7%9C%D7%95%D7%A8%D7%99%D7%95%D7%AA/"));
        startActivity(intent);
        }
        if (v == link10) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://celiacrights.org.il/the-celiac/relaxing-after-work"));
        startActivity(intent);
        }
        if (v == link11) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.dietmaster.co.il/%D7%93%D7%99%D7%90%D7%98%D7%94-%D7%9C%D7%9C%D7%90-%D7%92%D7%9C%D7%95%D7%98%D7%9F/"));
        startActivity(intent);
        }
        if (v == link12) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.focus.co.il/infopage.asp?page=1184"));
        startActivity(intent);
        }
        if (v == link13) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://simania.co.il/bookdetails.php?item_id=969586"));
        startActivity(intent);
        }
        if (v == link14) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://simania.co.il/bookdetails.php?item_id=968395"));
        startActivity(intent);
        }
        if (v == link15) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://simania.co.il/bookdetails.php?item_id=495199"));
        startActivity(intent);
        }
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
        Intent i = new Intent(links.this, MainActivity.class);
        startActivity(i);
        finish();
        }
        if (id == R.id.nav_register) {
        Intent i = new Intent(links.this, Registration.class);
        startActivity(i);
        finish();
        }
        if (id == R.id.nav_login) {
        Intent i = new Intent(links.this, Login.class);
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
        Intent intent = new Intent(links.this, MainActivity.class);
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
        Intent i = new Intent(links.this, Profile.class);
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
        Intent i = new Intent(links.this, information.class);
        startActivity(i);
        finish();
        }
        if (id == R.id.nav_videos) {
        Intent i = new Intent(links.this, videos.class);
        startActivity(i);
        finish();
        }
        if (id == R.id.nav_links) {
        Intent i = new Intent(links.this, links.class);
        startActivity(i);
        finish();
        }
        if (id == R.id.nav_celiac_day) {
        Intent i = new Intent(links.this, celiacDay.class);
        startActivity(i);
        finish();
        }
        if (id == R.id.nav_bakeries) {
        Intent i = new Intent(links.this, bakeries.class);
        startActivity(i);
        finish();
        }
        if (id == R.id.nav_restaurants) {
        Intent i = new Intent(links.this, MainActivity.class);
        startActivity(i);
        finish();
        }
        if (id == R.id.nav_shops) {
        Intent i = new Intent(links.this, MainActivity.class);
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