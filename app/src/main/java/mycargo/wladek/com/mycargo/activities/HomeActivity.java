package mycargo.wladek.com.mycargo.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import mycargo.wladek.com.mycargo.R;
import mycargo.wladek.com.mycargo.databinding.ActivityHome2Binding;
import mycargo.wladek.com.mycargo.helpers.TokenDbHelper;
import mycargo.wladek.com.mycargo.helpers.UserDbHelper;
import mycargo.wladek.com.mycargo.pojo.BasicUser;
import mycargo.wladek.com.mycargo.pojo.Profile;
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
    private String token;
    private Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home2);
        setSupportActionBar(viewBinding.toolbar);
        userDbHelper = new UserDbHelper(this);
        tokenDbHelper = new TokenDbHelper(this);
        basicUser = userDbHelper.getBasicUser();
        token = tokenDbHelper.getToken().getAccessToken();
        getProfile();
        resolveView(basicUser);
        viewBinding.userProfileName.setText(basicUser.getFirstname() + " " + basicUser.getLastname());
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
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(HomeActivity.this, resp,
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
            viewBinding.layoutTransporterView.setVisibility(View.VISIBLE);
        }

        if (basicUser.getRole().equals("Shippers")){
            viewBinding.layoutShipperView.setVisibility(View.VISIBLE);
        }
    }
}
