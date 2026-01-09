package com.vinaekal.sisehat.network;

import com.vinaekal.sisehat.model.content.BookingContent;
import com.vinaekal.sisehat.model.content.RegisterContent;
import com.vinaekal.sisehat.model.request.BookingRequest;
import com.vinaekal.sisehat.model.request.RegisterRequest;
import com.vinaekal.sisehat.model.response.ApiResponse;
import com.vinaekal.sisehat.model.content.LoginContent;
import com.vinaekal.sisehat.model.request.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("user/login")
    Call<ApiResponse<LoginContent>> login(@Body LoginRequest request);

    @POST("user/register")
    Call<ApiResponse<RegisterContent>> register(@Body RegisterRequest request);

    @POST("user/booking")
    Call<ApiResponse<BookingContent>> createBooking(@Body BookingRequest request);


}
