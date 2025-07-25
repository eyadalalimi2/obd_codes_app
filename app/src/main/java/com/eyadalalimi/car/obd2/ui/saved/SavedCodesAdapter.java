package com.eyadalalimi.car.obd2.ui.saved;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eyadalalimi.car.obd2.R;

import java.util.List;

public class SavedCodesAdapter extends RecyclerView.Adapter<SavedCodesAdapter.VH> {

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

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_code, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        SavedCode it = items.get(pos);
        h.tvCode.setText(it.code);
        h.tvDesc.setText(it.description);
        h.tvDate.setText(it.dateTime);

        // صف التوسيط لكل شيء
        h.tvCode.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        h.tvDesc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // أيقونة عرض التفاصيل
        h.btnOpen.setOnClickListener(v -> listener.onOpen(pos));
        // أيقونة حذف
        h.btnDelete.setOnClickListener(v -> listener.onDelete(pos));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvDate, tvCode, tvDesc;
        ImageView btnDelete, btnOpen;

        VH(View v) {
            super(v);
            tvCode    = v.findViewById(R.id.tvItemCode);
            tvDesc    = v.findViewById(R.id.tvItemDesc);
            tvDate    = v.findViewById(R.id.tvItemDate);
            btnDelete = v.findViewById(R.id.btnDeleteItem);
            btnOpen   = v.findViewById(R.id.btnOpenItem);
        }
    }
}
