package com.proapp.obdcodes.ui.offline;

import android.os.Bundle;
import android.view.View;
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
    private View emptyView;
    private OfflineCodeAdapter adapter;
    private List<ObdCodeEntity> fullList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_offline_code_list);
        setTitle(getString(R.string.title_offline_codes));

        recyclerView = findViewById(R.id.rvOfflineCodes);
        etSearch      = findViewById(R.id.etSearch);
        emptyView     = findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OfflineCodeAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        setupSearchBox();
        loadCodesFromLocal();
    }

    private void loadCodesFromLocal() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ObdCodeEntity> codeList = AppDatabase
                    .getInstance(this)
                    .obdCodeDao()
                    .getAllCodesRaw();

            runOnUiThread(() -> {
                fullList.clear();
                fullList.addAll(codeList);
                adapter.updateData(fullList);
                toggleEmptyView();
            });
        });
    }

    private void setupSearchBox() {
        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }
            @Override public boolean onQueryTextChange(String newText) {
                String q = newText.toLowerCase().trim();
                List<ObdCodeEntity> filtered = new ArrayList<>();
                for (ObdCodeEntity code : fullList) {
                    if (code.code.toLowerCase().contains(q) ||
                            code.title.toLowerCase().contains(q)) {
                        filtered.add(code);
                    }
                }
                adapter.updateData(filtered);
                toggleEmptyView();
                return true;
            }
        });
    }

    private void toggleEmptyView() {
        if (adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean shouldShowBottomNav() {
        return false;
    }
}
