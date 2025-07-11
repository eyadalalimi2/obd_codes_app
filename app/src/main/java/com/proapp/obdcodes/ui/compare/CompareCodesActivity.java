package com.proapp.obdcodes.ui.compare;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.proapp.obdcodes.util.SubscriptionUtils;
import androidx.lifecycle.ViewModelProvider;
import android.view.inputmethod.InputMethodManager;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.ApiClient;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.ui.base.BaseActivity;
import com.proapp.obdcodes.viewmodel.CompareViewModel;

public class CompareCodesActivity extends BaseActivity {
    private TextInputEditText et1, et2;
    private View card1, card2;
    private CompareViewModel vm;
    private View scrollCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_compare_codes);
        setTitle(getString(R.string.compare_codes));

        et1   = findViewById(R.id.etCode1);
        et2   = findViewById(R.id.etCode2);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        scrollCards = findViewById(R.id.scrollCards);
        findViewById(R.id.btnCompare).setOnClickListener(v -> {
            hideKeyboard(); 
            onCompare();
        });
        View btnCompare = findViewById(R.id.btnCompare);
        // ✅ تحقق من صلاحية استخدام المقارنة
        SubscriptionUtils.hasFeature(this, "COMPARE_CODES", isAllowed -> {
            if (!isAllowed) {
                Toast.makeText(this, "هذه الميزة متاحة فقط للمشتركين", Toast.LENGTH_LONG).show();
                btnCompare.setEnabled(false);
                return;
            }

            // ✅ تم السماح → فعل الزر
            btnCompare.setOnClickListener(v -> {
                hideKeyboard();
                onCompare();
            });
        });
        vm = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(CompareViewModel.class);
    }

    /** إخفاء لوحة المفاتيح وإزالة التركيز من الحقول */
    private void hideKeyboard() {
        // 1) طلب التركيز إلى الحاوية الرئيسية
        View root = findViewById(R.id.mainCoordinator); // أو المعرف الذي أعطيته للـ CoordinatorLayout
        root.requestFocus();

        // 2) إخفاء الكيبورد
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // نحاول إخفاء بناءً على أي حقل كان لديه التركيز
            View current = getCurrentFocus();
            if (current != null) {
                imm.hideSoftInputFromWindow(current.getWindowToken(), 0);
            }
        }
    }


    private void onCompare() {
        String code1 = et1.getText() != null ? et1.getText().toString().trim() : "";
        String code2 = et2.getText() != null ? et2.getText().toString().trim() : "";

        if (TextUtils.isEmpty(code1) || TextUtils.isEmpty(code2)) {
            Toast.makeText(this, R.string.invalid_selection, Toast.LENGTH_SHORT).show();
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
    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }

    private void bindCard(View card, ObdCode code) {
        // 1) الكود باللون الأحمر
        ((TextView) card.findViewById(R.id.tvCardCode))
                .setText(code.getCode());

        // 2) العنوان
        ((TextView) card.findViewById(R.id.tvCardTitle))
                .setText(code.getTitle());

        // 3) الشدة
        LinearProgressIndicator sevBar =
                card.findViewById(R.id.progressSeverity);
        sevBar.setProgress(parseSeverity(code.getSeverity()));

        // 4) الوصف قابل للطَيّ
        setupExpandable(
                card,
                R.id.headerDescription,
                R.id.tvDescriptionContent,
                R.id.ivToggleDescription,
                code.getDescription()
        );

        // 5) الأعراض الشائعة
        setupExpandable(
                card,
                R.id.headerSymptoms,
                R.id.tvSymptomsContent,
                R.id.ivToggleSymptoms,
                code.getSymptoms()
        );

        // 6) الأسباب
        setupExpandable(
                card,
                R.id.headerCauses,
                R.id.tvCausesContent,
                R.id.ivToggleCauses,
                code.getCauses()
        );

        // 7) الحلول
        setupExpandable(
                card,
                R.id.headerSolutions,
                R.id.tvSolutionsContent,
                R.id.ivToggleSolutions,
                code.getSolutions()
        );

        // 8) التشخيص قابل للطَيّ
        setupExpandable(
                card,
                R.id.headerDiagnosis,
                R.id.tvDiagnosisContent,
                R.id.ivToggleDiagnosis,
                code.getDiagnosis()
        );

        // 9) الصورة مع معالجة الفارغ
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
            case "moderate": return 50;
            case "high":     return 80;
            default:         return 0;
        }
    }
}
