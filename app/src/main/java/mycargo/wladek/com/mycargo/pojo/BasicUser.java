package mycargo.wladek.com.mycargo.pojo;

import com.auth0.android.jwt.JWT;

import java.io.Serializable;

import mycargo.wladek.com.mycargo.util.StringUtils;

/**
 * Created by wladek on 5/24/17.
 */

public class BasicUser implements Serializable {
    private String nameidentifier;
    private String email;
    private String key;
    private String securityStamp;
    private String role;
    private String firstname;
    private String othername;
    private String lastname;
    private String profileId;

    public BasicUser() {
    }

    public BasicUser(JWT jwt , String password) {

        this.nameidentifier = jwt.getClaim(StringUtils.NAME_IDENTIFIER).asString();
        this.email = jwt.getClaim(StringUtils.EMAIL).asString();
        this.key = password;
        this.securityStamp = jwt.getClaim(StringUtils.SECURITY_STAMP).asString();
        this.role = jwt.getClaim(StringUtils.USER_ROlE).asString();
        this.firstname = jwt.getClaim(StringUtils.FIRST_NAME).asString();
        this.othername = jwt.getClaim(StringUtils.OTHER_NAME).asString();
        this.lastname = jwt.getClaim(StringUtils.LAST_NAME).asString();

        if (this.role.toLowerCase().equals("transporters")){
            this.profileId = jwt.getClaim(StringUtils.TRANSPORTER_ID).asString();
        }else if (this.role.toLowerCase().equals("shippers")){
            this.profileId = jwt.getClaim(StringUtils.SHIPPER_ID).asString();
        }

    }

    public String getNameidentifier() {
        return nameidentifier;
    }

    public void setNameidentifier(String nameidentifier) {
        this.nameidentifier = nameidentifier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecurityStamp() {
        return securityStamp;
    }

    public void setSecurityStamp(String securityStamp) {
        this.securityStamp = securityStamp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
