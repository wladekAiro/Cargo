package mycargo.wladek.com.mycargo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wladek on 5/31/17.
 */

public class RoleProfileResult {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("phoneNumberConfirmed")
    @Expose
    private boolean phoneNumberConfirmed;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("emailConfirmed")
    @Expose
    private boolean emailConfirmed;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("companyConfirmed")
    @Expose
    private boolean companyConfirmed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPhoneNumberConfirmed() {
        return phoneNumberConfirmed;
    }

    public void setPhoneNumberConfirmed(boolean phoneNumberConfirmed) {
        this.phoneNumberConfirmed = phoneNumberConfirmed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isCompanyConfirmed() {
        return companyConfirmed;
    }

    public void setCompanyConfirmed(boolean companyConfirmed) {
        this.companyConfirmed = companyConfirmed;
    }
}
