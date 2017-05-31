package mycargo.wladek.com.mycargo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wladek on 5/28/17.
 */

public class ShipperProfile extends RoleProfile {
    @SerializedName("PinNumber")
    @Expose
    private String PinNumber;

    public String getPinNumber() {
        return PinNumber;
    }

    public void setPinNumber(String pinNumber) {
        PinNumber = pinNumber;
    }

    public Map<String , String> toFieldMap(){
        Map<String , String> vals = new HashMap<>();
        vals.put("Id" , getId()+"");
        vals.put("PhoneNumber" , getPhoneNumber());
        vals.put("Email" , getEmail());
        vals.put("Address" , getAddress());
        vals.put("CompanyName" , getCompanyName());
        vals.put("PinNumber" , getPinNumber());

        return vals;
    }
}
