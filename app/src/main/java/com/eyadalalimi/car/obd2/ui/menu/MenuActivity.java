package com.eyadalalimi.car.obd2.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.ui.about.AboutActivity;
import com.eyadalalimi.car.obd2.ui.account.AccountActivity;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.ui.home.HomeActivity;
import com.eyadalalimi.car.obd2.ui.howitworks.HowItWorksActivity;
import com.eyadalalimi.car.obd2.ui.legal.DisclaimerActivity;
import com.eyadalalimi.car.obd2.ui.legal.PrivacyActivity;
import com.eyadalalimi.car.obd2.ui.legal.TermsActivity;
import com.eyadalalimi.car.obd2.ui.pricing.PricingActivity;
import com.eyadalalimi.car.obd2.ui.saved.SavedCodesActivity;
import com.eyadalalimi.car.obd2.ui.support.ContactUsActivity;

public class MenuActivity extends BaseActivity {

    private void setClick(int id, Class<?> target) {
        findViewById(id).setOnClickListener(v -> startActivity(new Intent(this, target)));
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return true; // ✅ قم بتغيير هذا إلى true إذا كنت تريد أن تظهر BottomNav هنا
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_menu);

        // إعداد الشريط العلوي
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("القائمة");

        // الروابط
        setClick(R.id.btnPricing, PricingActivity.class);
        setClick(R.id.btnAbout, AboutActivity.class);
        setClick(R.id.btnHowItWorks, HowItWorksActivity.class);
        setClick(R.id.btnDisclaimer, DisclaimerActivity.class);
        setClick(R.id.btnTerms, TermsActivity.class);
        setClick(R.id.btnPrivacy, PrivacyActivity.class);
        setClick(R.id.btnContact, ContactUsActivity.class);
        setClick(R.id.btnHome, HomeActivity.class);
        setClick(R.id.btnSaved, SavedCodesActivity.class);
        setClick(R.id.btnAccount, AccountActivity.class);

        findViewById(R.id.btnRate).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("market://details?id=" + getPackageName()));
            startActivity(intent);
        });

        findViewById(R.id.btnShare).setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_this_app)));
        });
    }
}