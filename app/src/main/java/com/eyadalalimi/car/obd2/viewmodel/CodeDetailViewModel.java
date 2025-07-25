package com.eyadalalimi.car.obd2.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eyadalalimi.car.obd2.network.model.ObdCode;
import com.eyadalalimi.car.obd2.repository.CodeDetailRepository;

public class CodeDetailViewModel extends AndroidViewModel {
    private final CodeDetailRepository repository;
    private MutableLiveData<ObdCode> codeDetail = new MutableLiveData<>();

    public CodeDetailViewModel(@NonNull Application app) {
        super(app);
        repository = new CodeDetailRepository(app);
    }

    /**
     * Load the code details, choosing the appropriate API (base or translations)
     * based on the provided language code.
     *
     * @param code     the OBD code string (e.g. "B0001")
     * @param langCode the ISO language code (e.g. "en", "ar", "fr", "tr")
     */
    public void loadCodeDetail(String code, String langCode) {
        codeDetail = repository.fetchCodeDetail(code, langCode);
    }

    public LiveData<ObdCode> getCodeDetail() {
        return codeDetail;
    }
}
