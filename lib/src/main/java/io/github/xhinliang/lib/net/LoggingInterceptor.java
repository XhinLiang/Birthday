
package io.github.xhinliang.lib.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor {

    private static final String TAG = "OkHttp";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Log.d(TAG, String.format("%s\n%s", request, request.headers()));

        Response response = chain.proceed(request);

        Log.d(TAG, String.format("%s\n%s", response, response.headers()));

        return response;
    }
}
