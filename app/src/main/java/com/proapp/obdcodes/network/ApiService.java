package com.proapp.obdcodes.network;

import com.proapp.obdcodes.network.model.ChatRequest;
import com.proapp.obdcodes.network.model.ChatResponse;
import com.proapp.obdcodes.network.model.CompareResult;
import com.proapp.obdcodes.network.model.GoogleLoginRequest;
import com.proapp.obdcodes.network.model.LoginRequest;
import com.proapp.obdcodes.network.model.LoginResponse;
import com.proapp.obdcodes.network.model.NotificationResponse;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.network.model.RegisterRequest;
import com.proapp.obdcodes.network.model.RegisterResponse;
import com.proapp.obdcodes.network.model.UpdateProfileRequest;
import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.network.model.UserProfileResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest body);

    @POST("register")
    Call<RegisterResponse> register(@Body RegisterRequest body);

    @POST("login/google")
    Call<LoginResponse> loginWithGoogle(@Body GoogleLoginRequest body);
    /** تسجيل الخروج */
    @POST("logout")
    Call<Void> logout();

    /** حذف حساب المستخدم */
    @DELETE("user/profile/delete")
    Call<Void> deleteAccount();
    @Headers("Accept: application/json")
    @GET("user/profile")
    Call<UserProfileResponse> getUserProfile();

    @Headers("Accept: application/json")
    @PUT("user/profile/update")
    Call<UserProfileResponse> updateUserProfile(@Body UpdateProfileRequest request);

    // إعادة إرسال رابط تفعيل البريد
    @POST("email/verification-notification")
    Call<Void> sendVerificationNotification();

    // أمثلة لنقطة نهاية الباقات
    @GET("subscriptions")
    Call<List<Subscription>> getSubscriptions();
    @GET("notifications")
    Call<NotificationResponse> getNotifications();

    @POST("notifications/{id}/read")
    Call<Void> markNotificationRead(@Path("id") long id);

    @DELETE("notifications/{id}")
    Call<Void> deleteNotification(@Path("id") long id);
    @GET("codes/{code}")
    Call<ObdCode> getCodeDetail(@Path("code") String code);
    @GET("codes/trending")
    Call<List<ObdCode>> getTrendingCodes();

    @GET("codes/{id}/compare/{other_id}")
    Call<CompareResult> compareCodes(
            @Path("id") long firstId,
            @Path("other_id") long secondId
    );
    @Headers("Accept: application/json")
    @POST("ai/chat")
    Call<ChatResponse> chat(@Body ChatRequest request);


}

