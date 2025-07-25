package com.eyadalalimi.car.obd2.ui.notifications;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.network.model.UserNotification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.VH> {

    public interface Listener {
        void onMarkRead(long id);
        void onDelete(long id);
    }

    private final List<UserNotification> items = new ArrayList<>();
    private Listener listener;

    public void setListener(Listener l) {
        this.listener = l;
    }

    public void submitList(List<UserNotification> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        UserNotification un = items.get(position);
        Locale locale = Locale.getDefault();

        holder.tvTitle.setText(un.getNotification().getTitle());
        holder.tvMessage.setText(un.getNotification().getMessage());
        holder.tvDate.setText(formatDate(un.getCreatedAt(), locale));

        boolean unread = un.getReadAt() == null;
        holder.tvTitle.setTypeface(null, unread ? Typeface.BOLD : Typeface.NORMAL);

        holder.btnMarkRead.setOnClickListener(v -> {
            if (listener != null) listener.onMarkRead(un.getId());
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(un.getId());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvDate;
        Button btnMarkRead, btnDelete;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnMarkRead = itemView.findViewById(R.id.btnMarkRead);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private String formatDate(String isoDate, Locale locale) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US);
            isoFormat.setLenient(false);
            Date date = isoFormat.parse(isoDate);

            long diffMillis = System.currentTimeMillis() - date.getTime();
            long minutes = diffMillis / (60 * 1000);
            long hours = diffMillis / (60 * 60 * 1000);
            long days = diffMillis / (24 * 60 * 60 * 1000);

            if (minutes < 1)
                return locale.getLanguage().equals("ar") ? "الآن" : "just now";
            else if (minutes < 60)
                return locale.getLanguage().equals("ar") ? "منذ " + minutes + " دقيقة" : minutes + " minutes ago";
            else if (hours < 24)
                return locale.getLanguage().equals("ar") ? "منذ " + hours + " ساعة" : hours + " hours ago";
            else if (days == 1)
                return locale.getLanguage().equals("ar") ? "أمس" : "Yesterday";
            else if (days < 4)
                return locale.getLanguage().equals("ar") ? "منذ " + days + " أيام" : days + " days ago";
            else {
                SimpleDateFormat full = new SimpleDateFormat("d MMMM yyyy - hh:mm a", locale);
                return full.format(date);
            }

        } catch (Exception e) {
            return isoDate;
        }
    }
}
