package mycargo.wladek.com.mycargo.util.api.retrofit;

import mycargo.wladek.com.mycargo.pojo.Token;
import mycargo.wladek.com.mycargo.pojo.UserRegPojo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by wladek on 5/25/17.
 */

public interface APITokenService {
    @POST("token")
    @FormUrlEncoded
    Call<Token> getToken(@Field("UserName") String userName,
                         @Field("Password") String password);

    @Headers("Accept: text/plain")
    @POST("api/v1/Users/register")
    Call<String> registerUser(@Body UserRegPojo userRegPojo);
}

