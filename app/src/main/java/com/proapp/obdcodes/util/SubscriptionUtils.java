package com.proapp.obdcodes.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;

import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.ui.subscription.SubscriptionRepository;
import com.proapp.obdcodes.ui.subscription.SubscriptionStatusActivity;

import java.util.List;

public class SubscriptionUtils {

    public static void checkFeatureAccess(
            Context context,
            String featureKey,
            Runnable onAllowed
    ) {
        new SubscriptionRepository(context)
                .getCurrentSubscription(new SubscriptionRepository.CurrentSubscriptionCallback() {
                    @Override
                    public void onSuccess(Subscription subscription) {
                        Log.d("SUB_FEATURES", "features=" + subscription.getFeatures() + ", required=" + featureKey);
                        if (subscription != null && hasFeature(subscription, featureKey)) {
                            onAllowed.run();
                        } else {
                            showLockedDialog(context);
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(
                                context,
                                "تعذر التحقق من الاشتراك: " + errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                        showLockedDialog(context);
                    }
                });
    }

    public static void hasFeature(
            Context context,
            String featureKey,
            FeatureCheckCallback callback
    ) {
        new SubscriptionRepository(context)
                .getCurrentSubscription(new SubscriptionRepository.CurrentSubscriptionCallback() {
                    @Override
                    public void onSuccess(Subscription subscription) {
                        boolean allowed = subscription != null
                                && hasFeature(subscription, featureKey);
                        callback.onResult(allowed);
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        callback.onResult(false);
                    }
                });
    }

    private static boolean hasFeature(Subscription subscription, String featureKey) {
        List<String> features = subscription.getFeatures();
        Log.d("SUB_FEATURES", "features=" + features + ", required=" + featureKey);
        if (features == null) return false;
        for (String f : features) {
            if (f != null && f.equalsIgnoreCase(featureKey)) {
                Log.d("SUB_FEATURES", "Feature FOUND: " + f);
                return true;
            }
        }
        Log.d("SUB_FEATURES", "Feature NOT FOUND");
        return false;
    }

    private static void showLockedDialog(Context context) {
        Toast.makeText(
                context,
                "هذه الميزة مخصصة للمشتركين",
                Toast.LENGTH_LONG
        ).show();
        Intent intent = new Intent(context, SubscriptionStatusActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static boolean hasFeature(String featureKey) {
        return false;
    }

    public interface FeatureCheckCallback {
        void onResult(boolean isAllowed);
    }
}
