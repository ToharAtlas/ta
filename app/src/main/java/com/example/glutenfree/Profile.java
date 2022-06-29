package com.example.glutenfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    Button button_logout, button_update, button_addBusiness;
    ImageView profileImage;
    TextView textViewUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ProgressDialog p;
    DatabaseReference databaseReference = firebaseDatabase.getReference("users");
    StorageReference storageReference;
    String picname = "";
    Uri uri;

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
            setContentView(R.layout.activity_profile);
            sharedPreferences = getSharedPreferences("remember_me1", 0);
            button_logout = findViewById(R.id.button_logout);
            button_logout.setOnClickListener(this);
            button_update = findViewById(R.id.button_update);
            button_update.setOnClickListener(this);
            button_addBusiness = findViewById(R.id.button_addBusiness);
            button_addBusiness.setOnClickListener(this);
            button_addBusiness.setVisibility(View.INVISIBLE);
            profileImage = findViewById(R.id.profileImage);
            if (sharedPreferences.getString("profileType", "").equals("true"))
                button_addBusiness.setVisibility(View.VISIBLE);
            if (sharedPreferences.getString("profilePic", "").equals("0")) {
                profileImage.setImageResource(R.drawable.icon_man);
            } else {
                storageReference = FirebaseStorage.getInstance().getReference("image/" + sharedPreferences.getString("mail", "").replace('.', ' '));
                storageReference = storageReference.child(sharedPreferences.getString("profilePic", ""));
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(Profile.this).load(uri).into(profileImage); //הפיקאסו שם את הuri בתוך imageView
                    }
                });
            }
            profileImage.setOnClickListener(this);
            textViewUser = findViewById(R.id.textViewUser);
            textViewUser.setText("שלום " + sharedPreferences.getString("firstName", "") + " " + sharedPreferences.getString("lastName", ""));
            firebaseAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        //  View headerView = navigationView.inflateHeaderView(R.layout.header);
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
        if (profileImage == v) {
            adb = new AlertDialog.Builder(this);
            adb.setTitle("בחירת תמונה");
            adb.setMessage("את/ה עומד/ת לבחור תמונה מהגלריה");
            adb.setCancelable(true);
            adb.setPositiveButton("גלריה", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int i) {
                    Intent intent = new Intent();
                    intent.setType("image/*");

                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);

                }
            });
            adb.setNeutralButton("ביטול", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int i) {

                }
            });
            ad = adb.create();
            ad.show();
        }
        if (v == button_logout) {
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
                    Intent intent = new Intent(Profile.this, MainActivity.class);
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
        if (v == button_update) {
            Intent i = new Intent(Profile.this, UpdateUser.class);
            startActivity(i);
        }
        if (v == button_addBusiness) {
            Intent i = new Intent(Profile.this, addBusiness.class);
            startActivity(i);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            uri = data.getData();
            profileImage.setImageURI(uri);
            if (sharedPreferences.getString("profilePic", "").equals("0")) {
                ContentResolver cR = getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String t = mime.getExtensionFromMimeType(cR.getType(uri));
                picname = System.currentTimeMillis() + "." + t;
                databaseReference.child(sharedPreferences.getString("key", "")).child("profileImage").setValue(picname);
                editor = sharedPreferences.edit();
                editor.putString("profilePic", picname);
                editor.commit();
            } else {
                picname = sharedPreferences.getString("profilePic", "");
            }
            storageReference = FirebaseStorage.getInstance().getReference("image/" + sharedPreferences.getString("mail", "").replace('.', ' '));
            storageReference = storageReference.child(picname);
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent i = new Intent(Profile.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_register) {
            Intent i = new Intent(Profile.this, Registration.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_login) {
            Intent i = new Intent(Profile.this, Login.class);
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
                    Intent intent = new Intent(Profile.this, MainActivity.class);
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
            Intent i = new Intent(Profile.this, Profile.class);
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
            Intent i = new Intent(Profile.this, information.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_links) {
            Intent i = new Intent(Profile.this, links.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_videos) {
            Intent i = new Intent(Profile.this, videos.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_celiac_day) {
            Intent i = new Intent(Profile.this, celiacDay.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_bakeries) {
            Intent i = new Intent(Profile.this, bakeries.class);
            i.putExtra("bakeries", "bakeries");
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_restaurants) {
            Intent i = new Intent(Profile.this, bakeries.class);
            i.putExtra("restaurants", "restaurants");
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_shops) {
            Intent i = new Intent(Profile.this, bakeries.class);
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