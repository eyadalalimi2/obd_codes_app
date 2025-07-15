package com.proapp.obdcodes.network;

import com.proapp.obdcodes.network.model.ActivationRequest;
import com.proapp.obdcodes.network.model.AddCarRequest;
import com.proapp.obdcodes.network.model.AddCarResponse;
import com.proapp.obdcodes.network.model.BrandModelsResponse;
import com.proapp.obdcodes.network.model.BrandsResponse;
import com.proapp.obdcodes.network.model.ChatRequest;
import com.proapp.obdcodes.network.model.ChatResponse;
import com.proapp.obdcodes.network.model.CompareResult;
import com.proapp.obdcodes.network.model.DeleteCarResponse;
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
import com.proapp.obdcodes.network.model.UpdateCarRequest;
import com.proapp.obdcodes.network.model.UpdateCarsResponse;
import com.proapp.obdcodes.network.model.UserCarsResponse;
import com.proapp.obdcodes.network.model.UserProfileResponse;
import com.proapp.obdcodes.network.model.ForgotPasswordRequest;
import com.proapp.obdcodes.network.model.ResetPasswordRequest;
import com.proapp.obdcodes.network.model.MessageResponse;
import com.proapp.obdcodes.network.model.VerifyStatusResponse;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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
    /**
     * تحديث بيانات البروفايل (مع رفع صورة اختيارية)
     * Content-Type: multipart/form-data
     */
    @Multipart
    @POST("user/profile/update")
    @Headers("Accept: application/json")
    Call<UserProfileResponse> updateUserProfile(
            @Part("username")    RequestBody username,
            @Part("email")       RequestBody email,
            @Part("phone")       RequestBody phone,
            @Part("job_title")   RequestBody jobTitle,
            @Part MultipartBody.Part profileImage
    );

    // --------------- نسيت كلمة المرور (الميزة الثانية) ---------------
    @POST("password/forgot")
    @Headers("Accept: application/json")
    Call<MessageResponse> forgotPassword(
            @Body ForgotPasswordRequest request
    );

    // --------------- إعادة تعيين كلمة المرور (الميزة الثالثة) ---------------
    @POST("password/reset")
    @Headers("Accept: application/json")
    Call<MessageResponse> resetPassword(
            @Body ResetPasswordRequest request
    );
    @DELETE("user/profile/delete")
    Call<Void> deleteAccount();

    @POST("email/verification-notification")
    Call<Void> sendVerificationNotification();



    // --------------- ميزة التحقق من البريد الإلكتروني ---------------
    @POST("email/verification-notification")
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
    @Headers("Accept: application/json")
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


    // ---------------- ميزة السيارات ----------------

    /** 1. استدعاء جميع سيارات المستخدم */
    @GET("user/cars")
    Call<UserCarsResponse> getUserCars();

    /** 2. إضافة سيارة جديدة */
    @POST("user/cars")
    Call<AddCarResponse> addUserCar(@Body AddCarRequest request);

    /** 3. تحديث بيانات سيارة موجودة */
    @PUT("user/cars/{car_id}")
    Call<UpdateCarsResponse> updateUserCar(
            @Path("car_id") int carId,
            @Body UpdateCarRequest request
    );

    /** 4. حذف سيارة */
    @DELETE("user/cars/{car_id}")
    Call<DeleteCarResponse> deleteUserCar(@Path("car_id") int carId);

    /** 5. جلب قائمة الشركات المصنعة */
    @GET("brands")
    Call<BrandsResponse> getBrands();

    /** 6. جلب طرازات شركة معينة */
    @GET("brands/{brand_id}/models")
    Call<BrandModelsResponse> getBrandModels(@Path("brand_id") int brandId);

    /** 7. جلب سنوات الإنتاج لطراز معين */
    @GET("models/{model_id}/years")
    Call<List<String>> getModelYears(@Path("model_id") int modelId);
}