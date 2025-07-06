package com.proapp.obdcodes.ui.notifications;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.databinding.ActivityNotificationsBinding;
import com.proapp.obdcodes.viewmodel.NotificationStateViewModel;
import com.proapp.obdcodes.viewmodel.NotificationViewModel;

public class NotificationsActivity extends AppCompatActivity {
    private ActivityNotificationsBinding binding;
    private NotificationViewModel vm;
    private NotificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // تهيئة الشريط العلوي
        setSupportActionBar(binding.toolbarNotifications);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // تهيئة ViewModel
        vm = new ViewModelProvider(this).get(NotificationViewModel.class);

        // تهيئة قائمة الإشعارات
        adapter = new NotificationsAdapter();
        adapter.setListener(new NotificationsAdapter.Listener() {
            @Override
            public void onMarkRead(long id) {
                vm.markRead(id).observe(NotificationsActivity.this, ok -> {
                    Toast.makeText(NotificationsActivity.this,
                            ok ? R.string.marked_read : R.string.error, Toast.LENGTH_SHORT).show();
                    vm.refreshNotifications(); // إعادة تحميل
                });
            }

            @Override
            public void onDelete(long id) {
                vm.delete(id).observe(NotificationsActivity.this, ok -> {
                    Toast.makeText(NotificationsActivity.this,
                            ok ? R.string.deleted : R.string.error, Toast.LENGTH_SHORT).show();
                    vm.refreshNotifications(); // إعادة تحميل
                });
            }
        });

        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotifications.setAdapter(adapter);

        // متابعة عدد الإشعارات وعرضها
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
}
