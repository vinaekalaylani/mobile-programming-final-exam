package com.vinaekal.sisehat.network;

import com.vinaekal.sisehat.model.ApiResponse;
import com.vinaekal.sisehat.model.LoginContent;
import com.vinaekal.sisehat.model.ApiRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("user/login")
    Call<ApiResponse<LoginContent>> login(@Body ApiRequest request);
}
