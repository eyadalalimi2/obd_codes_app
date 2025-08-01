package com.eyadalalimi.car.obd2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eyadalalimi.car.obd2.network.model.Subscription;
import com.eyadalalimi.car.obd2.network.model.SubscriptionRequest;
import com.eyadalalimi.car.obd2.repository.SubscriptionRepository;

public class SubscriptionViewModel extends AndroidViewModel {

    private final SubscriptionRepository repository;
    private final MutableLiveData<Subscription> subscriptionLD   = new MutableLiveData<>();
    private final MutableLiveData<Boolean>     loadingLD        = new MutableLiveData<>(false);
    private final MutableLiveData<String>      errorLD          = new MutableLiveData<>();
    private final MutableLiveData<Boolean>     renewResultLD    = new MutableLiveData<>();
    private final MutableLiveData<Boolean>     cancelResultLD   = new MutableLiveData<>();

    public SubscriptionViewModel(@NonNull Application app) {
        super(app);
        repository = new SubscriptionRepository(app);
    }

    public LiveData<Subscription> getCurrentSubscription() { return subscriptionLD; }
    public LiveData<Boolean>     getLoading()             { return loadingLD;       }
    public LiveData<String>      getError()               { return errorLD;         }
    public LiveData<Boolean>     getRenewResult()         { return renewResultLD;   }
    public LiveData<Boolean>     getCancelResult()        { return cancelResultLD;  }

    /** يجلب بيانات الاشتراك الحالي */
    public void refresh() {
        loadingLD.setValue(true);
        repository.getCurrentSubscription(new SubscriptionRepository.CurrentSubscriptionCallback() {
            @Override
            public void onSuccess(@NonNull Subscription sub) {
                loadingLD.postValue(false);
                subscriptionLD.postValue(sub);
            }
            @Override
            public void onFailure(@NonNull String err) {
                loadingLD.postValue(false);
                errorLD.postValue(err);
            }
        });
    }

    /** يجدد الاشتراك يدوياً */
    // SubscriptionViewModel.java
    public void renewSubscription() {
        Subscription current = subscriptionLD.getValue();
        if (current == null) {
            errorLD.setValue("لا يوجد اشتراك للتجديد");
            return;
        }

        loadingLD.setValue(true);
        // استخدام ID الخطة و googleProductId بدلاً من id و endAt
        SubscriptionRequest req = new SubscriptionRequest(
                current.getPlan().getId(),       // plan_id الصحيح
                current.getGoogleProductId(),     // purchase_token (محتوى التوكن من Google Play)
                "google_play"                    // المنصة
        );

        repository.renewSubscription(req, new SubscriptionRepository.SubscriptionCallback() {
            @Override
            public void onSuccess() {
                loadingLD.postValue(false);
                renewResultLD.postValue(true);
            }
            @Override
            public void onFailure(@NonNull String err) {
                loadingLD.postValue(false);
                errorLD.postValue(err);
                renewResultLD.postValue(false);
            }
        });
    }


    /** يلغي الاشتراك الحالي */
    public void cancelSubscription() {
        Subscription current = subscriptionLD.getValue();
        if (current == null) {
            errorLD.setValue("لا يوجد اشتراك للإلغاء");
            return;
        }

        loadingLD.setValue(true);
        SubscriptionRequest req = new SubscriptionRequest(
                current.getId(),
                current.getGoogleProductId(),
                "google_play"
        );

        repository.cancelSubscription(req, new SubscriptionRepository.SubscriptionCallback() {
            @Override
            public void onSuccess() {
                loadingLD.postValue(false);
                cancelResultLD.postValue(true);
            }
            @Override
            public void onFailure(@NonNull String err) {
                loadingLD.postValue(false);
                errorLD.postValue(err);
                cancelResultLD.postValue(false);
            }
        });
    }
}
