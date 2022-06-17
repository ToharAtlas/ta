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
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class addBusiness extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    TextView textType;
    String chosen;
    Spinner spinner;
    ArrayList<String> businessType;
    Switch isDelivering;
    String Delivering, editedVideo;
    String Type, Name, Description, Address,AddressCity, Opening_hours, Site_link, Facebook_link, Prices, Delivery_services, Phone, Video, Key;
    TextInputLayout shippingServices;
    TextInputEditText businessName, businessAddress,businessAddressCity, businessDescription, openingHours, siteLink, facebookLink, prices, phone, video;
    Button picturesPicker, send;
    ArrayList<String> StringPictures;
    ArrayList<Uri> UriPictures;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ProgressDialog p;
    DatabaseReference databaseReference = firebaseDatabase.getReference("business");
    StorageReference storageReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AlertDialog.Builder adb;
    AlertDialog ad;
    Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public EndDrawerToggle drawerToggle;
    NavigationView navigationView;
    check_Input check = new check_Input(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        textType = findViewById(R.id.textType);
        spinner = findViewById(R.id.type);
        businessType = new ArrayList<>();
        businessType.add("בחר");
        businessType.add("מסעדה/ בית קפה");
        businessType.add("מאפיה/קונדיטוריה");
        businessType.add("חנות מוצרים");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, businessType);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!((TextView) view).getText().toString().equals("בחר")) {
                    textType.setText("סוג העסק: " + ((TextView) view).getText());
                    chosen = ((TextView) view).getText().toString();
                } else
                    textType.setText("סוג העסק: " + ((TextView) view).getText());
                Toast.makeText(addBusiness.this, "יש לבחור את סוג העסק!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        businessName = findViewById(R.id.businessName);
        businessDescription = findViewById(R.id.businessDescription);
        businessAddress = findViewById(R.id.businessAddress);
        businessAddressCity = findViewById(R.id.businessAddressCity);
        openingHours = findViewById(R.id.openingHours);
        siteLink = findViewById(R.id.siteLink);
        facebookLink = findViewById(R.id.facebookLink);
        prices = findViewById(R.id.prices);
        businessName = findViewById(R.id.businessName);
        businessDescription = findViewById(R.id.businessDescription);
        openingHours = findViewById(R.id.openingHours);
        siteLink = findViewById(R.id.siteLink);
        facebookLink = findViewById(R.id.facebookLink);
        prices = findViewById(R.id.prices);
        shippingServices = findViewById(R.id.deliveryServices);
        isDelivering = findViewById(R.id.isDelivering);
        shippingServices.setVisibility(View.INVISIBLE);
        Delivering = "false";
        isDelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDelivering.isChecked()) {
                    shippingServices.setVisibility(View.VISIBLE);
                    Delivering = shippingServices.getEditText().getText().toString();
                } else {
                    shippingServices.setVisibility(View.INVISIBLE);
                    Delivering = "false";
                }
            }
        });
        phone = findViewById(R.id.phone);
        picturesPicker = findViewById(R.id.picturesPicker);
        picturesPicker.setOnClickListener(this);
        StringPictures = new ArrayList<>();
        UriPictures = new ArrayList<>();
        video = findViewById(R.id.video);
      /*  editedVideo = video.getText().toString();
        for (int i = 0; i < editedVideo.length(); i++) {
            if (editedVideo.charAt(i) == '=')
                editedVideo = editedVideo.substring(i + 1);
        }*/
        send = findViewById(R.id.send);
        send.setOnClickListener(this);

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
        if (v == picturesPicker) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
        }
        if (v == send) {
            Name = businessName.getText().toString();
            Type = chosen;
            Description = businessDescription.getText().toString();
            Address = businessAddress.getText().toString();
            AddressCity = businessAddressCity.getText().toString();
            Opening_hours = openingHours.getText().toString();
            Site_link = siteLink.getText().toString();
            Facebook_link = facebookLink.getText().toString();
            Prices = prices.getText().toString();
            Delivery_services = Delivering;
            Phone = phone.getText().toString();
            Video= video.getText().toString();
            databaseReference = databaseReference.push();
            business b=new business(Type, Name, Description, Address,AddressCity, Opening_hours, Site_link, Facebook_link, Prices, Delivery_services, Phone, "0", StringPictures, Video, databaseReference.getKey(), new ArrayList<String>());
            for(int i = 0; i <UriPictures.size(); i++) {
                storageReference = FirebaseStorage.getInstance().getReference("business/"+Name);
                storageReference = storageReference.child(StringPictures.get(i));
                storageReference.putFile(UriPictures.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
            databaseReference.setValue(b);
            p = new ProgressDialog(this);
            p.setMessage("תהליך הוספת העסק למערכת מתבצעת כעת");
            p.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    p.dismiss();
                }
            }, 3000);
            Toast.makeText(addBusiness.this, "הוספת העסק הצליחה!", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(addBusiness.this,MainActivity.class);
            startActivity(intent);
            finish();
            /*try {
                check.setInputEditText(Password);
                if (!check.checkPass(password)) {
                    throw new Exception();
                }
                check.setInputEditText(Email);
                if (!check.checkMail(mail)){
                    throw new Exception();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // פעולה מתרחשת בעת החזרה לאקטיביטי הנוכחי (במקרה הזה בשל היציאה לגלריה)
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                UriPictures.add(data.getClipData().getItemAt(i).getUri());
                StringPictures.add(System.currentTimeMillis() + "." + getFileExtension(UriPictures.get(i)));
            }
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent i = new Intent(addBusiness.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_register) {
            Intent i = new Intent(addBusiness.this, Registration.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_login) {
            Intent i = new Intent(addBusiness.this, Login.class);
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
                    Intent intent = new Intent(addBusiness.this, MainActivity.class);
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
            Intent i = new Intent(addBusiness.this, Profile.class);
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
            Intent i = new Intent(addBusiness.this, information.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_links) {
            Intent i = new Intent(addBusiness.this,links.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_videos) {
            Intent i = new Intent(addBusiness.this, videos.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_celiac_day) {
            Intent i = new Intent(addBusiness.this, celiacDay.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_bakeries) {
            Intent i = new Intent(addBusiness.this, bakeries.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_restaurants) {
            Intent i = new Intent(addBusiness.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_shops) {
            Intent i = new Intent(addBusiness.this, MainActivity.class);
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