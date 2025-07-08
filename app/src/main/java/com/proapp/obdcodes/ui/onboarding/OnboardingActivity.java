package com.proapp.obdcodes.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.ui.auth.AuthActivity;
import com.proapp.obdcodes.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private OnboardingAdapter adapter;
    private List<OnboardingItem> itemList;
    private Button btnNext;
    private TextView tvSkip, tvPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.onboarding_viewpager);
        btnNext = findViewById(R.id.btnNext);
        tvSkip = findViewById(R.id.btnSkip);
        tvPrev = findViewById(R.id.btnPrev);

        setupItems();
        adapter = new OnboardingAdapter(itemList);
        viewPager.setAdapter(adapter);


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                tvPrev.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
                btnNext.setText(position == itemList.size()-1 ? getString(R.string.get_started) : getString(R.string.next));
            }
        });

        btnNext.setOnClickListener(v -> {
            int next = viewPager.getCurrentItem() + 1;
            if (next < itemList.size()) {
                viewPager.setCurrentItem(next);
            } else {
                launchAuth();
            }
        });

        tvSkip.setOnClickListener(v -> launchAuth());
        tvPrev.setOnClickListener(v -> {
            int prev = viewPager.getCurrentItem() - 1;
            if (prev >= 0) viewPager.setCurrentItem(prev);
        });
    }

    private void setupItems() {
        itemList = new ArrayList<>();
        itemList.add(new OnboardingItem(R.drawable.ic_onboarding_1, "مرحباً بك", "تطبيق OBD Codes يتيح لك تشخيص الأعطال بسهولة."));
        itemList.add(new OnboardingItem(R.drawable.ic_onboarding_2, "ابحث بالرمز", "ابحث عن رموز الأعطال عبر الكود."));
        itemList.add(new OnboardingItem(R.drawable.ic_onboarding_3, "ميزات متقدمة", "استفد من التشخيص بالأعراض والإحصائيات."));
    }

    private void launchAuth() {
        getSharedPreferences("com.proapp.obdcodes_preferences", MODE_PRIVATE)
                .edit()
                .putBoolean("is_first_launch", false)
                .apply();

        startActivity(new Intent(OnboardingActivity.this, AuthActivity.class));
        finish();
    }

}
