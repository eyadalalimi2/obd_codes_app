package com.eyadalalimi.car.obd2.utils;

import android.content.Context;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import com.bumptech.glide.Glide;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.network.ApiClient;

public class BindingAdapters {


    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        // إذا null أو فارغ، عرض الصورة الافتراضية
        if (url == null || url.isEmpty()) {
            view.setImageResource(R.drawable.profile_sample);
            return;
        }

        // إذا المسار المرسل نسبي (لا يبدأ بـ http) نضيف عليه قاعدة الصور:
        String fullUrl = url.startsWith("http") ? url : ApiClient.IMAGE_BASE_URL + url;

        Glide.with(view.getContext())
                .load(fullUrl)
                .circleCrop()
                .placeholder(R.drawable.profile_sample)
                .error(R.drawable.profile_sample)
                .into(view);
    }
    public static String formatDateWithPrefix(Context ctx, String isoDate, int prefixResId) {
        String prefix = ctx.getString(prefixResId);
        String formatted = DateUtils.formatIsoDate(ctx, isoDate);
        return prefix + " " + formatted;
    }
}
