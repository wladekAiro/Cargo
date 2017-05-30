package mycargo.wladek.com.mycargo.service;

import mycargo.wladek.com.mycargo.pojo.BasicUser;
import mycargo.wladek.com.mycargo.pojo.Token;
import mycargo.wladek.com.mycargo.util.api.ApiUtils;
import mycargo.wladek.com.mycargo.util.api.retrofit.APITokenService;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by wladek on 5/27/17.
 */

public class TokenRequester {
    private BasicUser basicUser;

    String token;

    public TokenRequester(BasicUser basicUser){
        this.basicUser = basicUser;
    }

    public String getAuthenticationToken() {
        try {
            APITokenService lyftService = ApiUtils.getAPITokenService();
            Call<Token> authRequestCall = lyftService.getToken(basicUser.getEmail() , basicUser.getKey());
            authRequestCall.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, retrofit2.Response<Token> response) {
                    try {
                        if (response.isSuccessful()) {
                            token = response.body().getAccessToken();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return token;
    }
}
