package com.example.glutenfree;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import androidx.appcompat.widget.Toolbar;

public class WifiReceiver extends BroadcastReceiver {
    private WifiManager wifiManager;
    private ConnectivityManager cm;
    private NetworkInfo networkInfo;
    boolean is_wifi;
    private Dialog dialog;
    private Toolbar toolbar;

    public WifiReceiver() {
    }

    public WifiReceiver(Dialog dialog, Toolbar toolbar) {
        this.dialog = dialog;
        this.toolbar = toolbar;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // throw new UnsupportedOperationException("Not yet implemented");
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        networkInfo = cm.getActiveNetworkInfo();
        if (wifiStateExtra == WifiManager.WIFI_STATE_ENABLED || (networkInfo != null && networkInfo.isConnected())) {
            is_wifi = true;
            toolbar.setBackgroundColor(context.getResources().getColor(R.color.cream2));
            dialog.dismiss();

        } else {
            dialog.setContentView(R.layout.dialog_wifi);
            dialog.setCancelable(false);
            if (!((Activity) context).isFinishing())
                dialog.show();
            toolbar.setBackgroundColor(context.getResources().getColor(R.color.cream2));
            is_wifi = false;
        }

    }
}