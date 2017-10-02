package com.myntra.flickrgame.base;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by SurvivoR on 7/3/2017.
 * {@link ApiService}
 */

public abstract class ApiService<U, T extends ApiRequest, L extends ApiResponse> {

    private static final int TIME_OUT = 30;

    /**
     * Use to build Gson object for serialization and deserialization of pojo
     */
    private static Gson sGson;

    private Context mContext;

    public ApiService(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public final void execute(final T request, final ApiCallback<L> callback) {
        if (callback == null) {
//            throw new ApiException(ApiException.Kind.NULL, "Callback may not be null");
        } else if (getAPI() == null) {
            callback.onComplete();
            callback.onFailure(new ApiException(ApiException.Kind.NULL, "API Interface may not be null"));
        } else if (request == null) {
            callback.onComplete();
            callback.onFailure(new ApiException(ApiException.Kind.NULL, "Request may not be null"));
        } else if (!request.isValid()) {
            callback.onComplete();
            callback.onFailure(new ApiException(ApiException.Kind.INVALID_REQUEST, "Invalid request"));
        } else {
            String baseUrl = request.getBaseUrl();
            if (TextUtils.isEmpty(baseUrl)) {
                callback.onComplete();
                callback.onFailure(new ApiException(ApiException.Kind.NULL, "Base Url may not be null"));
                return;
            }
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(getConnectionTimeoutInSeconds(), SECONDS)
                    .readTimeout(getReadTimeoutInSeconds(), SECONDS);
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Call<L> call = ApiService.this.onExecute(retrofit.create(getAPI()), request);
                    call.enqueue(callback);
                }
            }).start();
        }
    }

    private Gson getGson() {
        if (sGson != null) {
            return sGson;
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        return sGson = gsonBuilder.create();
    }

    public int getConnectionTimeoutInSeconds() {
        return TIME_OUT;
    }

    public int getReadTimeoutInSeconds() {
        return TIME_OUT;
    }

    protected abstract Class<U> getAPI();

    /**
     * @param api     to pass data in request
     * @param request to send on server
     */
    protected abstract Call<L> onExecute(U api, T request);
}
