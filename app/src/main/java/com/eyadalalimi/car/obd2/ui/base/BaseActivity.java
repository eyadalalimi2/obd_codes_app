package com.eyadalalimi.car.obd2.ui.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.base.NetworkUtil;
import com.eyadalalimi.car.obd2.network.model.UserNotification;
import com.eyadalalimi.car.obd2.ui.TestActivity;
import com.eyadalalimi.car.obd2.ui.account.AccountActivity;
import com.eyadalalimi.car.obd2.ui.analytics.TrendingCodesActivity;
import com.eyadalalimi.car.obd2.ui.cars.CarListActivity;
import com.eyadalalimi.car.obd2.ui.chat.ChatActivity;
import com.eyadalalimi.car.obd2.ui.compare.CompareCodesActivity;
import com.eyadalalimi.car.obd2.ui.diagnosis.SymptomDiagnosisActivity;
import com.eyadalalimi.car.obd2.ui.history.DiagnosisHistoryActivity;
import com.eyadalalimi.car.obd2.ui.home.HomeActivity;
import com.eyadalalimi.car.obd2.ui.menu.MenuActivity;
import com.eyadalalimi.car.obd2.ui.notifications.NotificationsActivity;
import com.eyadalalimi.car.obd2.ui.offline.OfflineModeActivity;
import com.eyadalalimi.car.obd2.ui.pdf.PdfReportActivity;
import com.eyadalalimi.car.obd2.ui.plans.PlansActivity;
import com.eyadalalimi.car.obd2.ui.saved.SavedCodesActivity;
import com.eyadalalimi.car.obd2.ui.settings.SettingsActivity;
import com.eyadalalimi.car.obd2.ui.plans.SubscriptionStatusActivity;
import com.eyadalalimi.car.obd2.ui.visual.VisualLibraryActivity;
import com.eyadalalimi.car.obd2.viewmodel.NotificationStateViewModel;
import com.eyadalalimi.car.obd2.viewmodel.NotificationViewModel;
import com.eyadalalimi.car.obd2.viewmodel.UserViewModel;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private DrawerLayout drawer;
    private BottomNavigationView bottomNav;
    private NavigationView navView;
    private NotificationViewModel notificationViewModel;
    private TextView badge;
    private AlertDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // إعداد Dialog عدم الاتصال بالإنترنت
        setupNoInternetDialog();

        setContentView(R.layout.activity_base);
        // تفعيل ملء الشاشة وإخفاء أزرار النظام
        hideSystemUI();
        // التعامل مع الـ Insets لتجنب تداخل عناصر الواجهة مع الحواف أو أزرار النظام المخفية (Notch/Cutout)
        handleWindowInsets();

        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        // إعداد التولبار
        Toolbar toolbar = findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getTitle());
        }

        // إعداد السحب الجانبي (Drawer) والقائمة الجانبية
        drawer = findViewById(R.id.base_drawer);
        navView = findViewById(R.id.base_nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        // معالجة نافذة Drawer مع الـ Insets (يدعم الحواف والشاشات المنحنية)
        ViewCompat.setOnApplyWindowInsetsListener(navView, (v, insets) -> {
            int top = insets.getSystemWindowInsetTop();
            int bottom = insets.getSystemWindowInsetBottom();
            v.setPadding(v.getPaddingLeft(), top, v.getPaddingRight(), bottom);
            return insets.consumeSystemWindowInsets();
        });

        // Header binding
        View header = navView.getHeaderView(0);
        ImageView imgHeader = header.findViewById(R.id.nav_header_image);
        TextView tvHeaderName  = header.findViewById(R.id.nav_header_name);
        TextView tvHeaderEmail = header.findViewById(R.id.nav_header_email);
        ImageView ivHeaderStatus = header.findViewById(R.id.ivConnectionStatus);
        TextView tvHeaderStatus = header.findViewById(R.id.tvConnectionStatus);

        UserViewModel headerVm = new ViewModelProvider(this).get(UserViewModel.class);
        headerVm.getUserProfile().observe(this, user -> {
            if (user != null) {
                String imageUrl = "https://obdcodehub.com/storage/" + user.getProfileImage();
                Glide.with(this)
                        .load(imageUrl)
                        .circleCrop()
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile_sample)
                        .into(imgHeader);
                tvHeaderName.setText(user.getUsername());
                tvHeaderEmail.setText(user.getEmail());
            }
        });
        boolean isConnected = NetworkUtil.isConnected(this);
        ivHeaderStatus.setImageResource(isConnected ? R.drawable.ic_online : R.drawable.ic_offline);
        tvHeaderStatus.setText(isConnected ? "متصل" : "غير متصل");
        header.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, AccountActivity.class));
        });

        // إعداد القائمة السفلية
        bottomNav = findViewById(R.id.base_bottom_nav);

        // ضبط الـ Insets للقائمة السفلية لضمان عدم تداخلها مع أزرار النظام أو حافة الشاشة
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
            int bottom = insets.getSystemWindowInsetBottom();
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottom);
            return insets.consumeSystemWindowInsets();
        });

        if (shouldShowBottomNav()) {
            bottomNav.setVisibility(View.VISIBLE);
        } else {
            bottomNav.setVisibility(View.GONE);
        }
        bottomNav.setOnItemSelectedListener(item -> {
            Intent intent = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                if (this instanceof HomeActivity) return true;
                intent = new Intent(this, HomeActivity.class);
            } else if (id == R.id.nav_saved) {
                if (this instanceof SavedCodesActivity) return true;
                intent = new Intent(this, SavedCodesActivity.class);
            } else if (id == R.id.nav_account) {
                if (this instanceof AccountActivity) return true;
                intent = new Intent(this, AccountActivity.class);
            } else if (id == R.id.nav_menu) {
                if (this instanceof MenuActivity) return true;
                intent = new Intent(this, MenuActivity.class);
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            return true;
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    /**
     * تفعيل وضع ملء الشاشة وإخفاء أزرار النظام (Immersive Mode)
     */
    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // للأجهزة الحديثة (Android 11+)
            getWindow().setDecorFitsSystemWindows(false);
            getWindow().getInsetsController().hide(android.view.WindowInsets.Type.navigationBars() | android.view.WindowInsets.Type.statusBars());
            getWindow().getInsetsController().setSystemBarsBehavior(
                    android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        } else {
            // للأجهزة القديمة
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        }
    }

    /**
     * ضبط الـ Insets لجميع عناصر الواجهة (حل عملي لمنع التداخل مع حواف الشاشة أو أزرار النظام)
     */
    private void handleWindowInsets() {
        View root = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            // استخدم هذا إذا أردت ضبط الحواف تلقائياً لكامل الشاشة (مثلاً إذا كان لديك عناصر إضافية)
            // int top = insets.getSystemWindowInsetTop();
            // int bottom = insets.getSystemWindowInsetBottom();
            // v.setPadding(v.getPaddingLeft(), top, v.getPaddingRight(), bottom);
            return insets.consumeSystemWindowInsets();
        });
    }

    protected boolean shouldShowBottomNav() {
        return this instanceof HomeActivity ||
                this instanceof SavedCodesActivity ||
                this instanceof AccountActivity ||
                this instanceof MenuActivity;
    }

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
        close.setOnClickListener(x -> noInternetDialog.dismiss());
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isOfflineModeEnabled = prefs.getBoolean("offline_mode_enabled", false);

        if (!isOfflineModeEnabled && !NetworkUtil.isConnected(this)) {
            if (!noInternetDialog.isShowing()) {
                noInternetDialog.show();
            }
        } else if (noInternetDialog.isShowing()) {
            noInternetDialog.dismiss();
        }

        notificationViewModel.refreshNotifications();
        highlightCurrentMenuItem();
    }

    protected void highlightCurrentMenuItem() {
        int currentDrawerItemId = getCurrentDrawerItemId();
        if (currentDrawerItemId != 0 && navView != null) {
            navView.setCheckedItem(currentDrawerItemId);
        }

        int currentBottomNavItemId = getCurrentBottomNavItemId();
        if (currentBottomNavItemId != 0 && bottomNav != null) {
            bottomNav.setSelectedItemId(currentBottomNavItemId);
        }
    }

    protected int getCurrentDrawerItemId() {
        if (this instanceof SymptomDiagnosisActivity) {
            return R.id.nav_symptom;
        } else if (this instanceof CompareCodesActivity) {
            return R.id.nav_compare;
        } else if (this instanceof VisualLibraryActivity) {
            return R.id.nav_visual;
        } else if (this instanceof DiagnosisHistoryActivity) {
            return R.id.nav_history;
        } else if (this instanceof PdfReportActivity) {
            return R.id.nav_pdf;
        } else if (this instanceof NotificationsActivity) {
            return R.id.nav_notifications;
        } else if (this instanceof TrendingCodesActivity) {
            return R.id.nav_trending;
        } else if (this instanceof TestActivity) {
            return R.id.nav_dashboard;
        } else if (this instanceof SettingsActivity) {
            return R.id.nav_settings;
        } else if (this instanceof OfflineModeActivity) {
            return R.id.nav_offline;
        }
        return 0;
    }
    protected int getCurrentBottomNavItemId() {
        if (this instanceof HomeActivity) {
            return R.id.nav_home;
        } else if (this instanceof SavedCodesActivity) {
            return R.id.nav_saved;
        } else if (this instanceof AccountActivity) {
            return R.id.nav_account;
        } else if (this instanceof MenuActivity) {
            return R.id.nav_menu;
        }
        return 0;
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

        if (getCurrentDrawerItemId() == id) {
            return true;
        }

        if (id == R.id.nav_symptom) {
            intent = new Intent(this, SymptomDiagnosisActivity.class);
        } else if (id == R.id.nav_compare) {
            intent = new Intent(this, CompareCodesActivity.class);
        } else if (id == R.id.nav_visual) {
            intent = new Intent(this, VisualLibraryActivity.class);
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
        } else if (id == R.id.nav_offline) {
            intent = new Intent(this, OfflineModeActivity.class);
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
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

    protected void setActivityLayout(int layoutResID) {
        FrameLayout container = findViewById(R.id.base_content_frame);
        getLayoutInflater().inflate(layoutResID, container, true);
    }

    @Override
    public void onBackPressed() {
        if (this instanceof HomeActivity) {
            super.onBackPressed();
            return;
        }

        if (isTaskRoot()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_scale_fade_in, R.anim.slide_scale_fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_scale_fade_in_left, R.anim.slide_scale_fade_out_right);
    }
}
