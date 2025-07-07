package com.proapp.obdcodes.ui.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.base.NetworkUtil;
import com.proapp.obdcodes.network.model.UserNotification;
import com.proapp.obdcodes.ui.TestActivity;
import com.proapp.obdcodes.ui.account.AccountActivity;
import com.proapp.obdcodes.ui.analytics.TrendingCodesActivity;
import com.proapp.obdcodes.ui.chat.ChatActivity;
import com.proapp.obdcodes.ui.compare.CompareCodesActivity;
import com.proapp.obdcodes.ui.diagnosis.SymptomDiagnosisActivity;
import com.proapp.obdcodes.ui.history.DiagnosisHistoryActivity;
import com.proapp.obdcodes.ui.home.HomeActivity;
import com.proapp.obdcodes.ui.menu.MenuActivity;
import com.proapp.obdcodes.ui.notifications.NotificationsActivity;
import com.proapp.obdcodes.ui.pdf.PdfReportActivity;
import com.proapp.obdcodes.ui.saved.SavedCodesActivity;
import com.proapp.obdcodes.ui.settings.SettingsActivity;
import com.proapp.obdcodes.viewmodel.NotificationStateViewModel;
import com.proapp.obdcodes.viewmodel.NotificationViewModel;
import com.proapp.obdcodes.viewmodel.UserViewModel;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private BottomNavigationView bottomNav;
    private NotificationViewModel notificationViewModel;
    private TextView badge;

    // Dialog for no-internet state
    private AlertDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prepare the no-internet dialog
        setupNoInternetDialog();

        // Inflate the base layout containing toolbar, drawer & bottom nav
        setContentView(R.layout.activity_base);

        // Initialize notification ViewModel
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);

        // اجعل الـ Toolbar يعرض عنوان الـ Activity كما هو في android:label
        if (getSupportActionBar() != null) {
            // يقوم getTitle() بإرجاع قيمة android:label من الـ Manifest
            getSupportActionBar().setTitle(getTitle());
        }


        // Setup drawer & navigation view
        drawer = findViewById(R.id.base_drawer);
        NavigationView navView = findViewById(R.id.base_nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        // Header binding
        View header = navView.getHeaderView(0);
        ImageView imgHeader = header.findViewById(R.id.nav_header_image);
        TextView tvHeaderName  = header.findViewById(R.id.nav_header_name);
        TextView tvHeaderEmail = header.findViewById(R.id.nav_header_email);

        UserViewModel headerVm = new ViewModelProvider(this).get(UserViewModel.class);
        headerVm.getUserProfile().observe(this, user -> {
            if (user != null) {
                String imageUrl = "https://obdcode.xyz/storage/" + user.getProfileImage();
                Glide.with(this)
                        .load(imageUrl)
                        .circleCrop()
                        .placeholder(R.drawable.profile_sample)
                        .error(R.drawable.profile_sample)
                        .into(imgHeader);
                tvHeaderName.setText(user.getUsername());
                tvHeaderEmail.setText(user.getEmail());
            }
        });

        header.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, AccountActivity.class));
        });

        // Setup bottom navigation
        bottomNav = findViewById(R.id.base_bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            animateBottomNav(item);
            Intent intent = null;
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_saved) {
                intent = new Intent(this, HomeActivity.class);
                intent = new Intent(this, SavedCodesActivity.class);
            } else if (id == R.id.nav_account) {
                intent = new Intent(this, AccountActivity.class);
            } else if (id == R.id.nav_menu) {
                intent = new Intent(this, MenuActivity.class);
            }
            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            }
            return true;
        });
    }

    /**
     * Prepares the AlertDialog that shows when there's no internet connection.
     */
    private void setupNoInternetDialog() {
        View v = getLayoutInflater().inflate(R.layout.dialog_no_internet, null);

        AlertDialog.Builder b = new AlertDialog.Builder(this)
                .setView(v)
                .setCancelable(false);

        noInternetDialog = b.create();

        Button retry = v.findViewById(R.id.btn_retry);
        retry.setOnClickListener(x -> {
            noInternetDialog.dismiss();
            onResume();
        });

        Button close = v.findViewById(R.id.btn_close);
        close.setOnClickListener(x ->
                noInternetDialog.dismiss());


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!NetworkUtil.isConnected(this)) {
            noInternetDialog.show();
        } else if (noInternetDialog.isShowing()) {
            noInternetDialog.dismiss();
        }

        notificationViewModel.refreshNotifications();
    }


    @SuppressLint("RestrictedApi")
    private void animateBottomNav(@NonNull MenuItem selectedItem) {
        if (bottomNav == null) return;

        View navChild = bottomNav.getChildAt(0);
        if (!(navChild instanceof BottomNavigationMenuView)) return;

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navChild;

        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView itemView =
                    (BottomNavigationItemView) menuView.getChildAt(i);
            View icon = itemView.findViewById(com.google.android.material.R.id.icon);

            if (icon == null) continue;

            if (itemView.getItemData().getItemId() == selectedItem.getItemId()) {
                icon.animate()
                        .scaleX(1.2f).scaleY(1.2f)
                        .alpha(1f)
                        .setDuration(200)
                        .start();
            } else {
                icon.animate()
                        .scaleX(1f).scaleY(1f)
                        .alpha(0.6f)
                        .setDuration(200)
                        .start();
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = null;
        int id = item.getItemId();
        if (id == R.id.nav_symptoms) {
            intent = new Intent(this, SymptomDiagnosisActivity.class);
        } else if (id == R.id.nav_compare) {
            intent = new Intent(this, CompareCodesActivity.class);
        } else if (id == R.id.nav_visuals) {
            intent = new Intent(this, MenuActivity.class);
        } else if (id == R.id.nav_history) {
            intent = new Intent(this, DiagnosisHistoryActivity.class);
        } else if (id == R.id.nav_pdf) {
            intent = new Intent(this, PdfReportActivity.class);
        } else if (id == R.id.nav_notifications) {
            intent = new Intent(this, NotificationsActivity.class);
        } else if (id == R.id.nav_trending) {
            intent = new Intent(this, TrendingCodesActivity.class);
        } else if (id == R.id.nav_ai_assistant) {
            intent = new Intent(this, ChatActivity.class);
        } else if (id == R.id.nav_dashboard) {
            intent = new Intent(this, TestActivity.class);
        } else if (id == R.id.nav_settings) {
            intent = new Intent(this, SettingsActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notifications, menu);

        MenuItem item = menu.findItem(R.id.action_notifications);
        View actionView = item.getActionView();

        ImageView icon = actionView.findViewById(R.id.icon_notification);
        badge = actionView.findViewById(R.id.tv_badge);

        NotificationViewModel vm = new ViewModelProvider(this).get(NotificationViewModel.class);
        vm.getNotifications().observe(this, list -> {
            long unreadCount = 0;
            if (list != null) {
                for (UserNotification n : list) {
                    if (n.getReadAt() == null) unreadCount++;
                }
            }
            if (unreadCount > 0) {
                badge.setVisibility(View.VISIBLE);
                badge.setText(String.valueOf(unreadCount));
            } else {
                badge.setVisibility(View.GONE);
            }
        });

        NotificationStateViewModel stateVM = new ViewModelProvider(this).get(NotificationStateViewModel.class);
        stateVM.getClearBadgeSignal().observe(this, clear -> {
            if (Boolean.TRUE.equals(clear)) {
                badge.setVisibility(View.GONE);
                stateVM.resetSignal();
            }
        });

        actionView.setOnClickListener(v -> onOptionsItemSelected(item));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Inflate a layout into the base content frame.
     */
    protected void setActivityLayout(int layoutResID) {
        FrameLayout container = findViewById(R.id.base_content_frame);
        getLayoutInflater().inflate(layoutResID, container, true);
    }
}
