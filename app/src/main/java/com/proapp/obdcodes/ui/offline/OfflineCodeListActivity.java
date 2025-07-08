package com.proapp.obdcodes.ui.offline;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.local.AppDatabase;
import com.proapp.obdcodes.local.entity.ObdCodeEntity;
import com.proapp.obdcodes.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class OfflineCodeListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SearchView etSearch;
    private OfflineCodeAdapter adapter;
    private List<ObdCodeEntity> fullList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_offline_code_list);
        setTitle("الأكواد المحفوظة");

        recyclerView = findViewById(R.id.rvOfflineCodes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etSearch = findViewById(R.id.etSearch);

        setupSearchBox();
        loadCodesFromLocal();
    }

    private void loadCodesFromLocal() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ObdCodeEntity> codeList = AppDatabase.getInstance(this)
                    .obdCodeDao()
                    .getAllCodesRaw();

            runOnUiThread(() -> {
                fullList.clear();
                fullList.addAll(codeList);
                adapter = new OfflineCodeAdapter(this, new ArrayList<>(fullList));
                recyclerView.setAdapter(adapter);
            });
        });
    }

    private void setupSearchBox() {
        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // لا نحتاج إلى تنفيذ شيء عند الإرسال
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = newText.toLowerCase().trim();
                List<ObdCodeEntity> filtered = new ArrayList<>();
                for (ObdCodeEntity code : fullList) {
                    if (code.code.toLowerCase().contains(query) ||
                            code.title.toLowerCase().contains(query)) {
                        filtered.add(code);
                    }
                }
                if (adapter != null) adapter.updateData(filtered);
                return true;
            }
        });
    }
}
