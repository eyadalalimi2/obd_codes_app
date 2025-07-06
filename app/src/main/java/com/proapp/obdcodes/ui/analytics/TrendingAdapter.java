package com.proapp.obdcodes.ui.analytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.ObdCode;
import java.util.ArrayList;
import java.util.List;

public class TrendingAdapter
        extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ObdCode code);
    }

    private final List<ObdCode> items = new ArrayList<>();
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    public void setItems(List<ObdCode> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trending_code, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {
        ObdCode c = items.get(position);
        holder.tvCode.setText(c.getCode());
        holder.tvTitle.setText(c.getTitle());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(c);
        });
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCode, tvTitle;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode  = itemView.findViewById(R.id.tvTrendingCode);
            tvTitle = itemView.findViewById(R.id.tvTrendingTitle);
        }
    }
}
