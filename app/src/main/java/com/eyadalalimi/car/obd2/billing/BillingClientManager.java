package com.eyadalalimi.car.obd2.billing;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;

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
                new ProductDetailsResponseListener() {
                    @Override
                    public void onProductDetailsResponse(
                            @NonNull BillingResult billingResult,
                            @NonNull List<ProductDetails> productDetailsList
                    ) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                && !productDetailsList.isEmpty()) {
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
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && !purchases.isEmpty()) {
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
