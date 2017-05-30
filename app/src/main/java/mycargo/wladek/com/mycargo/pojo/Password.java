package mycargo.wladek.com.mycargo.pojo;

/**
 * Created by wladek on 5/25/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Password {
    @SerializedName("oldPassword")
    @Expose
    private String oldPassword;
    @SerializedName("newPassword")
    @Expose
    private String newPassword;
    @SerializedName("confirmPassword")
    @Expose
    private String confirmPassword;

    /**
     * No args constructor for use in serialization
     */
    public Password() {
    }

    /**
     * @param confirmPassword
     * @param newPassword
     * @param oldPassword
     */
    public Password(String oldPassword, String newPassword, String confirmPassword) {
        super();
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
