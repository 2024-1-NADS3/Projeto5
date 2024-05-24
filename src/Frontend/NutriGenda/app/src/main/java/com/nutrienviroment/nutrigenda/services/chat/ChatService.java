package com.nutrienviroment.nutrigenda.services.chat;

import com.nutrienviroment.nutrigenda.models.chat.ChatRequest;
import com.nutrienviroment.nutrigenda.models.chat.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatService {
    @POST("/chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest request);
}
