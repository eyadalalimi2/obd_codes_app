package com.proapp.obdcodes.ui.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationItemView; // ستحتاج هذه الاستيرادات للأنيميشن إذا أردت الاحتفاظ بها
import com.google.android.material.bottomnavigation.BottomNavigationMenuView; // ستحتاج هذه الاستيرادات للأنيميشن إذا أردت الاحتفاظ بها
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.base.NetworkUtil;
import com.proapp.obdcodes.network.model.UserNotification;
import com.proapp.obdcodes.ui.TestActivity;
import com.proapp.obdcodes.ui.account.AccountActivity;
import com.proapp.obdcodes.ui.analytics.TrendingCodesActivity;
import com.proapp.obdcodes.ui.cars.CarListActivity;
import com.proapp.obdcodes.ui.chat.ChatActivity;
import com.proapp.obdcodes.ui.compare.CompareCodesActivity;
import com.proapp.obdcodes.ui.diagnosis.SymptomDiagnosisActivity;
import com.proapp.obdcodes.ui.history.DiagnosisHistoryActivity;
import com.proapp.obdcodes.ui.home.HomeActivity;
import com.proapp.obdcodes.ui.menu.MenuActivity;
import com.proapp.obdcodes.ui.notifications.NotificationsActivity;
import com.proapp.obdcodes.ui.offline.OfflineModeActivity;
import com.proapp.obdcodes.ui.pdf.PdfReportActivity;
import com.proapp.obdcodes.ui.plans.PlansActivity;
import com.proapp.obdcodes.ui.saved.SavedCodesActivity;
import com.proapp.obdcodes.ui.settings.SettingsActivity;
import com.proapp.obdcodes.ui.subscription.SubscriptionStatusActivity;
import com.proapp.obdcodes.ui.visual.VisualLibraryActivity;
import com.proapp.obdcodes.viewmodel.NotificationStateViewModel;
import com.proapp.obdcodes.viewmodel.NotificationViewModel;
import com.proapp.obdcodes.viewmodel.UserViewModel;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private DrawerLayout drawer;
    private BottomNavigationView bottomNav;
    private NavigationView navView; // إضافة هذا المتغير لسهولة الوصول إليه
    private NotificationViewModel notificationViewModel;
    private TextView badge;
    private boolean backPressedToHome = false;
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
        navView = findViewById(R.id.base_nav_view); // تهيئة المتغير
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        // تم إزالة الأسطر التالية لأننا نريد أن تدعم NavigationView التحديد التلقائي
        // navView.setItemIconTintList(null);
        // navView.setItemTextColor(null);
        // navView.setItemBackground(null);
        // navView.getMenu().setGroupCheckable(0, false, true); // هذا السطر هو السبب في عدم التحديد التلقائي، يجب إزالته أو التفكير فيه

        // في `drawer_paid_menu.xml` لديك `android:checkableBehavior="single"`
        // هذا يعني أن NavigationView يمكنه إدارة التحديد بنفسه.
        // فقط تأكد من أن الألوان المستخدمة في الـ Style الخاص بالـ App متوافقة
        // مع التحديد الافتراضي (عادة ما يكون لون التمييز primary color)

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
                String imageUrl = "https://obdcode.xyz/storage/" + user.getProfileImage();
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
// إظهار حالة الاتصال: أيقونة + نص
        boolean isConnected = NetworkUtil.isConnected(this);
        ivHeaderStatus.setImageResource(isConnected ? R.drawable.ic_online : R.drawable.ic_offline);
        tvHeaderStatus.setText(isConnected ? "متصل" : "غير متصل");
        header.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, AccountActivity.class));
        });

        // Setup bottom navigation
        bottomNav = findViewById(R.id.base_bottom_nav);

        // ✅ إظهار أو إخفاء BottomNavigationView حسب الصفحة
        if (shouldShowBottomNav()) {
            bottomNav.setVisibility(View.VISIBLE);
        } else {
            bottomNav.setVisibility(View.GONE);
        }
        bottomNav.setOnItemSelectedListener(item -> {
            // لا نحتاج لـ animateBottomNav هنا إذا كنا نستخدم setChecked
            // animateBottomNav(item); // يمكنك إزالة هذا السطر إذا لم تعد تريده
            Intent intent = null;
            int id = item.getItemId();

            // قم بإزالة الأسطر المكررة أو غير الصحيحة هنا
            // وتأكد أن كل ID يقود إلى Activity واحد فقط
            if (id == R.id.nav_home) {
                // لا تفعل شيئًا إذا كنت بالفعل في HomeActivity
                if (this instanceof HomeActivity) {
                    return true;
                }
                intent = new Intent(this, HomeActivity.class);
            } else if (id == R.id.nav_saved) {
                if (this instanceof SavedCodesActivity) {
                    return true;
                }
                intent = new Intent(this, SavedCodesActivity.class);
            } else if (id == R.id.nav_account) {
                if (this instanceof AccountActivity) {
                    return true;
                }
                intent = new Intent(this, AccountActivity.class);
            } else if (id == R.id.nav_menu) {
                if (this instanceof MenuActivity) {
                    return true;
                }
                intent = new Intent(this, MenuActivity.class);
            }

            if (intent != null) {
                // هذه الأعلام جيدة للانتقال بين الأنشطة الرئيسية
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // أغلق النشاط الحالي بعد الانتقال
            }
            return true;
        });
    }

    protected boolean shouldShowBottomNav() {
        // يمكنك هنا تحديد الأنشطة التي يجب أن تظهر فيها القائمة السفلية
        // على سبيل المثال:
        return this instanceof HomeActivity ||
                this instanceof SavedCodesActivity ||
                this instanceof AccountActivity ||
                this instanceof MenuActivity;
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

        // **** إضافة منطق تحديد العنصر النشط هنا ****
        highlightCurrentMenuItem();
    }

    /**
     * تحدد العنصر النشط في القائمة الجانبية والقائمة السفلية بناءً على النشاط الحالي.
     * يجب استدعاء هذه الدالة في onResume() لكل نشاط يرث من BaseActivity.
     */
    protected void highlightCurrentMenuItem() {
        // تحديد العنصر في القائمة الجانبية (Navigation Drawer)
        int currentDrawerItemId = getCurrentDrawerItemId();
        if (currentDrawerItemId != 0 && navView != null) {
            navView.setCheckedItem(currentDrawerItemId);
        }

        // تحديد العنصر في القائمة السفلية (Bottom Navigation Bar)
        int currentBottomNavItemId = getCurrentBottomNavItemId();
        if (currentBottomNavItemId != 0 && bottomNav != null) {
            bottomNav.setSelectedItemId(currentBottomNavItemId);
            // قد تحتاج لإزالة animateBottomNav() من onItemSelectedListener
            // لأن setSelectedItemId() قد تشغل تأثيرات التحديد الافتراضية
            // إذا كنت لا تزال تريد تأثير الـ scale، يمكنك استدعاءه هنا
            // animateBottomNav(bottomNav.getMenu().findItem(currentBottomNavItemId));
        }
    }

    /**
     * ترجع ID العنصر المطابق للنشاط الحالي في القائمة الجانبية.
     * يجب تعديل هذه الدالة لتغطية جميع الأنشطة التي يمكن الوصول إليها من القائمة الجانبية.
     */
    protected int getCurrentDrawerItemId() {
        if (this instanceof SymptomDiagnosisActivity) {
            return R.id.nav_symptom;
        } else if (this instanceof CompareCodesActivity) {
            return R.id.nav_compare;
        } else if (this instanceof VisualLibraryActivity) { // للـ nav_visual
            return R.id.nav_visual;
        } else if (this instanceof DiagnosisHistoryActivity) {
            return R.id.nav_history;
        } else if (this instanceof PdfReportActivity) {
            return R.id.nav_pdf;
        } else if (this instanceof NotificationsActivity) {
            return R.id.nav_notifications;
        } else if (this instanceof TrendingCodesActivity) {
            return R.id.nav_trending;
        } else if (this instanceof TestActivity) { // للـ nav_dashboard
            return R.id.nav_dashboard;
        } else if (this instanceof SettingsActivity) {
            return R.id.nav_settings;
        } else if (this instanceof OfflineModeActivity) {
            return R.id.nav_offline;
        } else if (this instanceof PlansActivity) { // للـ nav_plans
            return R.id.nav_plans;
        } else if (this instanceof SubscriptionStatusActivity) { // للـ nav_subscription_status
            return R.id.nav_subscription_status;
        }
        // أضف المزيد من الحالات للأنشطة الأخرى
        return 0; // لا يوجد عنصر محدد
    }

    /**
     * ترجع ID العنصر المطابق للنشاط الحالي في القائمة السفلية.
     * يجب تعديل هذه الدالة لتغطية جميع الأنشطة التي يمكن الوصول إليها من القائمة السفلية.
     */
    protected int getCurrentBottomNavItemId() {
        if (this instanceof HomeActivity) {
            return R.id.nav_home;
        } else if (this instanceof SavedCodesActivity) {
            return R.id.nav_saved;
        } else if (this instanceof AccountActivity) {
            return R.id.nav_account;
        } else if (this instanceof MenuActivity) { // لـ nav_menu في BottomNav
            return R.id.nav_menu;
        }
        // أضف المزيد من الحالات للأنشطة الأخرى التي يجب أن يكون لها عنصر في BottomNav
        return 0; // لا يوجد عنصر محدد
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

        // تجنب إعادة تشغيل النشاط الحالي إذا كان هو نفسه
        if (getCurrentDrawerItemId() == id) {
            return true; // لا تفعل شيئًا إذا كان العنصر المحدد هو النشاط الحالي
        }

        if (id == R.id.nav_symptom) { // تم تغيير nav_symptoms إلى nav_symptom بناءً على XML
            intent = new Intent(this, SymptomDiagnosisActivity.class);
        } else if (id == R.id.nav_compare) {
            intent = new Intent(this, CompareCodesActivity.class);
        } else if (id == R.id.nav_visual) { // تم تغيير nav_visuals إلى nav_visual بناءً على XML
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
        } else if (id == R.id.nav_offline) {
            intent = new Intent(this, OfflineModeActivity.class);
        } else if (id == R.id.nav_subscription_status) {
            intent = new Intent(this, SubscriptionStatusActivity.class); // جعلها تعود بـ Intent
        }else if (id == R.id.nav_plans) {
            intent = new Intent(this, PlansActivity.class); // جعلها تعود بـ Intent
        }else if (id == R.id.nav_cars) {
            intent = new Intent(this, CarListActivity.class);
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

    /**
     * Inflate a layout into the base content frame.
     */
    protected void setActivityLayout(int layoutResID) {
        FrameLayout container = findViewById(R.id.base_content_frame);
        getLayoutInflater().inflate(layoutResID, container, true);
    }

    @Override
    public void onBackPressed() {
        if (this instanceof HomeActivity) {
            // الصفحة الرئيسية: خروج عادي من التطبيق
            super.onBackPressed();
            return;
        }

        // تحقق هل هذا النشاط هو الجذر (أول Activity في الـ stack)
        if (isTaskRoot()) {
            // إذا كان هو الجذر (لا يوجد صفحة سابقة): اذهب للرئيسية ولا تخرج نهائياً
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            // هناك صفحة سابقة: ترجع للوراء (سلوك طبيعي)
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