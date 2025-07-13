package com.proapp.obdcodes.ui.notifications;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.util.SubscriptionUtils;
import com.proapp.obdcodes.viewmodel.NotificationStateViewModel;
import com.proapp.obdcodes.viewmodel.NotificationViewModel;

public class NotificationsActivity extends BaseActivity {
    private NotificationViewModel vm;
    private NotificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);

        // حماية الميزة المدفوعة قبل أي تنفيذ للواجهة
        SubscriptionUtils.checkFeatureAccess(this, "SMART_NOTIFICATIONS", this::setupNotifications);
    }

    private void setupNotifications() {
        runOnUiThread(() -> {
            setActivityLayout(R.layout.activity_notifications);
            setTitle(getString(R.string.notifications));

            vm = new ViewModelProvider(this).get(NotificationViewModel.class);

            androidx.recyclerview.widget.RecyclerView recyclerView = findViewById(R.id.rv_notifications);
            adapter = new NotificationsAdapter();

            adapter.setListener(new NotificationsAdapter.Listener() {
                @Override
                public void onMarkRead(long id) {
                    vm.markRead(id).observe(NotificationsActivity.this, ok -> {
                        Toast.makeText(NotificationsActivity.this,
                                ok ? R.string.marked_read : R.string.error, Toast.LENGTH_SHORT).show();
                        vm.refreshNotifications();
                    });
                }

                @Override
                public void onDelete(long id) {
                    vm.delete(id).observe(NotificationsActivity.this, ok -> {
                        Toast.makeText(NotificationsActivity.this,
                                ok ? R.string.deleted : R.string.error, Toast.LENGTH_SHORT).show();
                        vm.refreshNotifications();
                    });
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            vm.getNotifications().observe(this, list -> {
                int previousCount = adapter.getItemCount();
                adapter.submitList(list);
                if (list != null && list.size() > previousCount) {
                    playNotificationSoundAndVibrate();
                }
            });

            // مسح الشارة
            NotificationStateViewModel stateVM = new ViewModelProvider(this).get(NotificationStateViewModel.class);
            stateVM.clearBadge();
        });
    }

    private void playNotificationSoundAndVibrate() {
        try {
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), sound);
            if (r != null) r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v != null && v.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(200);
            }
        }
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
