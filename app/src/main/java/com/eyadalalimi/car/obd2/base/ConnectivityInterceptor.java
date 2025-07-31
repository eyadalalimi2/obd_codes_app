package com.eyadalalimi.car.obd2.base;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor يتحقق من وجود اتصال إنترنت قبل تنفيذ الطلب،
 * ويرمي استثناءً مخصصًا يمكن التقاطه في طبقة أعلى لعرض رسالة للمستخدم.
 */
public class ConnectivityInterceptor implements Interceptor {

    private final Context context;

    public ConnectivityInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtil.isConnected(context)) {
            // رمي استثناء مخصص يوضح عدم وجود اتصال
            throw new NoConnectivityException();
        }
        Request request = chain.request();
        return chain.proceed(request);
    }

    /**
     * استثناء مخصص يُستخدم عندما لا يكون هناك اتصال بالإنترنت.
     */
    public static class NoConnectivityException extends IOException {
        public NoConnectivityException() {
            super("No Internet Connection");
        }
    }
}
