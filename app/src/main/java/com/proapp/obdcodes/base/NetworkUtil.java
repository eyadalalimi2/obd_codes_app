package com.proapp.obdcodes.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo active = cm.getActiveNetworkInfo();
            return active != null && active.isConnected();
        }
        return false;
    }
}
