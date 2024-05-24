package com.nutrienviroment.nutrigenda.services.chat;

import android.content.Context;
import android.widget.Toast;

import com.nutrienviroment.nutrigenda.models.chat.ChatRequest;
import com.nutrienviroment.nutrigenda.models.chat.ChatResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatRepository {
    private ChatService chatService;

    public ChatRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nutrigendaapi.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        chatService = retrofit.create(ChatService.class);
    }

    public void sendMessage(String message, Context context, final ChatCallback callback) {
        ChatRequest request = new ChatRequest();
        request.setMessage(message);

        Call<ChatResponse> call = chatService.sendMessage(request);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro ao obter resposta do servidor.");
                    Toast.makeText(context, "Erro no envio da mensagem: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                callback.onError(t.getMessage());
                Toast.makeText(context, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface ChatCallback {
        void onSuccess(ChatResponse response);
        void onError(String error);
    }
}
