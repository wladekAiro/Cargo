package mycargo.wladek.com.mycargo.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;

import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mycargo.wladek.com.mycargo.R;
import mycargo.wladek.com.mycargo.databinding.ActivityLoginBinding;
import mycargo.wladek.com.mycargo.helpers.TokenDbHelper;
import mycargo.wladek.com.mycargo.helpers.UserDbHelper;
import mycargo.wladek.com.mycargo.pojo.BasicUser;
import mycargo.wladek.com.mycargo.pojo.Token;
import mycargo.wladek.com.mycargo.util.api.ApiUtils;
import mycargo.wladek.com.mycargo.util.api.retrofit.APITokenService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private SweetAlertDialog sweetAlertDialog;
    private String accessToken;
    private String expiresIn;
    private APITokenService mApiService;
    private TokenDbHelper tokenDbHelper;
    private UserDbHelper userDbHelper;ActivityLoginBinding viewBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinder = DataBindingUtil.setContentView(this , R.layout.activity_login);
        mApiService = ApiUtils.getAPITokenService();

        tokenDbHelper = new TokenDbHelper(this);
        userDbHelper = new UserDbHelper(this);
        viewBinder.btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        viewBinder.linkSignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        testJWT();
        // Reset errors.
        viewBinder.inputEmail.setError(null);
        viewBinder.inputPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = viewBinder.inputEmail.getText().toString();
        String password = viewBinder.inputPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && isPasswordValid(password)) {
            viewBinder.inputPassword.setError(getString(R.string.error_invalid_password));
            focusView = viewBinder.inputPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            viewBinder.inputEmail.setError(getString(R.string.error_field_required));
            focusView = viewBinder.inputEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            viewBinder.inputEmail.setError(getString(R.string.error_invalid_email));
            focusView = viewBinder.inputEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            logIn(email , password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() < 4;
    }

    private void testJWT(){

        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6ImIzZjMzMzU4LThkMDktNGE5Zi1hMjY0LTdhMzQwMjI3YjY4OSIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25hbWUiOiJ3bGFkZWsuYWlyb0BnbWFpbC5jb20iLCJBc3BOZXQuSWRlbnRpdHkuU2VjdXJpdHlTdGFtcCI6IjViMDdkODBmLThiZjEtNDI5My1iMDFjLTM1Njg5MmNiZWVjZCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IlRyYW5zcG9ydGVycyIsImh0dHA6Ly9zY2hlbWFzLm15Y2FyZ28uaW8vaWRlbnRpdHkvY2xhaW1zL3Blcm1pc3Npb24iOlsidHJ1Y2tzLmNyZWF0ZSIsInRydWNrcy5lZGl0IiwiZHJpdmVycy5jcmVhdGUiLCJkcml2ZXJzLmVkaXQiLCJ1c2Vycy5lZGl0IiwidHJhbnNwb3J0ZXJzLmxpc3QiLCJ0cmFuc3BvcnRlcnMuZWRpdCJdLCJodHRwOi8vc2NoZW1hcy5teWNhcmdvLmlvL2lkZW50aXR5L2NsYWltcy9maXJzdG5hbWUiOiJXbGFkZWsiLCJodHRwOi8vc2NoZW1hcy5teWNhcmdvLmlvL2lkZW50aXR5L2NsYWltcy9vdGhlcm5hbWVzIjoiT2NoaWVuZyIsImh0dHA6Ly9zY2hlbWFzLm15Y2FyZ28uaW8vaWRlbnRpdHkvY2xhaW1zL2xhc3RuYW1lIjoiQWlybyIsImh0dHA6Ly9zY2hlbWFzLm15Y2FyZ28uaW8vaWRlbnRpdHkvY2xhaW1zL3RyYW5zcG9ydGVyX2lkIjoiMyIsInN1YiI6IndsYWRlay5haXJvQGdtYWlsLmNvbSIsImp0aSI6ImQ4MWUwZDM4LTIzNDctNDU3Zi04ODg1LWE2NjhiYzgzZDI3NyIsImlhdCI6MTQ5NTYxMTc3OCwibmJmIjoxNDk1NjExNzc4LCJleHAiOjE0OTU2MTI5NzgsImlzcyI6IlNoaXBNeUNhcmdvVG9rZW5TZXJ2ZXIiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjUyNDU2LyJ9.JPSkbm75cNbv9RavSWAX3wBep6uyat93xVKplVojofc";
        JWT jwt = new JWT(jwtToken);

        System.out.println("++++ TOKEN ISSUER : "+jwt.getIssuer());

        Map<String , String> header = jwt.getHeader();

        List<String> payLoadAudience = jwt.getAudience();

        for (String key : header.keySet()){
            System.out.println(" HEADER KEY : "+key + " ; VALUE : " + header.get(key));
        }

        int count = 0;

        for (String aud : payLoadAudience){
            count ++;
            System.out.println(" AUDIENCE "+count +" : "+ aud);
        }

        System.out.println("++++ TOKEN ID : "+jwt.getId());
        System.out.println("++++ GET CLAIM : "+jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name").asString());
    }

    private void logIn(String username, final String password) {


        sweetAlertDialog = new SweetAlertDialog(LoginActivity.this , SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please wait ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        mApiService.getToken(username , password).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                sweetAlertDialog.dismiss();

                String resp = response.message();

                System.out.println("++++++ RESP MESSAGE +++++ "+ response.message());

                if (!resp.toLowerCase().equals("ok")){
                    Toast.makeText(LoginActivity.this , "Invalid credentials" , Toast.LENGTH_LONG).show();
                }

                if(response.isSuccessful()) {

                    Log.i("LOGIN ACTIVITY", "post submitted to API.");

                    if (tokenDbHelper.saveToken(response.body())){
                        if (userDbHelper.saveBasicUser(new BasicUser(new JWT(response.body().getAccessToken()) , password))){
                            System.out.println("++++ LAUNCHING HOME ACTIVITY ++++++ ");
                            Intent intent = new Intent(LoginActivity.this , HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(LoginActivity.this , "Error occurred. Try again." , Toast.LENGTH_LONG).show();
                sweetAlertDialog.dismiss();
                t.printStackTrace();

                System.out.println(" +++ Unable to submit post to API. ++++ " + call.toString());

                Log.e("LOGINACTIVITY", "Unable to submit post to API.");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}

