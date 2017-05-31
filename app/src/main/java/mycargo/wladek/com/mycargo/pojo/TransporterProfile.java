package mycargo.wladek.com.mycargo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wladek on 5/31/17.
 */

public class TransporterProfile extends RoleProfile {
    @SerializedName("RegistrationCertificateNumber")
    @Expose
    private String RegistrationCertificateNumber;
    @SerializedName("CompanyPinNumber")
    @Expose
    private String CompanyPinNumber;
    @SerializedName("CompanyProfile")
    @Expose
    private String CompanyProfile;
    @SerializedName("DirectorName")
    @Expose
    private String DirectorName;
    @SerializedName("DirectorIdNumber")
    @Expose
    private String DirectorIdNumber;
    @SerializedName("DirectorPinNumber")
    @Expose
    private String DirectorPinNumber;

    public String getRegistrationCertificateNumber() {
        return RegistrationCertificateNumber;
    }

    public void setRegistrationCertificateNumber(String registrationCertificateNumber) {
        RegistrationCertificateNumber = registrationCertificateNumber;
    }

    public String getCompanyPinNumber() {
        return CompanyPinNumber;
    }

    public void setCompanyPinNumber(String companyPinNumber) {
        CompanyPinNumber = companyPinNumber;
    }

    public String getCompanyProfile() {
        return CompanyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        CompanyProfile = companyProfile;
    }

    public String getDirectorName() {
        return DirectorName;
    }

    public void setDirectorName(String directorName) {
        DirectorName = directorName;
    }

    public String getDirectorIdNumber() {
        return DirectorIdNumber;
    }

    public void setDirectorIdNumber(String directorIdNumber) {
        DirectorIdNumber = directorIdNumber;
    }

    public String getDirectorPinNumber() {
        return DirectorPinNumber;
    }

    public void setDirectorPinNumber(String directorPinNumber) {
        DirectorPinNumber = directorPinNumber;
    }

    public Map<String , String> toFieldMap(){
        Map<String , String> vals = new HashMap<>();
        vals.put("Id" , getId()+"");
        vals.put("PhoneNumber" , getPhoneNumber());
        vals.put("Email" , getEmail());
        vals.put("Address" , getAddress());
        vals.put("CompanyName" , getCompanyName());
        vals.put("RegistrationCertificateNumber" , getRegistrationCertificateNumber());
        vals.put("CompanyPinNumber" , getCompanyPinNumber());
        vals.put("CompanyProfile" , getCompanyProfile());
        vals.put("DirectorName" , getDirectorName());
        vals.put("DirectorIdNumber" , getDirectorIdNumber());
        vals.put("DirectorPinNumber" , getDirectorPinNumber());

        return vals;
    }
}
