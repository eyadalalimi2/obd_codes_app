// com.proapp.obdcodes.ui.plans/PlansViewModel.java
package com.proapp.obdcodes.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.proapp.obdcodes.network.model.Plan;
import com.proapp.obdcodes.network.model.SubscriptionRequest;
import com.proapp.obdcodes.network.model.ActivationRequest;
import com.proapp.obdcodes.repository.PlanRepository;

import java.util.List;

public class PlansViewModel extends AndroidViewModel {

    private final PlanRepository repository;
    private final MutableLiveData<List<Plan>> plansLD      = new MutableLiveData<>();
    private final MutableLiveData<Boolean>    loadingLD    = new MutableLiveData<>(false);
    private final MutableLiveData<String>     errorLD      = new MutableLiveData<>();
    private final MutableLiveData<Boolean>    subscribeLD  = new MutableLiveData<>();
    private final MutableLiveData<Boolean>    activateLD   = new MutableLiveData<>();

    public PlansViewModel(@NonNull Application app) {
        super(app);
        repository = new PlanRepository(app);
    }

    public LiveData<List<Plan>>  getPlans()           { return plansLD;      }
    public LiveData<Boolean>     getLoading()         { return loadingLD;    }
    public LiveData<String>      getError()           { return errorLD;      }
    public LiveData<Boolean>     getSubscribeResult() { return subscribeLD;  }
    public LiveData<Boolean>     getActivateResult()  { return activateLD;   }

    public void loadPlans() {
        loadingLD.setValue(true);
        repository.getAllPlans(new PlanRepository.PlansCallback() {
            @Override
            public void onSuccess(@NonNull List<Plan> p) {
                loadingLD.postValue(false);
                plansLD.postValue(p);
            }
            @Override
            public void onFailure(@NonNull String e) {
                loadingLD.postValue(false);
                errorLD.postValue(e);
            }
        });
    }

    public void subscribeWithToken(long planId, @NonNull String token) {
        loadingLD.setValue(true);
        SubscriptionRequest req = new SubscriptionRequest(planId, token, "google_play", null);
        repository.subscribeToPlan(req, new PlanRepository.SubscriptionCallback() {
            @Override
            public void onSuccess() {
                loadingLD.postValue(false);
                subscribeLD.postValue(true);
            }
            @Override
            public void onFailure(@NonNull String e) {
                loadingLD.postValue(false);
                errorLD.postValue(e);
                subscribeLD.postValue(false);
            }
        });
    }

    public void activateByCode(long planId, @NonNull String code) {
        loadingLD.setValue(true);
        ActivationRequest req = new ActivationRequest(planId, code);
        repository.activateByCode(req, new PlanRepository.ActivateCallback() {
            @Override
            public void onSuccess() {
                loadingLD.postValue(false);
                activateLD.postValue(true);
            }
            @Override
            public void onFailure(@NonNull String e) {
                loadingLD.postValue(false);
                errorLD.postValue(e);
                activateLD.postValue(false);
            }
        });
    }
}
