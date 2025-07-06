package com.proapp.obdcodes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.proapp.obdcodes.R;

public class TestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        // Free feature buttons
        findViewById(R.id.btn_language).setOnClickListener(v -> {
            startActivity(new Intent(this, com.proapp.obdcodes.ui.settings.LanguageSettingsActivity.class));
        });

        findViewById(R.id.btn_home).setOnClickListener(v -> startActivity(new Intent(this, com.proapp.obdcodes.ui.home.HomeActivity.class)));
        findViewById(R.id.btn_saved).setOnClickListener(v -> startActivity(new Intent(this, com.proapp.obdcodes.ui.saved.SavedCodesActivity.class)));
        findViewById(R.id.btn_account).setOnClickListener(v -> startActivity(new Intent(this, com.proapp.obdcodes.ui.account.AccountActivity.class)));
        findViewById(R.id.btn_about).setOnClickListener(v -> startActivity(new Intent(this, com.proapp.obdcodes.ui.about.AboutActivity.class)));
        findViewById(R.id.btn_contact).setOnClickListener(v -> startActivity(new Intent(this, com.proapp.obdcodes.ui.support.ContactUsActivity.class)));
        findViewById(R.id.btn_how).setOnClickListener(v -> startActivity(new Intent(this, com.proapp.obdcodes.ui.howitworks.HowItWorksActivity.class)));
        findViewById(R.id.btn_pricing).setOnClickListener(v -> startActivity(new Intent(this, com.proapp.obdcodes.ui.pricing.PricingActivity.class)));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_symptoms) {
            // Open symptom-based diagnosis
        } else if (id == R.id.nav_compare) {
            // Open code comparison
        } else if (id == R.id.nav_visuals) {
            // Open visual components
        } else if (id == R.id.nav_history) {
            // Open diagnosis history
        } else if (id == R.id.nav_pdf) {
            // Open PDF generator
        } else if (id == R.id.nav_ai_assistant) {
            // Open AI Assistant
        } else if (id == R.id.nav_notifications) {
            // Open smart notifications
        } else if (id == R.id.nav_trending) {
            // Open trending analytics
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
