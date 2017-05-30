package mycargo.wladek.com.mycargo.util.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mycargo.wladek.com.mycargo.pojo.BasicUser;
import mycargo.wladek.com.mycargo.util.AuthenticationInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wladek on 5/25/17.
 */

public class RetrofitClient {
    private static boolean tokenExpired;
    private static String accessToken = "";
    private static Retrofit retrofit = null;
    private static Retrofit retrofitAuthenticated = null;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static OkHttpClient.Builder getHttpClient() {
        return httpClient;
    }

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    static OkHttpClient client = new OkHttpClient();

    public static Retrofit getClientAuth(String baseUrl, String authToken , BasicUser basicUser) {

        if (retrofitAuthenticated == null) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            client.interceptors().add(interceptor);

            retrofitAuthenticated = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitAuthenticated;
    }
}
