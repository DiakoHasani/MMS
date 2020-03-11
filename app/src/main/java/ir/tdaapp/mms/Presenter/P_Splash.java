package ir.tdaapp.mms.Presenter;

import android.content.Context;
import android.os.Handler;

import ir.tdaapp.mms.Model.Repositorys.DataBase.Tbl_User;
import ir.tdaapp.mms.Model.Services.S_Splash;

import static android.content.Context.MODE_PRIVATE;

public class P_Splash {
    private S_Splash s_splash;
    Context context;
    Handler handler_Logo, handler_Title, handler_Splash, handler_CheckHasAccount;
    Tbl_User tbl_user;

    public P_Splash(Context context, S_Splash s_splash) {
        this.s_splash = s_splash;
        this.context = context;

        tbl_user = new Tbl_User(context);
    }

    public void Start() {
        s_splash.HideAll();
        ShowLogo();
    }

    //در اینجا در زمان معلوم شده نمایش لوگو در اکتیویتی فراخوانی می شود
    private void ShowLogo() {
        handler_Logo = new Handler();
        handler_Logo.postDelayed(() -> {

            s_splash.ShowLogo();
            ShowTitle();

        }, 600);
    }

    //در اینجا در زمان معلوم شده نمایش تایتل در اکتیویتی فراخوانی می شود
    private void ShowTitle() {
        handler_Title = new Handler();
        handler_Title.postDelayed(() -> {

            s_splash.ShowTitle();
            ShowProgressBar();

        }, 500);
    }

    //در اینجا در زمان معلوم شده نمایش پروگرس بار در اکتیویتی فراخوانی می شود
    private void ShowProgressBar() {
        handler_Splash = new Handler();
        handler_Splash.postDelayed(() -> {
            s_splash.ShowProgressBar();
            GetHasAccount();
        }, 500);
    }

    //در اینجا بعد از وقت معلوم شده شروع به چک دیتابیس می کند
    private void GetHasAccount() {
        handler_CheckHasAccount = new Handler();
        handler_CheckHasAccount.postDelayed(() -> {
            CheckHasAccount();
        }, 300);
    }

    //در اینجا دیتابیس را چک می کند
    private void CheckHasAccount() {
        s_splash.HasAccount(tbl_user.HasAccount());
    }
}
