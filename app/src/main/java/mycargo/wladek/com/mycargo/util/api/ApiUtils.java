package mycargo.wladek.com.mycargo.util.api;

import mycargo.wladek.com.mycargo.pojo.BasicUser;
import mycargo.wladek.com.mycargo.util.api.retrofit.APIProfileService;
import mycargo.wladek.com.mycargo.util.api.retrofit.APITokenService;

/**
 * Created by wladek on 5/25/17.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://ship.mycargo.io/";

    public static APITokenService getAPITokenService() {
        return RetrofitClient.getClient(BASE_URL).create(APITokenService.class);
    }

    public static APIProfileService getApIProfileService(String authToken , BasicUser basicUser){
       return RetrofitClient.getClientAuth(BASE_URL , authToken , basicUser).create(APIProfileService.class);
    }
}
