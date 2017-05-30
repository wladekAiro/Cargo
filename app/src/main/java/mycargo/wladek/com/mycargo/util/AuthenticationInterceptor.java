package mycargo.wladek.com.mycargo.util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wladek on 5/25/17.
 */

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;
    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .addHeader("Authorization", "Bearer "+authToken);
        Request request = builder.build();
        return chain.proceed(request);
    }
}
