package com.example.glutenfree;

import androidx.annotation.NonNull;
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
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateUser extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    TextInputEditText FirstName, LastName;
    Button button;
    Switch profileType;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    String firstName = "", lastName = "", password = "", email = "";
    ProgressDialog p;
    Toolbar toolbar;
    AlertDialog.Builder adb;
    AlertDialog ad;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public DrawerLayout drawerLayout;
    public EndDrawerToggle drawerToggle;
    NavigationView navigationView;
    check_Input check = new check_Input(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        FirstName = findViewById(R.id.etFirstName);
        LastName = findViewById(R.id.etLastName);
        button = findViewById(R.id.send);
        button.setOnClickListener(this);
        profileType = (Switch) findViewById(R.id.profileType);
        toolbar = findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences("remember_me1", 0);
        databaseReference = firebaseDatabase.getReference("users").child(sharedPreferences.getString("key", ""));
        editor = sharedPreferences.edit();
        email = sharedPreferences.getString("mail", "");
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
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
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirstName.setHint(dataSnapshot.child("firstName").getValue().toString());
                LastName.setHint(dataSnapshot.child("lastName").getValue().toString());
                //     Password.setHint( dataSnapshot.child("password").getValue().toString());
                //    Email.setHint( dataSnapshot.child("email").getValue().toString());
                profileType.setChecked(Boolean.parseBoolean(dataSnapshot.child("profileType").getValue().toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UpdateUser.this, "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                p.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            try {
                firstName = FirstName.getText().toString();
                lastName = LastName.getText().toString();
                check.setInputEditText(FirstName);
                if (!check.checkName(firstName)) {
                    throw new Exception();
                }
                check.setInputEditText(LastName);
                if (!check.checkName(lastName)) {
                    throw new Exception();
                }

            } catch (Exception e) {
                if (e.getMessage() != null)
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            p = new ProgressDialog(this);
            p.setMessage("עדכון משתמש מתבצע כעת");
            p.show();
            databaseReference.child("firstName").setValue(firstName);
            databaseReference.child("lastName").setValue(lastName);
            databaseReference.child("profileType").setValue(profileType.isChecked());
            editor.putString("firstName", firstName.toString());
            editor.putString("lastName", lastName.toString());
            editor.putString("profileType", profileType.isChecked() + "");
            editor.commit();
            Intent intent = new Intent(UpdateUser.this, Profile.class);
            startActivity(intent);
            finish();


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent i = new Intent(UpdateUser.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_register) {
            Intent i = new Intent(UpdateUser.this, Registration.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_login) {
            Intent i = new Intent(UpdateUser.this, Login.class);
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
                    Intent intent = new Intent(UpdateUser.this, MainActivity.class);
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
            Intent i = new Intent(UpdateUser.this, Profile.class);
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
            Intent i = new Intent(UpdateUser.this, information.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_videos) {
            Intent i = new Intent(UpdateUser.this, videos.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_links) {
            Intent i = new Intent(UpdateUser.this, links.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_celiac_day) {
            Intent i = new Intent(UpdateUser.this, celiacDay.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_bakeries) {
            Intent i = new Intent(UpdateUser.this, bakeries.class);
            i.putExtra("bakeries", "bakeries");
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_restaurants) {
            Intent i = new Intent(UpdateUser.this, bakeries.class);
            i.putExtra("restaurants", "restaurants");
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_shops) {
            Intent i = new Intent(UpdateUser.this, bakeries.class);
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