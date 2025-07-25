package com.eyadalalimi.car.obd2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eyadalalimi.car.obd2.network.model.UserNotification;
import com.eyadalalimi.car.obd2.repository.NotificationRepository;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {

    private final NotificationRepository repo;
    private final MutableLiveData<List<UserNotification>> notifications = new MutableLiveData<>();

    public NotificationViewModel(@NonNull Application app) {
        super(app);
        repo = new NotificationRepository(app);
        refreshNotifications(); // تحميل أولي
    }

    public void refreshNotifications() {
        repo.fetchNotifications().observeForever(notifications::postValue);
    }

    public LiveData<List<UserNotification>> getNotifications() {
        return notifications;
    }

    public LiveData<Boolean> markRead(long id) {
        return repo.markRead(id);
    }

    public LiveData<Boolean> delete(long id) {
        return repo.delete(id);
    }
}