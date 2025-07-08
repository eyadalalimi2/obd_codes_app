package com.proapp.obdcodes.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)) {
            SyncManager.downloadCodes(context, new SyncManager.SyncCallback() {
                @Override
                public void onSuccess(int updatedCount) {
                    // نجاح التحديث بالخلفية، يمكن تسجيل لوج أو تجاهله
                }

                @Override
                public void onFailure(String error) {
                    // يمكن تجاهل الفشل أو تسجيله فقط
                }
            });
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}
