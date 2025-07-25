package com.eyadalalimi.car.obd2.ui.analytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.network.model.ObdCode;
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
        ObdCode code = items.get(position);
        holder.tvCode.setText(code.getCode());
        holder.tvTitle.setText(code.getTitle());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(code);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCode, tvTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // هذه المعرفات يجب أن تتطابق مع item_trending_code.xml
            tvCode  = itemView.findViewById(R.id.tvItemCode);
            tvTitle = itemView.findViewById(R.id.tvItemTitle);
        }
    }
}
