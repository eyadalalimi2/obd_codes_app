package com.proapp.obdcodes.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.repository.ObdRepository;
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
