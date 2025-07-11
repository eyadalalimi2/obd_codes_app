package com.proapp.obdcodes.ui.chat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proapp.obdcodes.R;
import com.proapp.obdcodes.local.entity.ChatMessageEntity;
import com.proapp.obdcodes.network.model.ChatMessage;
import com.proapp.obdcodes.network.model.ChatResponse;
import com.proapp.obdcodes.network.model.User;
import com.proapp.obdcodes.ui.home.HomeActivity;
import com.proapp.obdcodes.viewmodel.AiChatViewModel;
import com.proapp.obdcodes.viewmodel.ChatRoomViewModel;
import com.proapp.obdcodes.viewmodel.UserViewModel;
import com.proapp.obdcodes.util.SubscriptionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private AiChatViewModel aiViewModel;
    private ChatRoomViewModel chatRoomViewModel;
    private ChatAdapter adapter;
    private RecyclerView rvChat;
    private EditText etMessage;
    private ImageButton btnSend;
    private User currentUser;
    private boolean isTypingShown = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ التحقق من صلاحية الوصول إلى الميزة
        SubscriptionUtils.hasFeature(this, "AI_DIAGNOSTIC_ASSISTANT", isAllowed -> {
            if (!isAllowed) {
                Toast.makeText(this, "هذه الميزة متاحة فقط للمشتركين", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // تابع التحميل فقط إذا كانت الميزة مفعّلة
            initializeChatUI();
        });
    }

    private void initializeChatUI() {
        setContentView(R.layout.activity_chat);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> onBackPressed());

        ImageView ivAvatar = findViewById(R.id.iv_avatar);
        ivAvatar.setImageResource(R.drawable.icon_ai_2);

        ImageButton btnClearChat = findViewById(R.id.btn_clear_chat);
        btnClearChat.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm_delete, null);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create();

            dialogView.findViewById(R.id.btnDelete).setOnClickListener(v2 -> {
                chatRoomViewModel.clearChatHistory();
                adapter.setMessages(new ArrayList<>());
                dialog.dismiss();
            });
            dialogView.findViewById(R.id.btnCancel).setOnClickListener(v2 -> dialog.dismiss());
            dialog.show();
        });

        rvChat = findViewById(R.id.rv_chat);
        etMessage = findViewById(R.id.et_chat_message);
        btnSend = findViewById(R.id.btn_send);

        adapter = new ChatAdapter(new ArrayList<>(), null);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        chatRoomViewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        chatRoomViewModel.getAllMessages().observe(this, this::renderLocalMessages);

        aiViewModel = new ViewModelProvider(this).get(AiChatViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserProfile().observe(this, user -> {
            if (user != null) {
                currentUser = user;
                adapter.setUser(user);
            }
        });

        aiViewModel.getResponse().observe(this, this::handleResponse);
        btnSend.setOnClickListener(v -> sendMessage());

        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void renderLocalMessages(List<ChatMessageEntity> roomList) {
        List<ChatMessage> messages = new ArrayList<>();
        for (ChatMessageEntity ent : roomList) {
            messages.add(new ChatMessage(ent.getRole(), ent.getContent(), ent.getTimestamp()));
        }

        adapter.setMessages(messages);
        scrollToBottom();
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "يرجى إدخال رسالة", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentUser == null) {
            Toast.makeText(this, "لم يتم تحميل بيانات المستخدم بعد", Toast.LENGTH_SHORT).show();
            return;
        }

        ChatMessage userMsg = new ChatMessage("user", text, currentTime());
        adapter.append(userMsg);
        chatRoomViewModel.insert(new ChatMessageEntity("user", text, userMsg.getTimestamp()));
        scrollToBottom();

        if (!isTypingShown) {
            adapter.append(new ChatMessage("typing", "", ""));
            isTypingShown = true;
            scrollToBottom();
        }

        aiViewModel.send(text);
        etMessage.setText("");
    }

    private void handleResponse(ChatResponse cr) {
        if (isTypingShown) {
            adapter.removeLast();
            isTypingShown = false;
        }

        if (cr.getError() != null) {
            Toast.makeText(this, cr.getError(), Toast.LENGTH_LONG).show();
            return;
        }

        String reply = cr.getReply();
        if (reply != null && !reply.isEmpty()) {
            ChatMessage aiMsg = new ChatMessage("assistant", reply, currentTime());
            adapter.append(aiMsg);
            chatRoomViewModel.insert(new ChatMessageEntity("assistant", reply, aiMsg.getTimestamp()));
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        int count = adapter.getItemCount();
        if (count > 0) {
            rvChat.scrollToPosition(count - 1);
        }
    }

    private String currentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
