package com.example.glutenfree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Registration extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, OnCompleteListener<AuthResult>, OnFailureListener, OnSuccessListener<UploadTask.TaskSnapshot> {
    TextInputEditText FirstName, LastName, Password, Email;
    Switch profileType;
    Button button;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    ProgressDialog p;
    DatabaseReference databaseReference = firebaseDatabase.getReference("users");
    String firstname = "", lastname = "", password = "", mail = "", picname = "";
    Toolbar toolbar;
    AlertDialog.Builder adb;
    AlertDialog ad;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public DrawerLayout drawerLayout;
    public EndDrawerToggle drawerToggle;
    NavigationView navigationView;
    check_Input check = new check_Input(this);

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registration);
            FirstName = findViewById(R.id.etFirstName);
            LastName = findViewById(R.id.etLastName);
            Password = findViewById(R.id.etPass);
            Email = findViewById(R.id.etEmail);
            toolbar = findViewById(R.id.toolbar);
            profileType = (Switch) findViewById(R.id.profileType);
            button = findViewById(R.id.send);
            button.setOnClickListener(this);
            firebaseAuth = FirebaseAuth.getInstance();
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
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            try {
                firstname = FirstName.getText().toString();
                lastname = LastName.getText().toString();
                password = Password.getText().toString();
                mail = Email.getText().toString();
                check.setInputEditText(FirstName);
                if (!check.checkName(firstname)) {
                    throw new Exception();
                }
                check.setInputEditText(LastName);
                if (!check.checkName(lastname)) {
                    throw new Exception();
                }
                check.setInputEditText(Password);
                if (!check.checkPass(password)) {
                    throw new Exception();
                }
                check.setInputEditText(Email);
                if (!check.checkMail(mail)) {
                    throw new Exception();
                }
                p = new ProgressDialog(this);
                p.setMessage("הרשמה מתבצעת כעת");
                p.show();
                firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(this).addOnFailureListener(this);


            } catch (Exception e) {
                if (e.getMessage() != null)
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            databaseReference = firebaseDatabase.getReference("users").push();
            databaseReference.setValue(new User(FirstName.getText().toString(), LastName.getText().toString(), Password.getText().toString(), Email.getText().toString(), "0", databaseReference.getKey(), profileType.isChecked() + ""));
            p.dismiss();
            Toast.makeText(Registration.this, "ההרשמה הצליחה!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Registration.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(Registration.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        p.dismiss();
    }

    public void createUser() {
        p = new ProgressDialog(this);
        p.setMessage("הרשמה");
        p.show();
        //  if (isValidate())
        firebaseAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString());
    }


    private boolean isValidate() {
        if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
            Email.setError("אימייל לא רשום בצורה תקינה");
            Email.setFocusable(true);
            return false;
        } else if (Password.getText().toString().length() < 4) {
            Password.setError("סיסמה חייבת להכיל לפחות 4 תווים");
            Password.setFocusable(true);
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent i = new Intent(Registration.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_register) {
            Intent i = new Intent(Registration.this, Registration.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_login) {
            Intent i = new Intent(Registration.this, Login.class);
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
                    Intent intent = new Intent(Registration.this, MainActivity.class);
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
            Intent i = new Intent(Registration.this, Profile.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_inform) {
            Intent i = new Intent(Registration.this, information.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_videos) {
            Intent i = new Intent(Registration.this, videos.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_links) {
            Intent i = new Intent(Registration.this,links.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_celiac_day) {
            Intent i = new Intent(Registration.this, celiacDay.class);
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_bakeries) {
            Intent i = new Intent(Registration.this, bakeries.class);
            i.putExtra("bakeries", "bakeries");
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_restaurants) {
            Intent i = new Intent(Registration.this, bakeries.class);
            i.putExtra("restaurants", "restaurants");
            startActivity(i);
            finish();
        }
        if (id == R.id.nav_shops) {
            Intent i = new Intent(Registration.this, bakeries.class);
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