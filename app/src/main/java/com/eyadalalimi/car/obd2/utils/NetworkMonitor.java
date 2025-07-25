package com.eyadalalimi.car.obd2.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkMonitor extends BroadcastReceiver {

    public interface ConnectionListener {
        void onConnected();
    }

    private ConnectionListener listener;

    public NetworkMonitor(ConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            boolean isConnected = info != null && info.isConnected();

            if (isConnected && listener != null) {
                listener.onConnected();
            }
        }
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, filter);
    }

    public void unregister(Context context) {
        try {
            context.unregisterReceiver(this);
        } catch (IllegalArgumentException ignored) {}
    }
}
