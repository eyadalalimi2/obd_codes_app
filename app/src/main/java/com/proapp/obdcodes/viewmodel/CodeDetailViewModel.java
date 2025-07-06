package com.proapp.obdcodes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.proapp.obdcodes.network.model.CodeDetailResponse;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.repository.CodeDetailRepository;

public class CodeDetailViewModel extends AndroidViewModel {
    private final CodeDetailRepository repository;
    private MutableLiveData<ObdCode> codeDetail = new MutableLiveData<>();

    public CodeDetailViewModel(@NonNull Application app) {
        super(app);
        repository = new CodeDetailRepository(app);
    }

    public void loadCodeDetail(String code) {
        codeDetail = repository.fetchCodeDetail(code);
    }

    public LiveData<ObdCode> getCodeDetail() {
        return codeDetail;
    }
}
