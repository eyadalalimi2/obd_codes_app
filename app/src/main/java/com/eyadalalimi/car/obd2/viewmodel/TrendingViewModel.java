package com.eyadalalimi.car.obd2.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.eyadalalimi.car.obd2.network.model.ObdCode;
import com.eyadalalimi.car.obd2.repository.ObdRepository;
import java.util.List;

public class TrendingViewModel extends AndroidViewModel {
    private final ObdRepository repo;
    private LiveData<List<ObdCode>> trending;

    public TrendingViewModel(@NonNull Application app) {
        super(app);
        repo = new ObdRepository(app);
    }

    public LiveData<List<ObdCode>> getTrendingCodes() {
        if (trending == null) {
            trending = repo.getTrendingCodes();
        }
        return trending;
    }
}
