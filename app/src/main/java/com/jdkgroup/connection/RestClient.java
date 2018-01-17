package com.jdkgroup.connection;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.jdkgroup.baseclass.BaseApplication;
import com.jdkgroup.constant.RestConstant;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.utils.Logging;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient implements RestConstant {

    private static RestClient restClient;
    private static Context context;

    private RestService restService;
    private final int DEFAULT_TIMEOUT = 10;
    private int cacheSizeInMbs = 50;
    private int maxStale = 7; //1 WEEK

    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private File cacheFile = new File(BaseApplication.getBaseApplication().getCacheDir(), "bitcoinprice");
    private Cache cache = new Cache(cacheFile, 1024 * 1024 * cacheSizeInMbs);

    public static RestClient restInstance(Context context) {
        return restClient = (restClient == null ? new RestClient(context) : restClient);
    }

    private RestClient(Context context) {
        this.context = context;
        restService = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.addConverterFactory(new ToStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build().create(RestService.class);
    }

    public RestService getService() {
        return restService;
    }

    OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) //SET CONNECTION TIMEOUT
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) //SET READ TIMEOUT
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) //SET WRITE TIMEOUT
            .addInterceptor(new HeaderInterceptor())
            .addNetworkInterceptor(new CacheControlInterceptor())
            .addInterceptor(logging)
            .cache(cache) //ADD CACHE
            .build();

    //TODO CACHE CONTROL INTERCEPTOR MANAGE
    //https://www.codeday.top/2016/12/19/6602.html
    public class HeaderInterceptor implements Interceptor {
        TokenManager tokenManager = new TokenManagerImpl(context);

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.header("Accept", "application/json");

            //TODO TOKEN PASS
            if (tokenManager.hasToken()) {
                Logging.i(tokenManager.getToken());
                builder.header("Authorization", tokenManager.getToken());
            }
            Response response = chain.proceed(builder.build());

            Logging.i("----------------- API CALL -----------------");
            Logging.i("Token " + tokenManager.hasToken() + " - " + tokenManager.getToken());
            Logging.i("Response " + response);
            Logging.i("--------------------------------------------");

            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), response.body().string())).build();
        }
    }

    public class CacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            //TODO OFFLINE CACHE MANAGE
            if (AppUtils.isInternet(context)) {
                return response.newBuilder().removeHeader("Pragma") //CLEAR HEADER INFORMATION，BECAUSE SERVER IF NOT SUPPORTED， WILL RETURN SOME INTERFERENCE INFORMATION， DOES NOT CLEAR THE FOLLOWING CAN NOT BE EFFECTIVE
                        .header("Cache-Control", "public, max-age=" + 60 * 60 * 24 * maxStale).build();
            } else {
                return response.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * maxStale).removeHeader("Pragma").build();
            }
        }
    }
}