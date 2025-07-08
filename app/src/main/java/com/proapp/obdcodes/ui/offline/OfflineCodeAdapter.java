package com.proapp.obdcodes.ui.offline;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.local.entity.ObdCodeEntity;
import com.proapp.obdcodes.ui.code_details.CodeDetailsActivity;

import java.util.List;

public class OfflineCodeAdapter extends RecyclerView.Adapter<OfflineCodeAdapter.CodeViewHolder> {

    private final Context context;
    private final List<ObdCodeEntity> codeList;

    public OfflineCodeAdapter(Context context, List<ObdCodeEntity> codeList) {
        this.context = context;
        this.codeList = codeList;
    }

    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_code_offline, parent, false);
        return new CodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        ObdCodeEntity code = codeList.get(position);
        holder.tvCode.setText(code.code);
        holder.tvTitle.setText(code.title);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CodeDetailsActivity.class);
            intent.putExtra("CODE", code.code);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return codeList.size();
    }

    public void updateData(List<ObdCodeEntity> newList) {
        codeList.clear();
        codeList.addAll(newList);
        notifyDataSetChanged();
    }

    static class CodeViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode, tvTitle;

        public CodeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
