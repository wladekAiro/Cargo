package mycargo.wladek.com.mycargo.enums;

/**
 * Created by wladek on 5/27/17.
 */

public enum  RegistrationType {
    Shipper ("Shipper") , Transporter("Transporter");

    private String friendlyName;

    RegistrationType(String friendlyName){
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
