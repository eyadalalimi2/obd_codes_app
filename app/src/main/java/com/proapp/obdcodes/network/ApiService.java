package com.proapp.obdcodes.network;

import com.proapp.obdcodes.network.model.ActivationRequest;
import com.proapp.obdcodes.network.model.ChatRequest;
import com.proapp.obdcodes.network.model.ChatResponse;
import com.proapp.obdcodes.network.model.CompareResult;
import com.proapp.obdcodes.network.model.EncryptionKeyResponse;
import com.proapp.obdcodes.network.model.GoogleLoginRequest;
import com.proapp.obdcodes.network.model.LoginRequest;
import com.proapp.obdcodes.network.model.LoginResponse;
import com.proapp.obdcodes.network.model.NotificationResponse;
import com.proapp.obdcodes.network.model.ObdCode;
import com.proapp.obdcodes.network.model.Plan;
import com.proapp.obdcodes.network.model.RegisterRequest;
import com.proapp.obdcodes.network.model.RegisterResponse;
import com.proapp.obdcodes.network.model.Subscription;
import com.proapp.obdcodes.network.model.SubscriptionRequest;
import com.proapp.obdcodes.network.model.UpdateProfileRequest;
import com.proapp.obdcodes.network.model.UserProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ---------------- المصادقة ----------------
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("login/google")
    Call<LoginResponse> loginWithGoogle(@Body GoogleLoginRequest request);

    @POST("logout")
    Call<Void> logout();


    // ---------------- الحساب ----------------
    @GET("user/profile")
    @Headers("Accept: application/json")
    Call<UserProfileResponse> getUserProfile();

    @PUT("user/profile/update")
    @Headers("Accept: application/json")
    Call<UserProfileResponse> updateUserProfile(@Body UpdateProfileRequest request);

    @DELETE("user/profile/delete")
    Call<Void> deleteAccount();

    @POST("email/verification-notification")
    Call<Void> sendVerificationNotification();


    // ---------------- الإشعارات ----------------
    @GET("notifications")
    Call<NotificationResponse> getNotifications();

    @POST("notifications/{id}/read")
    Call<Void> markNotificationRead(@Path("id") long id);

    @DELETE("notifications/{id}")
    Call<Void> deleteNotification(@Path("id") long id);


    // ---------------- الأكواد ----------------
    @GET("obd/codes/all")
    Call<List<ObdCode>> getAllCodes();

    @GET("codes/{code}")
    Call<ObdCode> getCodeDetail(@Path("code") String code);

    @GET("codes/trending")
    Call<List<ObdCode>> getTrendingCodes();

    @GET("codes/{id}/compare/{other_id}")
    Call<CompareResult> compareCodes(
            @Path("id") long firstId,
            @Path("other_id") long secondId
    );


    // ---------------- الذكاء الاصطناعي ----------------
    @POST("ai/chat")
    @Headers("Accept: application/json")
    Call<ChatResponse> chat(@Body ChatRequest request);


    // ---------------- التشفير ----------------
    @GET("app/key")
    Call<EncryptionKeyResponse> getEncryptionKey(
            @Query("package") String packageName,
            @Query("version") String versionCode
    );


    // ---------------- الاشتراكات ----------------


    // 1. جلب جميع الباقات المتاحة
    @GET("plans")
    Call<List<Plan>> getAllPlans();

    // 2. جلب تفاصيل باقة محددة عبر ID
    @GET("plans/{id}")
    Call<Plan> getPlanById(@Path("id") int id);

    // 3. الاشتراك في باقة (عن طريق رمز الشراء من Google Play أو Apple)
    @POST("subscribe")
    Call<Subscription> subscribeToPlan(@Body SubscriptionRequest request);

    // 4. تفعيل باقة عن طريق كود يدوي
    @POST("activate-code")
    Call<Subscription> activatePlanByCode(@Body ActivationRequest request);

    // 5. عرض حالة الاشتراك الحالي (نشط / منتهي / لا يوجد)
    @GET("subscription/status")
    Call<Subscription> getCurrentSubscriptionStatus();

    // 6. تجديد الاشتراك الحالي يدويًا
    @POST("subscription/renew")
    Call<Subscription> renewSubscription(@Body SubscriptionRequest request);

    // 7. إلغاء الاشتراك الحالي
    @POST("subscription/cancel")
    Call<Subscription> cancelSubscription(@Body SubscriptionRequest request);
}