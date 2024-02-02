package everyide.webide.config.auth.dto.request;

public class PasswordChangeRequest {
    private String email;
    private String oldPassword;
    private String newPassword;

    // Getter and Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
