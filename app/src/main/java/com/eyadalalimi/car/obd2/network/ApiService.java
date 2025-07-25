package com.eyadalalimi.car.obd2.network;

import com.eyadalalimi.car.obd2.network.model.ActivationRequest;
import com.eyadalalimi.car.obd2.network.model.AddCarRequest;
import com.eyadalalimi.car.obd2.network.model.AddCarResponse;
import com.eyadalalimi.car.obd2.network.model.BrandModelsResponse;
import com.eyadalalimi.car.obd2.network.model.BrandsResponse;
import com.eyadalalimi.car.obd2.network.model.ChatRequest;
import com.eyadalalimi.car.obd2.network.model.ChatResponse;
import com.eyadalalimi.car.obd2.network.model.CompareResult;
import com.eyadalalimi.car.obd2.network.model.DeleteCarResponse;
import com.eyadalalimi.car.obd2.network.model.EncryptionKeyResponse;
import com.eyadalalimi.car.obd2.network.model.ForgotPasswordRequest;
import com.eyadalalimi.car.obd2.network.model.GoogleLoginRequest;
import com.eyadalalimi.car.obd2.network.model.LoginRequest;
import com.eyadalalimi.car.obd2.network.model.LoginResponse;
import com.eyadalalimi.car.obd2.network.model.MessageResponse;
import com.eyadalalimi.car.obd2.network.model.NotificationResponse;
import com.eyadalalimi.car.obd2.network.model.ObdCode;
import com.eyadalalimi.car.obd2.network.model.ObdCodeTranslation;
import com.eyadalalimi.car.obd2.network.model.Plan;
import com.eyadalalimi.car.obd2.network.model.RegisterRequest;
import com.eyadalalimi.car.obd2.network.model.RegisterResponse;
import com.eyadalalimi.car.obd2.network.model.ResetPasswordRequest;
import com.eyadalalimi.car.obd2.network.model.Subscription;
import com.eyadalalimi.car.obd2.network.model.SubscriptionRequest;
import com.eyadalalimi.car.obd2.network.model.UpdateCarRequest;
import com.eyadalalimi.car.obd2.network.model.UpdateCarsResponse;
import com.eyadalalimi.car.obd2.network.model.UpdateProfileRequest;
import com.eyadalalimi.car.obd2.network.model.UserCarsResponse;
import com.eyadalalimi.car.obd2.network.model.UserProfileResponse;
import com.eyadalalimi.car.obd2.network.model.VerifyStatusResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

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

    @POST("user/profile/update")
    @Headers("Accept: application/json")
    Call<UserProfileResponse> updateProfileData(@Body UpdateProfileRequest request);

    @Multipart
    @POST("user/profile/update_image")
    @Headers("Accept: application/json")
    Call<UserProfileResponse> updateProfileAvatar(@Part MultipartBody.Part profile_image);

    // ---------------- نسيت كلمة المرور ----------------
    @POST("password/forgot")
    @Headers("Accept: application/json")
    Call<MessageResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("password/reset")
    @Headers("Accept: application/json")
    Call<MessageResponse> resetPassword(@Body ResetPasswordRequest request);

    @DELETE("user/profile/delete")
    Call<Void> deleteAccount();

    // ---------------- التحقق من البريد ----------------
    @POST("email/resend-verification")
    @Headers("Accept: application/json")
    Call<MessageResponse> sendEmailVerification();

    @GET("email/verify/{id}/{hash}")
    @Headers("Accept: application/json")
    Call<MessageResponse> verifyEmail(
            @Path("id") long userId,
            @Path("hash") String hash,
            @Query("expires") long expires,
            @Query("signature") String signature
    );

    @GET("user/email/verify-status")
    Call<VerifyStatusResponse> getEmailVerifyStatus();

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

    @GET("codes/{id}/translations/{lang}")
    Call<ObdCodeTranslation> getCodeTranslation(
            @Path("id")   long   codeId,
            @Path("lang") String languageCode
    );

    @GET("codes/trending")
    Call<List<ObdCode>> getTrendingCodes();

    @GET("codes/{id}/compare/{other_id}")
    Call<CompareResult> compareCodes(
            @Path("id")       long firstId,
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
    @GET("plans")
    Call<List<Plan>> getAllPlans();

    @GET("plans/{id}")
    Call<Plan> getPlanById(@Path("id") int id);

    @POST("subscribe")
    Call<Subscription> subscribeToPlan(@Body SubscriptionRequest request);

    @POST("activate-code")
    Call<Subscription> activatePlanByCode(@Body ActivationRequest request);

    @GET("subscription/status")
    Call<Subscription> getCurrentSubscriptionStatus();

    @POST("subscription/renew")
    Call<Subscription> renewSubscription(@Body SubscriptionRequest request);

    @POST("subscription/cancel")
    Call<Subscription> cancelSubscription(@Body SubscriptionRequest request);

    // ---------------- ميزة السيارات ----------------
    @GET("user/cars")
    Call<UserCarsResponse> getUserCars();

    @POST("user/cars")
    Call<AddCarResponse> addUserCar(@Body AddCarRequest request);

    @PUT("user/cars/{car_id}")
    Call<UpdateCarsResponse> updateUserCar(
            @Path("car_id") int carId,
            @Body UpdateCarRequest request
    );

    @DELETE("user/cars/{car_id}")
    Call<DeleteCarResponse> deleteUserCar(@Path("car_id") int carId);

    @GET("brands")
    Call<BrandsResponse> getBrands();

    @GET("brands/{brand_id}/models")
    Call<BrandModelsResponse> getBrandModels(@Path("brand_id") int brandId);

    @GET("models/{model_id}/years")
    Call<List<String>> getModelYears(@Path("model_id") int modelId);
}
