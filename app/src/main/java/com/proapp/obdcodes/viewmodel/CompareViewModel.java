package com.proapp.obdcodes.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.repository.ObdRepository;

public class CompareViewModel extends AndroidViewModel {
    private final ObdRepository repo;

    public CompareViewModel(@NonNull Application app) {
        super(app);
        repo = new ObdRepository(app);
    }

    /** يرجع تفاصيل كود بناءً على رمزه */
    public LiveData<ObdCode> getCodeDetail(String code) {
        return repo.getCodeDetail(code);
    }
}
