package ir.tdaapp.mms.Model.ViewModels;

//برای مشخصات کاربر در لاگین
public class VM_Login {
    private String UserName;
    private String Password;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
