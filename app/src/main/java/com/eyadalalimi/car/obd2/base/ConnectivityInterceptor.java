package com.eyadalalimi.car.obd2.base;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import android.content.Context;

public class ConnectivityInterceptor implements Interceptor {
    private final Context context;
    public ConnectivityInterceptor(Context context) { this.context = context; }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtil.isConnected(context)) {
            throw new IOException("No Internet Connection");
        }
        Request request = chain.request();
        return chain.proceed(request);
    }
}
