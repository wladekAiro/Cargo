package mycargo.wladek.com.mycargo.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mycargo.wladek.com.mycargo.R;
import mycargo.wladek.com.mycargo.databinding.ActivityRegisterBinding;
import mycargo.wladek.com.mycargo.enums.RegistrationType;
import mycargo.wladek.com.mycargo.helpers.TokenDbHelper;
import mycargo.wladek.com.mycargo.helpers.UserDbHelper;
import mycargo.wladek.com.mycargo.pojo.BasicUser;
import mycargo.wladek.com.mycargo.pojo.UserRegPojo;
import mycargo.wladek.com.mycargo.service.ServiceGenerator;
import mycargo.wladek.com.mycargo.util.api.retrofit.APITokenService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding viewBinder;
    private UserDbHelper userDbHelper;
    private TokenDbHelper tokenDbHelper;
    private String token;
    private BasicUser basicUser;
    private SweetAlertDialog sweetAlertDialog;
    private RegistrationType registrationType;
    private MaterialDialog.Builder builder;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinder = DataBindingUtil.setContentView(this ,R.layout.activity_register);

        userDbHelper = new UserDbHelper(this);
        tokenDbHelper = new TokenDbHelper(this);

        basicUser = userDbHelper.getBasicUser();
        token = tokenDbHelper.getToken().getAccessToken();

        ArrayAdapter<RegistrationType> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                RegistrationType.values());

        viewBinder.spinnerRegistrationType.setAdapter(myAdapter);

        viewBinder.spinnerRegistrationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                registrationType = (RegistrationType) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewBinder.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        viewBinder.linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void registerUser() {
        String email = viewBinder.inputEmail.getText().toString().trim();
        String phoneNumber = viewBinder.inputPhoneNumber.getText().toString().trim();
        String idNumber = viewBinder.inputPhoneNumber.getText().toString().trim();
        String firstName = viewBinder.inputFirstName.getText().toString().trim();
        String otherName = viewBinder.inputOtherName.getText().toString().trim();
        String lastName = viewBinder.inputLastName.getText().toString().trim();
        String password = viewBinder.inputPassword.getText().toString().trim();
        String confirmPassword = viewBinder.inputConfirmPassword.getText().toString().trim();

        if(!validateInputs(email,phoneNumber,idNumber,firstName,otherName,lastName,password,confirmPassword)){
            UserRegPojo userRegPojo = new UserRegPojo();
            userRegPojo.setEmail(email);
            userRegPojo.setPhoneNumber(phoneNumber);
            userRegPojo.setIdNumber(idNumber);
            userRegPojo.setFirstName(firstName);
            userRegPojo.setOtherNames(otherName);
            userRegPojo.setLastName(lastName);
            userRegPojo.setPassword(password);
            userRegPojo.setConfirmPassword(confirmPassword);
            userRegPojo.setRegistrationType(registrationType.toString());

            submitRequest(userRegPojo);
        }
    }

    private void submitRequest(UserRegPojo userRegPojo) {

        sweetAlertDialog = new SweetAlertDialog(RegisterActivity.this , SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please wait ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        APITokenService apiTokenService =
                ServiceGenerator.createService(APITokenService.class);
        Call<String> call = apiTokenService.registerUser(userRegPojo);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                sweetAlertDialog.dismiss();

                if (response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this , "Registered" , Toast.LENGTH_LONG).show();
                    showSuccessDialog();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                sweetAlertDialog.dismiss();
            }
        });
    }

    private boolean validateInputs(String email, String phoneNumber, String idNumber, String firstName,
                                String otherName, String lastName, String password, String confirmPassword) {

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)){
            viewBinder.inputEmail.setError(getString(R.string.error_field_required));
            focusView = viewBinder.inputEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneNumber)){
            viewBinder.inputPhoneNumber.setError(getString(R.string.error_field_required));
            focusView = viewBinder.inputPhoneNumber;
            cancel = true;
        }

        if (TextUtils.isEmpty(idNumber)){
            viewBinder.inputIdNumber.setError(getString(R.string.error_field_required));
            focusView = viewBinder.inputIdNumber;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)){
            viewBinder.inputFirstName.setError(getString(R.string.error_field_required));
            focusView = viewBinder.inputFirstName;
            cancel = true;
        }

        if (TextUtils.isEmpty(otherName)){
            viewBinder.inputOtherName.setError(getString(R.string.error_field_required));
            focusView = viewBinder.inputOtherName;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastName)){
            viewBinder.inputLastName.setError(getString(R.string.error_field_required));
            focusView = viewBinder.inputLastName;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)){
            viewBinder.inputPassword.setError(getString(R.string.error_field_required));
            focusView = viewBinder.inputPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirmPassword)){
            viewBinder.inputPhoneNumber.setError(getString(R.string.error_field_required));
            focusView = viewBinder.inputConfirmPassword;
            cancel = true;
        }

        if (!password.equals(confirmPassword)){
            viewBinder.inputPassword.setError("passwords do not march");
            viewBinder.inputConfirmPassword.setError("passwords do not match");
            viewBinder.inputPassword.requestFocus();
            focusView = viewBinder.inputConfirmPassword;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }

        return cancel;
    }

    private void showSuccessDialog(){
        builder = new MaterialDialog.Builder(this);
        builder.title("Registration Success");
        builder.cancelable(false);
        builder.content("Your registration is almost done. " +
                "Please follow the link sent to your registration email to complete your registration.");
        builder.positiveText("Ok");

        dialog = builder.build();
        dialog.show();

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                onBackPressed();
            }
        });
    }
}
