package mycargo.wladek.com.mycargo.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mycargo.wladek.com.mycargo.R;
import mycargo.wladek.com.mycargo.databinding.ActivityHome2Binding;
import mycargo.wladek.com.mycargo.helpers.TokenDbHelper;
import mycargo.wladek.com.mycargo.helpers.UserDbHelper;
import mycargo.wladek.com.mycargo.pojo.BasicUser;
import mycargo.wladek.com.mycargo.pojo.Profile;
import mycargo.wladek.com.mycargo.pojo.RoleProfileResult;
import mycargo.wladek.com.mycargo.pojo.ShipperProfile;
import mycargo.wladek.com.mycargo.pojo.Token;
import mycargo.wladek.com.mycargo.pojo.TransporterProfile;
import mycargo.wladek.com.mycargo.service.ServiceGenerator;
import mycargo.wladek.com.mycargo.util.api.retrofit.APIProfileService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ActivityHome2Binding viewBinding;
    private UserDbHelper userDbHelper;
    private TokenDbHelper tokenDbHelper;
    private BasicUser basicUser;
    private Token token;
    private Profile profile;
    private SweetAlertDialog sweetAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home2);
        setSupportActionBar(viewBinding.toolbar);
        userDbHelper = new UserDbHelper(this);
        tokenDbHelper = new TokenDbHelper(this);
        basicUser = userDbHelper.getBasicUser();
        token = tokenDbHelper.getToken();
        getProfile();
        resolveView(basicUser);
        viewBinding.userProfileName.setText(basicUser.getFirstname() + " " + basicUser.getLastname());
        viewBinding.userProfileShortRole.setText("( "+basicUser.getRole()+" )");
    }

    public void setProfileValues(Profile profile){
        if (profile != null){
            viewBinding.inputEmail.setText(profile.getEmail());
            viewBinding.inputPhoneNumber.setText(profile.getPhoneNumber());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_password:
                i = new Intent(HomeActivity.this, PasswordActivity.class);
                startActivity(i);
                break;

            case R.id.action_setings:
                i = new Intent(HomeActivity.this , SettingsActivity.class);
                startActivity(i);
                break;

            case R.id.action_logout:
                i = new Intent(HomeActivity.this , LoginActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.action_ok:
                updateRoleProfile();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateRoleProfile() {
        if (basicUser.getRole().equals("Transporters")){
            updateTransporterProfile();
        }

        if (basicUser.getRole().equals("Shippers")){
            updateShipperProfile();
        }
    }

    private void updateShipperProfile() {
        //TODO logic to update shipper profile

        try {
            ShipperProfile sp = getShipperInput();

            APIProfileService mApiService = ServiceGenerator.createService(APIProfileService.class , tokenDbHelper.getToken() ,
                    HomeActivity.this , userDbHelper.getBasicUser());

            launchSweetDialogProgress();

            mApiService.updateShipperProfile(userDbHelper.getBasicUser().getProfileId() , sp.toFieldMap()).enqueue(new Callback<RoleProfileResult>() {
                @Override
                public void onResponse(Call<RoleProfileResult> call, Response<RoleProfileResult> response) {
                    if (sweetAlertDialog != null){
                        sweetAlertDialog.dismiss();
                    }

                    Toast.makeText(HomeActivity.this , response.message() , Toast.LENGTH_SHORT).show();

                    if (response.isSuccessful()){
                        Toast.makeText(HomeActivity.this , "Updated." , Toast.LENGTH_SHORT).show();
                        launchSweetDialogSuccess();
                    }
                }

                @Override
                public void onFailure(Call<RoleProfileResult> call, Throwable t) {
                    t.printStackTrace();
                    if (sweetAlertDialog != null){
                        sweetAlertDialog.dismiss();
                    }

                    Toast.makeText(HomeActivity.this , "Oops ! Network error." , Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateTransporterProfile() {
        //TODO logic to update transporter profile

        try {
            TransporterProfile tp = getTransporterInput();

            APIProfileService mApiService = ServiceGenerator.createService(APIProfileService.class , tokenDbHelper.getToken() ,
                    HomeActivity.this , userDbHelper.getBasicUser());

            launchSweetDialogProgress();

            mApiService.updateTransporterProfile(userDbHelper.getBasicUser().getProfileId() , tp.toFieldMap()).enqueue(new Callback<RoleProfileResult>() {
                @Override
                public void onResponse(Call<RoleProfileResult> call, Response<RoleProfileResult> response) {
                    if (sweetAlertDialog != null){
                        sweetAlertDialog.dismiss();
                    }

                    if (response.isSuccessful()){
                        Toast.makeText(HomeActivity.this , "Updated." , Toast.LENGTH_SHORT).show();
                        launchSweetDialogSuccess();
                    }
                }

                @Override
                public void onFailure(Call<RoleProfileResult> call, Throwable t) {
                    t.printStackTrace();
                    if (sweetAlertDialog != null){
                        sweetAlertDialog.dismiss();
                    }

                    Toast.makeText(HomeActivity.this , "Oops ! Network error." , Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void launchSweetDialogProgress(){
        sweetAlertDialog = new SweetAlertDialog(HomeActivity.this , SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("updating ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    private void launchSweetDialogSuccess(){
        sweetAlertDialog = new SweetAlertDialog(HomeActivity.this , SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Updated");
        sweetAlertDialog.setCancelable(true);
        sweetAlertDialog.show();
    }

    private void getProfile() {

        APIProfileService apiProfileService =
                ServiceGenerator.createService(APIProfileService.class, tokenDbHelper.getToken(),
                        this, userDbHelper.getBasicUser());
        Call<Profile> call = apiProfileService.getProfile();
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                System.out.println(" +++++++ RESPONSE +++++ " + response.message());

                for (int i = 0; i < response.headers().size(); i++) {
                    System.out.println(response.headers().name(i) + " : " + response.headers().value(i));
                }

                if (response.isSuccessful()) {
                    String resp = response.message();
                    System.out.println(" RESULT " + response.body().toString());
                    Toast.makeText(HomeActivity.this, "Refreshing",
                            Toast.LENGTH_LONG).show();
                    profile = response.body();

                    setProfileValues(response.body());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                t.printStackTrace();
                System.out.println(" +++ FAILED RESPONSE " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Error occured while accessing server",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    public void resolveView(BasicUser basicUser){

        System.out.println(" ++++ USEER ROLE ++++++ "+basicUser.getRole());

        if (basicUser.getRole().equals("Transporters")){
            viewBinding.layoutShipperView.setVisibility(View.GONE);
        }

        if (basicUser.getRole().equals("Shippers")){
            viewBinding.layoutTransporterView.setVisibility(View.GONE);
        }
    }

    private TransporterProfile getTransporterInput(){
        String id = basicUser.getProfileId();
        String phoneNumber = viewBinding.inputPhoneNumber.getText().toString().trim();
        String email  = viewBinding.inputEmail.getText().toString().trim();
        String address = viewBinding.inputAddress.getText().toString().trim();
        String companyName = viewBinding.inputCompanyName.getText().toString().trim();
        String registrationCertNo = viewBinding.inputRegistrationCertificateNumber.getText().toString().trim();
        String companyPinNo = viewBinding.inputCompanyPinNumber.getText().toString().trim();
        String companyProfile = viewBinding.inputCompanyProfile.getText().toString().trim();
        String directorName = viewBinding.inputDirectorName.getText().toString().trim();
        String directorIdNumber = viewBinding.inputDirectorIdNumber.getText().toString().trim();
        String directorPinNumber = viewBinding.inputDirectorPinNumber.getText().toString().trim();

        TransporterProfile tp = new TransporterProfile();
        tp.setId(Integer.parseInt(id));
        tp.setPhoneNumber(phoneNumber);
        tp.setEmail(email);
        tp.setAddress(address);
        tp.setCompanyName(companyName);
        tp.setRegistrationCertificateNumber(registrationCertNo);
        tp.setCompanyPinNumber(companyPinNo);
        tp.setCompanyProfile(companyProfile);
        tp.setDirectorName(directorName);
        tp.setDirectorIdNumber(directorIdNumber);
        tp.setDirectorPinNumber(directorPinNumber);

        return tp;
    }

    private ShipperProfile getShipperInput(){
        String id = basicUser.getProfileId();
        String phoneNumber = viewBinding.inputPhoneNumber.getText().toString().trim();
        String email  = viewBinding.inputEmail.getText().toString().trim();
        String address = viewBinding.inputAddress.getText().toString().trim();
        String companyName = viewBinding.inputCompanyName.getText().toString().trim();
        String pinNumber = viewBinding.inputPinNumber.getText().toString().trim();

        ShipperProfile sp = new ShipperProfile();
        sp.setId(Integer.parseInt(id));
        sp.setEmail(email);
        sp.setPhoneNumber(phoneNumber);
        sp.setAddress(address);
        sp.setCompanyName(companyName);
        sp.setPinNumber(pinNumber);

        return sp;
    }
}
