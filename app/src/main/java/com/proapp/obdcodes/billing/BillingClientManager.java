// com.proapp.obdcodes.billing/BillingClientManager.java
package com.proapp.obdcodes.billing;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener; // <--- تم التعديل هنا
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
// import com.android.billingclient.api.QueryProductDetailsResponseListener; // <--- تم حذف هذا الاستيراد القديم
// import com.android.billingclient.api.QueryProductDetailsResult; // <--- لم يعد هذا الكلاس موجودًا بشكل مباشر في الاستجابة

import java.util.Collections;
import java.util.List;

public class BillingClientManager implements PurchasesUpdatedListener {

    public interface BillingCallback {
        void onPurchaseSuccess(@NonNull String purchaseToken);
        void onPurchaseError(@NonNull String errorMessage);
    }

    private final BillingClient billingClient;
    private final BillingCallback callback;
    private final Activity activity;
    private String productId;

    public BillingClientManager(@NonNull Activity activity,
                                @NonNull BillingCallback callback) {
        this.activity = activity;
        this.callback = callback;
        billingClient = BillingClient.newBuilder(activity)
                .enablePendingPurchases()
                .setListener(this)
                .build();
    }

    public void launchPurchaseFlow(@NonNull String productId) {
        this.productId = productId;
        if (!billingClient.isReady()) {
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        queryProductDetailsAndLaunch();
                    } else {
                        callback.onPurchaseError("فشل الإعداد: " + billingResult.getDebugMessage());
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    Log.e("BillingClientMgr", "خدمة الشراء مفصولة");
                }
            });
        } else {
            queryProductDetailsAndLaunch();
        }
    }

    private void queryProductDetailsAndLaunch() {
        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(Collections.singletonList(
                        QueryProductDetailsParams.Product.newBuilder()
                                .setProductId(productId)
                                .setProductType(BillingClient.ProductType.INAPP)
                                .build()
                ))
                .build();

        billingClient.queryProductDetailsAsync(
                params,
                // <--- تم التعديل هنا: استخدام ProductDetailsResponseListener بدلاً من QueryProductDetailsResponseListener
                new ProductDetailsResponseListener() {
                    @Override
                    public void onProductDetailsResponse(
                            @NonNull BillingResult billingResult,
                            // <--- هنا، ProductDetailsResponseListener يعطي قائمة مباشرة
                            @NonNull List<ProductDetails> productDetailsList
                    ) {
                        // لم يعد هناك حاجة لـ result.getProductDetailsList() لأن القائمة تأتي مباشرة
                        // التحقق من null لـ productDetailsList لم يعد ضروريًا لأنه مضمون أن يكون غير null
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                && !productDetailsList.isEmpty()) { // <--- تم إزالة التحقق من null
                            ProductDetails details = productDetailsList.get(0);
                            BillingFlowParams.ProductDetailsParams pdp =
                                    BillingFlowParams.ProductDetailsParams.newBuilder()
                                            .setProductDetails(details)
                                            .build();
                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                    .setProductDetailsParamsList(Collections.singletonList(pdp))
                                    .build();
                            billingClient.launchBillingFlow(activity, flowParams);
                        } else {
                            callback.onPurchaseError("تعذر الحصول على تفاصيل المنتج");
                        }
                    }
                }
        );
    }

    @Override
    public void onPurchasesUpdated(
            @NonNull BillingResult billingResult,
            @NonNull List<Purchase> purchases
    ) {
        // التحقق من null لـ purchases لم يعد ضروريًا لأنه مضمون أن يكون غير null
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && !purchases.isEmpty()) { // <--- تم إزالة التحقق من null
            for (Purchase purchase : purchases) {
                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                    callback.onPurchaseSuccess(purchase.getPurchaseToken());
                    return;
                }
            }
            callback.onPurchaseError("لم يكتمل الشراء");
        } else {
            callback.onPurchaseError("فشل في الشراء: " + billingResult.getDebugMessage());
        }
    }
}
