package mycargo.wladek.com.mycargo.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mycargo.wladek.com.mycargo.R;
import mycargo.wladek.com.mycargo.helpers.TokenDbHelper;
import mycargo.wladek.com.mycargo.pojo.Password;
import mycargo.wladek.com.mycargo.util.api.retrofit.APIProfileService;
import mycargo.wladek.com.mycargo.util.api.volley.TokenRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wladek on 5/25/17.
 */

public class PasswordFragment extends Fragment {
    View myFragmentView;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private SweetAlertDialog sweetAlertDialog;
    private TokenDbHelper tokenDbHelper;

    private APIProfileService mApiService;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PasswordFragment newInstance(int sectionNumber) {
        PasswordFragment fragment = new PasswordFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenDbHelper = new TokenDbHelper(getActivity());
//        mApiService = ApiUtils.getApIProfileService(tokenDbHelper.getToken().getAccessToken());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.password_fragment_layout, container, false);

        oldPassword = (EditText) myFragmentView.findViewById(R.id.old_password);
        newPassword = (EditText) myFragmentView.findViewById(R.id.new_password);
        confirmNewPassword = (EditText) myFragmentView.findViewById(R.id.confirm_new_password);

        Button btnResetPass = (Button) myFragmentView.findViewById(R.id.btn_reset_password);
        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        return myFragmentView;
    }

    public void resetPassword() {
        String oldPass = oldPassword.getText().toString();
        String newPass = newPassword.getText().toString();
        String confirmNewPass = confirmNewPassword.getText().toString();

        if (!validateInputs(oldPass, newPass, confirmNewPass)) {
            sendResetRequest(new Password(oldPass, newPass, confirmNewPass));
        }
    }

    private void sendResetRequestVolley(Password password) {

        Map<String, String> loginParams = new HashMap<String, String>();

        loginParams.put("oldPassword", password.getOldPassword());
        loginParams.put("newPassword", password.getNewPassword());
        loginParams.put("confirmPassword", password.getConfirmPassword());

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please wait ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        com.android.volley.Response.Listener<JSONObject> loginResponseListener = new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                System.out.println(" SERVER RESPONSE " + response.toString());
                    Toast.makeText(getActivity(), "RESET PASS : " + response.toString(), Toast.LENGTH_LONG).show();
                    sweetAlertDialog.dismiss();
            }
        };

        TokenRequest loginRequest = new TokenRequest(new JSONObject(loginParams) , loginResponseListener);
        loginRequest.setYourToken(tokenDbHelper.getToken().getAccessToken());
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(loginRequest);

    }


    private void sendResetRequest(Password password) {
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please wait ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        mApiService.changePassword(password).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                sweetAlertDialog.dismiss();
                String resp = response.message();

                System.out.println("++++++ RESP MESSAGE +++++ " + resp);

                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Password changed.", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), resp, Toast.LENGTH_LONG).show();
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

    private boolean validateInputs(String oldPass, String newPass, String confirmNewPass) {
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(oldPass)) {
            oldPassword.setError(getString(R.string.error_field_required));
            focusView = oldPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(newPass)) {
            newPassword.setError(getString(R.string.error_field_required));
            focusView = newPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirmNewPass)) {
            confirmNewPassword.setError(getString(R.string.error_field_required));
            focusView = confirmNewPassword;
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
}
