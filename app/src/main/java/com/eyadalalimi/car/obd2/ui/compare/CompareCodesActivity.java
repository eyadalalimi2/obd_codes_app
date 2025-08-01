package com.eyadalalimi.car.obd2.ui.compare;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.network.ApiClient;
import com.eyadalalimi.car.obd2.network.model.ObdCode;
import com.eyadalalimi.car.obd2.ui.base.BaseActivity;
import com.eyadalalimi.car.obd2.util.SubscriptionUtils;
import com.eyadalalimi.car.obd2.viewmodel.CompareViewModel;

import java.util.regex.Pattern;

public class CompareCodesActivity extends BaseActivity {
    private AutoCompleteTextView et1, et2;

    private View card1, card2;
    private CompareViewModel vm;
    private View scrollCards;

    /** نمط التحقق من صحة كود OBD (حرف P/C/B/U متبوع بـ4 أرقام) */
    private static final Pattern CODE_PATTERN = Pattern.compile("^[PCBU][0-9]{4}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ منع تهيئة الواجهة حتى يتم التأكد من وجود صلاحية الوصول
        SubscriptionUtils.checkFeatureAccess(this, "COMPARE_CODES", this::initCompareUI);
    }

    /** تهيئة واجهة المقارنة فقط بعد التأكد من الصلاحية */
    private void initCompareUI() {
        runOnUiThread(() -> {
            setActivityLayout(R.layout.activity_compare_codes);
            setTitle(getString(R.string.compare_codes));
            et1 = findViewById(R.id.etCode1);
            et2 = findViewById(R.id.etCode2);
            card1 = findViewById(R.id.card1);
            card2 = findViewById(R.id.card2);
            scrollCards = findViewById(R.id.scrollCards);

            findViewById(R.id.btnCompare).setOnClickListener(v -> {
                hideKeyboard();
                onCompare();
            });

            vm = new ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                    .get(CompareViewModel.class);
        });
    }

    /** إخفاء لوحة المفاتيح وإزالة التركيز من الحقول */
    private void hideKeyboard() {
        View root = findViewById(R.id.mainCoordinator);
        if (root != null) root.requestFocus();

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View current = getCurrentFocus();
            if (current != null) {
                imm.hideSoftInputFromWindow(current.getWindowToken(), 0);
            }
        }
    }

    private void onCompare() {
        String code1 = et1.getText() != null ? et1.getText().toString().trim() : "";
        String code2 = et2.getText() != null ? et2.getText().toString().trim() : "";

        // التحقق من صحة الكودين وفق النمط المحدد
        if (!isValidCode(code1) || !isValidCode(code2)) {
            Toast.makeText(this, R.string.code_invalid_format, Toast.LENGTH_SHORT).show();
            return;
        }

        vm.getCodeDetail(code1).observe(this, c1 -> {
            if (c1 == null) {
                Toast.makeText(this, R.string.code_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            vm.getCodeDetail(code2).observe(this, c2 -> {
                if (c2 == null) {
                    Toast.makeText(this, R.string.code_not_found, Toast.LENGTH_SHORT).show();
                    return;
                }
                scrollCards.setVisibility(View.VISIBLE);
                bindCard(card1, c1);
                bindCard(card2, c2);
            });
        });
    }

    /** التحقق من صحة الكود */
    private boolean isValidCode(String code) {
        return !TextUtils.isEmpty(code) && CODE_PATTERN.matcher(code).matches();
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

    private void bindCard(View card, ObdCode code) {
        ((TextView) card.findViewById(R.id.tvCardCode))
                .setText(code.getCode());
        ((TextView) card.findViewById(R.id.tvCardTitle))
                .setText(code.getTitle());
        LinearProgressIndicator sevBar =
                card.findViewById(R.id.progressSeverity);
        sevBar.setProgress(parseSeverity(code.getSeverity()));
        setupExpandable(card, R.id.headerDescription, R.id.tvDescriptionContent, R.id.ivToggleDescription, code.getDescription());
        setupExpandable(card, R.id.headerSymptoms, R.id.tvSymptomsContent, R.id.ivToggleSymptoms, code.getSymptoms());
        setupExpandable(card, R.id.headerCauses, R.id.tvCausesContent, R.id.ivToggleCauses, code.getCauses());
        setupExpandable(card, R.id.headerSolutions, R.id.tvSolutionsContent, R.id.ivToggleSolutions, code.getSolutions());
        setupExpandable(card, R.id.headerDiagnosis, R.id.tvDiagnosisContent, R.id.ivToggleDiagnosis, code.getDiagnosis());

        ImageView iv = card.findViewById(R.id.ivCardImage);
        String imageUrl = ApiClient.IMAGE_BASE_URL + code.getImage();
        Log.d("CompareCodes", "Loading image → " + imageUrl);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.splash)
                .into(iv);
    }

    /**
     * يعرض/يخفي المحتوى ويدير السهم
     */
    private void setupExpandable(View card,
                                 int headerId,
                                 int contentId,
                                 int iconId,
                                 String contentText) {
        View header = card.findViewById(headerId);
        TextView content = card.findViewById(contentId);
        ImageView arrow = card.findViewById(iconId);

        content.setText(contentText);
        header.setOnClickListener(v -> {
            boolean open = content.getVisibility() == View.VISIBLE;
            content.setVisibility(open ? View.GONE : View.VISIBLE);
            arrow.setRotation(open ? 0f : 180f);
        });
    }

    private int parseSeverity(String severity) {
        if (severity == null) return 0;
        switch (severity.toLowerCase()) {
            case "low":      return 25;
            case "medium": return 50;
            case "high":     return 80;
            default:         return 0;
        }
    }
}
