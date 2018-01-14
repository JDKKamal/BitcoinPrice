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
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient implements RestConstant {

    private static RestClient instance = null;
    private static Context context;

    private RestService restService;
    private final int DEFAULT_TIMEOUT = 10;

    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);

    private File cacheFile = new File(BaseApplication.getBaseApplication().getCacheDir(), "BitcoinPrice");
    private Cache cache = new Cache(cacheFile, 1024 * 1024 * 1024); //1 GB

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
                    .client(httpClient)
                    .build();
            restService = retrofit.create(RestService.class);
        }
    }

    public RestService getService() {
        return restService;
    }

    OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) //SET CONNECTION TIMEOUT
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) //SET READ TIMEOUT
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) //SET WRITE TIMEOUT
            .addNetworkInterceptor(new CacheControlInterceptor())
            .addInterceptor(logging)
            .cache(cache) //ADD CACHE
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

    //TODO CACHE CONTROL INTERCEPTOR MANAGE
    //https://www.codeday.top/2016/12/19/6602.html
    public class CacheControlInterceptor implements Interceptor {
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

            //TODO OFFLINE CACHE MANAGE
            if (isInternet(context)) {
                int maxAge = 60 * 60 * 24 * 28; //CACHE EXPIRATION TIME，UNIT FOR SECONDS (60 IS 1 SECOND)
                return response.newBuilder().removeHeader("Pragma") //CLEAR HEADER INFORMATION，BECAUSE SERVER IF NOT SUPPORTED， WILL RETURN SOME INTERFERENCE INFORMATION， DOES NOT CLEAR THE FOLLOWING CAN NOT BE EFFECTIVE
                        .header("Cache-Control", "public ,max-age=" + maxAge).build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; //WHEN THERE IS NO NETWORK，SET TIMEOUT TO 4 WEEK
                return response.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
}