package com.proapp.obdcodes.ui.saved;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;

import java.util.List;

public class SavedCodesAdapter
        extends RecyclerView.Adapter<SavedCodesAdapter.VH> {

    public interface Listener {
        void onDelete(int position);
        void onOpen(int position);
    }

    private final List<SavedCode> items;
    private final Listener listener;

    public SavedCodesAdapter(List<SavedCode> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_code, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int pos) {
        SavedCode it = items.get(pos);
        h.tvDate.setText(it.dateTime);
        h.tvCode.setText(it.code);
        h.tvDesc.setText(it.description);
        h.btnDelete.setOnClickListener(v -> listener.onDelete(pos));
        h.btnOpen.setOnClickListener(v -> listener.onOpen(pos));
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvDate, tvCode, tvDesc;
        ImageView btnDelete, btnOpen;
        VH(View v) {
            super(v);
            tvDate   = v.findViewById(R.id.tvItemDate);
            tvCode   = v.findViewById(R.id.tvItemCode);
            tvDesc   = v.findViewById(R.id.tvItemDesc);
            btnDelete= v.findViewById(R.id.btnDeleteItem);
            btnOpen  = v.findViewById(R.id.btnOpenItem);
        }
    }
}
