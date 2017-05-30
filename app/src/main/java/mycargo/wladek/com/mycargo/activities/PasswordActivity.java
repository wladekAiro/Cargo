package mycargo.wladek.com.mycargo.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mycargo.wladek.com.mycargo.R;
import mycargo.wladek.com.mycargo.databinding.ActivityPasswordBinding;
import mycargo.wladek.com.mycargo.helpers.TokenDbHelper;
import mycargo.wladek.com.mycargo.helpers.UserDbHelper;
import mycargo.wladek.com.mycargo.pojo.BasicUser;
import mycargo.wladek.com.mycargo.pojo.Password;
import mycargo.wladek.com.mycargo.service.ServiceGenerator;
import mycargo.wladek.com.mycargo.util.api.retrofit.APIProfileService;
import mycargo.wladek.com.mycargo.util.api.volley.TokenRequest;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordActivity extends AppCompatActivity {

    ActivityPasswordBinding viewBinding;
    private SweetAlertDialog sweetAlertDialog;
    private String token;

    private UserDbHelper userDbHelper;
    private TokenDbHelper tokenDbHelper;
    private BasicUser basicUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_password);
        userDbHelper = new UserDbHelper(this);
        tokenDbHelper = new TokenDbHelper(this);

        basicUser = userDbHelper.getBasicUser();

        viewBinding.btnSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    public void resetPassword() {
        String oldPass = viewBinding.inputOldPassword.getText().toString();
        String newPass = viewBinding.inputNewPassword.getText().toString();
        String confirmNewPass = viewBinding.inputConfirmPassword.getText().toString();

        if (!validateInputs(oldPass, newPass, confirmNewPass)) {
            sendResetRequest(new Password(oldPass, newPass, confirmNewPass));
        }
    }

    private void sendResetRequestVolley(Password password) {

        Map<String, String> loginParams = new HashMap<String, String>();

        loginParams.put("oldPassword", password.getOldPassword());
        loginParams.put("newPassword", password.getNewPassword());
        loginParams.put("confirmPassword", password.getConfirmPassword());

        sweetAlertDialog = new SweetAlertDialog(PasswordActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please wait ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        com.android.volley.Response.Listener<JSONObject> loginResponseListener = new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(" SERVER RESPONSE " + response.toString());
                Toast.makeText(PasswordActivity.this, "RESET PASS : " + response.toString(), Toast.LENGTH_LONG).show();
                sweetAlertDialog.dismiss();
            }
        };

        TokenRequest loginRequest = new TokenRequest(new JSONObject(loginParams), loginResponseListener);
        loginRequest.setYourToken(token);
        RequestQueue queue = Volley.newRequestQueue(PasswordActivity.this);
        queue.add(loginRequest);

    }

    private void sendResetRequest(final Password password) {
        sweetAlertDialog = new SweetAlertDialog(PasswordActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please wait ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APIProfileService apiProfileService =
                ServiceGenerator.createService(APIProfileService.class, tokenDbHelper.getToken(),
                        this, userDbHelper.getBasicUser());
        apiProfileService.changePassword(password).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                sweetAlertDialog.dismiss();

                Headers headers = response.headers();
                headers.toString();

                if (response.isSuccessful()) {
                    Toast.makeText(PasswordActivity.this, "Password changed.", Toast.LENGTH_LONG).show();
                    updateBasicUser(password.getNewPassword());
                } else {
                    Toast.makeText(PasswordActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sweetAlertDialog.dismiss();
                t.printStackTrace();

                System.out.println(" +++ Unable to submit post to API. ++++ ");

                Log.e("RESETPASSWORD", "Unable to submit post to API.");
            }
        });
    }

    public boolean upDateCredentials(Password password) {
        basicUser.setKey(password.getNewPassword());
        return userDbHelper.saveBasicUser(basicUser);
    }

    private boolean validateInputs(String oldPass, String newPass, String confirmNewPass) {
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(oldPass)) {
            viewBinding.inputOldPassword.setError(getString(R.string.error_field_required));
            focusView = viewBinding.inputOldPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(newPass)) {
            viewBinding.inputNewPassword.setError(getString(R.string.error_field_required));
            focusView = viewBinding.inputNewPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirmNewPass)) {
            viewBinding.inputConfirmPassword.setError(getString(R.string.error_field_required));
            focusView = viewBinding.inputConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            logIn(email , password);
        }

        return cancel;
    }

    private void updateBasicUser(String key) {
        basicUser.setKey(key);
        userDbHelper.saveBasicUser(basicUser);
    }

    @Override
    protected void onResume() {
        viewBinding.notifyChange();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        viewBinding.notifyChange();
        super.onRestart();
    }
}
