package com.eyadalalimi.car.obd2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationStateViewModel extends ViewModel {
    private final MutableLiveData<Boolean> clearBadge = new MutableLiveData<>(false);

    public LiveData<Boolean> getClearBadgeSignal() {
        return clearBadge;
    }

    public void clearBadge() {
        clearBadge.setValue(true);
    }

    public void resetSignal() {
        clearBadge.setValue(false);
    }
}
