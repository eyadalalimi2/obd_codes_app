package com.eyadalalimi.car.obd2.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eyadalalimi.car.obd2.R;
import com.eyadalalimi.car.obd2.network.model.ChatMessage;
import com.eyadalalimi.car.obd2.network.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_USER      = 1;
    private static final int VIEW_ASSISTANT = 2;
    private static final int VIEW_TYPING    = 3;

    private User user;
    private static final String IMAGE_BASE_URL = "https://obdcodehub.com/storage/";
    private final List<ChatMessage> messages = new ArrayList<>();

    public ChatAdapter(List<ChatMessage> initial, User user) {
        if (initial != null) messages.addAll(initial);
        this.user = user;
    }

    /** تحديث القائمة بالكامل من قاعدة البيانات */
    public void setMessages(List<ChatMessage> msgs) {
        messages.clear();
        if (msgs != null) messages.addAll(msgs);
        notifyDataSetChanged();
    }

    // تحديث بيانات المستخدم ديناميكياً
    public void setUser(User user) {
        this.user = user;
        notifyDataSetChanged();
    }

    public void append(ChatMessage msg) {
        messages.add(msg);
        notifyItemInserted(messages.size() - 1);
    }

    public void removeLast() {
        if (!messages.isEmpty()) {
            int pos = messages.size() - 1;
            messages.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String role = messages.get(position).getRole();
        if ("typing".equals(role)) return VIEW_TYPING;
        return "user".equals(role) ? VIEW_USER : VIEW_ASSISTANT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_USER) {
            View v = inflater.inflate(R.layout.item_chat_user, parent, false);
            return new UserHolder(v);
        } else if (viewType == VIEW_ASSISTANT) {
            View v = inflater.inflate(R.layout.item_chat_assistant, parent, false);
            return new AssistantHolder(v);
        } else {
            View v = inflater.inflate(R.layout.item_typing, parent, false);
            return new TypingHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        if (holder instanceof UserHolder) {
            ((UserHolder) holder).bind(msg, user);
        } else if (holder instanceof AssistantHolder) {
            ((AssistantHolder) holder).bind(msg);
        }
        // TypingHolder: لا يحتاج ربط
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ViewHolder لرسائل المستخدم (مع صورة المستخدم)
    static class UserHolder extends RecyclerView.ViewHolder {
        private final TextView tvContent, tvTimestamp;
        private final ImageView ivUser;

        UserHolder(@NonNull View itemView) {
            super(itemView);
            tvContent   = itemView.findViewById(R.id.tvMessageContent);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivUser      = itemView.findViewById(R.id.ivUser);
        }

        void bind(ChatMessage msg, User user) {
            tvContent.setText(msg.getContent());
            tvTimestamp.setText(msg.getTimestamp());
            if (user != null && ivUser != null) {
                String img = user.getProfileImage();
                if (img != null && !img.isEmpty()) {
                    if (!img.startsWith("http")) img = IMAGE_BASE_URL + img;
                    Glide.with(ivUser.getContext())
                            .load(img)
                            .placeholder(R.drawable.profile)
                            .error(R.drawable.profile)
                            .into(ivUser);
                } else {
                    ivUser.setImageResource(R.drawable.profile);
                }
            }
        }
    }

    // ViewHolder لرسائل المساعد (صورة ثابتة)
    static class AssistantHolder extends RecyclerView.ViewHolder {
        private final TextView tvContent, tvTimestamp;
        private final ImageView ivAssistant;

        AssistantHolder(@NonNull View itemView) {
            super(itemView);
            tvContent    = itemView.findViewById(R.id.tvMessageContent);
            tvTimestamp  = itemView.findViewById(R.id.tvTimestamp);
            ivAssistant  = itemView.findViewById(R.id.ivAssistant);
        }

        void bind(ChatMessage msg) {
            tvContent.setText(msg.getContent());
            tvTimestamp.setText(msg.getTimestamp());
            if (ivAssistant != null) {
                ivAssistant.setImageResource(R.drawable.icon_ai_1);
            }
        }
    }

    // ViewHolder لمؤشر الكتابة (Typing)
    static class TypingHolder extends RecyclerView.ViewHolder {
        TypingHolder(@NonNull View itemView) {
            super(itemView);
            // لا عناصر إضافية
        }
    }
}
