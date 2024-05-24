package com.nutrienviroment.nutrigenda.screens.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.chat.ChatResponse;
import com.nutrienviroment.nutrigenda.services.chat.ChatRepository;

public class ChatScreen extends AppCompatActivity {

    private EditText userMessageEditText;
    private LinearLayout chatContainer;
    private ChatRepository chatRepository;
    private TextView thinkingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_chat_screen);

        userMessageEditText = findViewById(R.id.userMessageEditText);
        Button sendMessageButton = findViewById(R.id.sendMessageButton);
        chatContainer = findViewById(R.id.chatContainer);

        chatRepository = new ChatRepository();

        sendMessageButton.setOnClickListener(v -> {
            String message = userMessageEditText.getText().toString();
            if (!message.isEmpty()) {
                addMessageToChat("Você: " + message, true);
                sendMessage(message);
                userMessageEditText.setText("");
            }
        });
    }

    private void sendMessage(String message) {
        showThinkingMessage(true);
        chatRepository.sendMessage(message, this, new ChatRepository.ChatCallback() {
            @Override
            public void onSuccess(ChatResponse response) {
                showThinkingMessage(false);
                addMessageToChat("IA: " + response.getReply(), false);
            }

            @Override
            public void onError(String error) {
                showThinkingMessage(false);
                addMessageToChat("Erro: " + error, false);
            }
        });
    }

    private void showThinkingMessage(boolean show) {
        if (show) {
            thinkingTextView = new TextView(this);
            thinkingTextView.setText("Pensando...");
            thinkingTextView.setPadding(16, 8, 16, 8);
            thinkingTextView.setTextColor(ContextCompat.getColor(this, R.color.gray));
            thinkingTextView.setBackgroundResource(R.drawable.bot_message_background);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 8, 8, 8);
            thinkingTextView.setLayoutParams(params);
            chatContainer.addView(thinkingTextView);

            final ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        } else {
            chatContainer.removeView(thinkingTextView);
        }
    }

    private void addMessageToChat(String message, boolean isUser) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setPadding(16, 8, 16, 8);
        textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        if (isUser) {
            textView.setBackgroundResource(R.drawable.user_message_background);
            textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            textView.setBackgroundResource(R.drawable.bot_message_background);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        textView.setLayoutParams(params);
        chatContainer.addView(textView);

        final ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}
