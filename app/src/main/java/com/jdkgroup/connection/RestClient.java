package com.jdkgroup.connection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.GsonBuilder;
import com.jdkgroup.baseclass.BaseApplication;
import com.jdkgroup.bitcoinprice.R;
import com.jdkgroup.constant.RestConstant;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.utils.Logging;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient implements RestConstant {

    private static RestClient instance = null;
    private Context context;

    private RestService restService;
    private final int DEFAULT_TIMEOUT = 5;

    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
    File cacheFile = new File(BaseApplication.getBaseApplication().getCacheDir(), "cache");
    Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb

    public static RestClient restInstance(Context context, int request) {
        if (instance == null) {
            instance = new RestClient(context, request);
        }
        return instance;
    }


    public RestClient(Context context, int request) {
        this.context = context;
        Retrofit retrofit;
        if (request == REQUEST_NO_AUTH) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    //.addConverterFactory(new ToStringConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            restService = retrofit.create(RestService.class);
        }
    }

    public RestService getService() {
        return restService;
    }

    TokenManager tokenManager = new TokenManagerImpl(context);

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            //.addNetworkInterceptor(new StethoInterceptor())
            .cache(cache)
            .addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                requestBuilder.header("Accept", "application/json");

                //TODO TOKEN PASS
                if (tokenManager.hasToken()) {
                    Logging.i(tokenManager.getToken());
                    requestBuilder.header("Authorization", tokenManager.getToken());
                }

                requestBuilder.method(original.method(), original.body());
                Response response = chain.proceed(requestBuilder.build());

                Logging.i("----------------- API CALL -----------------");
                Logging.i("Token " + tokenManager.hasToken() + " - " + tokenManager.getToken());
                Logging.i("Response " + response + " - " + response.body().string());
                Logging.i("--------------------------------------------");

                if (isInternet(context)) {
                    int maxAge = 60; // read from cache for 1 minute
                    requestBuilder.addHeader("Cache-Control", "public, max-age=" + maxAge);
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    requestBuilder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
                }
                return chain.proceed(requestBuilder.build());
            })
            .build();

    protected boolean isInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnectedOrConnecting())) {
            AppUtils.showToast(context, String.valueOf(R.string.no_internet_message));
            return false;
        }
        return true;
    }
}