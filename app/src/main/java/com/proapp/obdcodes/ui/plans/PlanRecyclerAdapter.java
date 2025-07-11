package com.proapp.obdcodes.ui.plans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Plan;
import com.proapp.obdcodes.util.FeatureMapper;

import java.util.List;

public class PlanRecyclerAdapter
        extends RecyclerView.Adapter<PlanRecyclerAdapter.PlanViewHolder> {

    public interface OnPlanClickListener {
        void onActivateCodeClick(@NonNull Plan plan);
        void onBuyWithGoogleClick(@NonNull Plan plan);
    }

    private final Context context;
    private final List<Plan> plans;
    private final OnPlanClickListener listener;

    public PlanRecyclerAdapter(
            @NonNull Context context,
            @NonNull List<Plan> plans,
            @NonNull OnPlanClickListener listener
    ) {
        this.context = context;
        this.plans = plans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = plans.get(position);

        holder.tvPlanName.setText("اسم الباقة: " + plan.getName());
        holder.tvPrice.setText("السعر: " + plan.getFormattedPrice());



        // المميزات
        List<String> keys = plan.getFeatures();
        if (keys == null || keys.isEmpty()) {
            holder.tvFeatures.setText("المميزات:\nلا توجد مميزات مذكورة");
        } else {
            StringBuilder sb = new StringBuilder("المميزات:\n");
            for (String key : keys) {
                sb.append("- ")
                        .append(FeatureMapper.toReadable(key))
                        .append("\n");
            }
            holder.tvFeatures.setText(sb.toString().trim());
        }

        holder.btnActivateWithCode.setOnClickListener(v ->
                listener.onActivateCodeClick(plan)
        );
        holder.btnBuyWithGoogle.setOnClickListener(v ->
                listener.onBuyWithGoogleClick(plan)
        );
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanName, tvPrice, tvDuration, tvFeatures;
        Button btnActivateWithCode, btnBuyWithGoogle;

        PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlanName          = itemView.findViewById(R.id.tvPlanName);
            tvPrice             = itemView.findViewById(R.id.tvPrice);
            tvFeatures          = itemView.findViewById(R.id.tvFeatures);
            btnActivateWithCode = itemView.findViewById(R.id.btnActivateWithCode);
            btnBuyWithGoogle    = itemView.findViewById(R.id.btnBuyWithGoogle);
        }
    }
}
