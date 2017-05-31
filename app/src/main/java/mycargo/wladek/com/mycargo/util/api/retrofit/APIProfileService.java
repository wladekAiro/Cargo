package mycargo.wladek.com.mycargo.util.api.retrofit;

import java.util.Map;

import mycargo.wladek.com.mycargo.pojo.Password;
import mycargo.wladek.com.mycargo.pojo.Profile;
import mycargo.wladek.com.mycargo.pojo.RoleProfileResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by wladek on 5/25/17.
 */

public interface APIProfileService {

    @GET("api/v1/Profile")
    Call<Profile> getProfile();

    @POST("api/v1/Profile/changePassword")
    Call<Void> changePassword(@Body Password password);

    @GET("api/v1/Shippers/{shipperId}")
    Call<RoleProfileResult> getShipperProfile(@Path("shipperId") String shipperId);

    @GET("api/v1/Transporters/{transporterId}")
    Call<RoleProfileResult> getTransporterProfile(@Path("transporterId") String transporterId);

    @PUT("api/v1/Shippers/{shipperId}")
    @FormUrlEncoded
    Call<RoleProfileResult> updateShipperProfile2(@Path("shipperId") String shipperId ,
                                                 @Field("Id") String id,@Field("PhoneNumber") String phoneNo,
                                                 @Field("Email") String email , @Field("Address") String address,
                                                 @Field("CompanyName") String companyName , @Field("PinNumber") String pinNumber);

    @PUT("api/v1/Transporters/{transporterId}")
    @FormUrlEncoded
    Call<RoleProfileResult> updateTransporterProfile2(@Path("transporterId") String transporterId ,
                                                     @Field("Id") String id,@Field("PhoneNumber") String phoneNo,
                                                     @Field("Email") String email , @Field("Address") String address,
                                                     @Field("CompanyName") String companyName , @Field("RegistrationCertificateNumber") String registrationCertificateNumber,
                                                     @Field("CompanyPinNumber") String companyPinNumber , @Field("CompanyProfile") String companyProfile,
                                                     @Field("DirectorName") String DirectorName, @Field("DirectorIdNumber") String DirectorIdNumber,
                                                     @Field("DirectorPinNumber") String directorPinNumber);

    @PUT("api/v1/Shippers/{shipperId}")
    @FormUrlEncoded
    Call<RoleProfileResult> updateShipperProfile(@Path("shipperId") String shipperId ,
                                                  @FieldMap Map<String , String> vals);

    @PUT("api/v1/Transporters/{transporterId}")
    @FormUrlEncoded
    Call<RoleProfileResult> updateTransporterProfile(@Path("transporterId") String transporterId ,
                                                     @FieldMap Map<String , String> vals);
}
