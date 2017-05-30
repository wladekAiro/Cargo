package mycargo.wladek.com.mycargo.util.api.retrofit;

import mycargo.wladek.com.mycargo.pojo.Password;
import mycargo.wladek.com.mycargo.pojo.Profile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by wladek on 5/25/17.
 */

public interface APIProfileService {

    @GET("api/v1/Profile")
    Call<Profile> getProfile();

    @GET("api/v1/Profile")
    Call<Profile> getShipperProfile(@Path("shipperId") String shipperId);

    @POST("api/v1/Profile/changePassword")
    Call<Void> changePassword(@Body Password password);
}
