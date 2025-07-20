package com.proapp.obdcodes.ui.plans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.network.model.Plan;
import com.proapp.obdcodes.util.FeatureMapper;

import java.util.List;

public class PlanRecyclerAdapter extends RecyclerView.Adapter<PlanRecyclerAdapter.PlanViewHolder> {

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

        if ("Pro".equalsIgnoreCase(plan.getName()) || "Premium".equalsIgnoreCase(plan.getName())) {
            holder.tvBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvBadge.setVisibility(View.GONE);
        }

        holder.featuresContainer.removeAllViews();
        List<String> features = plan.getFeatures();
        if (features == null || features.isEmpty()) {
            holder.featuresContainer.addView(createFeatureTextView("لا توجد مميزات مذكورة"));
        } else {
            for (String key : features) {
                holder.featuresContainer.addView(
                        createFeatureTextView("• " + FeatureMapper.toReadable(key))
                );
            }
        }

        holder.btnActivateWithCode.setOnClickListener(v -> listener.onActivateCodeClick(plan));
        holder.btnBuyWithGoogle.setOnClickListener(v -> listener.onBuyWithGoogleClick(plan));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPlanName;
        final TextView tvPrice;
        final TextView tvBadge;
        final LinearLayout featuresContainer;
        final Button btnActivateWithCode;
        final Button btnBuyWithGoogle;

        PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvPlanName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvBadge = itemView.findViewById(R.id.tvBadge);
            featuresContainer = itemView.findViewById(R.id.tvFeatures);
            btnActivateWithCode = itemView.findViewById(R.id.btnActivateWithCode);
            btnBuyWithGoogle = itemView.findViewById(R.id.btnBuyWithGoogle);
        }
    }

    private TextView createFeatureTextView(String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(14);
        tv.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
        tv.setPadding(0, 4, 0, 4);
        return tv;
    }
}
