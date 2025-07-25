package com.eyadalalimi.car.obd2.ui.offline;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.local.entity.ObdCodeEntity;
import com.eyadalalimi.car.obd2.ui.code_details.CodeDetailsActivity;
import java.util.List;

public class OfflineCodeAdapter
        extends RecyclerView.Adapter<OfflineCodeAdapter.CodeViewHolder> {

    private final Context context;
    private final List<ObdCodeEntity> codeList;

    public OfflineCodeAdapter(Context ctx, List<ObdCodeEntity> list) {
        this.context = ctx;
        this.codeList = list;
    }

    @NonNull @Override
    public CodeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_code_offline, parent, false);
        return new CodeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CodeViewHolder holder, int position) {
        ObdCodeEntity code = codeList.get(position);
        holder.tvCode.setText(code.code);
        holder.tvTitle.setText(code.title);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(
                    context, CodeDetailsActivity.class
            );
            intent.putExtra("CODE", code.code);
            context.startActivity(intent);
        });
    }

    @Override public int getItemCount() { return codeList.size(); }

    public void updateData(List<ObdCodeEntity> newList) {
        codeList.clear();
        codeList.addAll(newList);
        notifyDataSetChanged();
    }

    static class CodeViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCode, tvTitle;
        CodeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode  = itemView.findViewById(R.id.tvCode);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
