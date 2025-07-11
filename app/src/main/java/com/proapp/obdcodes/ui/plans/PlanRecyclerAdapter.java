// com.proapp.obdcodes.ui.plans/PlanRecyclerAdapter.java
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
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int pos) {
        Plan plan = plans.get(pos);
        holder.tvPlanName.setText(plan.getName());
        holder.tvPrice.setText(plan.getFormattedPrice());
        holder.tvFeatures.setText(plan.getFeaturesText());
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
        TextView tvPlanName, tvPrice, tvFeatures;
        Button btnActivateWithCode, btnBuyWithGoogle;

        PlanViewHolder(@NonNull View iv) {
            super(iv);
            tvPlanName          = iv.findViewById(R.id.tvPlanName);
            tvPrice             = iv.findViewById(R.id.tvPrice);
            tvFeatures          = iv.findViewById(R.id.tvFeatures);
            btnActivateWithCode = iv.findViewById(R.id.btnActivateWithCode);
            btnBuyWithGoogle    = iv.findViewById(R.id.btnBuyWithGoogle);
        }
    }
}
