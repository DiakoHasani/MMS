package ir.tdaapp.mms.Presenter;

import android.content.Context;
import android.widget.EditText;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ir.tdaapp.li_utility.Codes.Validation;
import ir.tdaapp.mms.Model.Repositorys.DataBase.Tbl_Role;
import ir.tdaapp.mms.Model.Repositorys.DataBase.Tbl_User;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_Login;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_User;
import ir.tdaapp.mms.Model.Services.S_Login;
import ir.tdaapp.mms.Model.Utilitys.Error;
import ir.tdaapp.mms.Model.ViewModels.VM_Login;
import ir.tdaapp.mms.Model.ViewModels.VM_Message;
import ir.tdaapp.mms.R;

public class P_Login {
    private S_Login s_login;
    private Api_Login api_login;
    private Api_User api_user;
    Context context;
    Tbl_User tbl_user;
    Tbl_Role tbl_role;

    Disposable dispose_StartLogin, dispose_GetRoles;

    public P_Login(Context context, S_Login s_login) {
        this.s_login = s_login;
        api_login = new Api_Login();
        api_user = new Api_User();
        this.context = context;
        tbl_user = new Tbl_User(context);
        tbl_role = new Tbl_Role(context);
    }

    //اینجا زمانی که کاربر دکمه لاگین را فشار دهد فراخوانی می شود
    public void StartLogin(VM_Login vm_login) {
        s_login.Loading(true);

        dispose_StartLogin = api_login.data(vm_login).subscribeWith(new DisposableSingleObserver<VM_Message>() {
            @Override
            public void onSuccess(VM_Message message) {

                //اگر در سرور لاگین با موفقیت انجام شود شرط زیر اجرا می شود
                if (message.isResault()) {
                    //در اینجا یوزر آی دی کاربر ذخیره میشود
                    SaveUserId(Integer.valueOf(message.getMessageText()));

                    //در اینجا نتیجه عملیات موفق به اکتیویتی ارسال می شود
                    s_login.IsSuccess(message.getCode(), Integer.valueOf(message.getMessageText()));
                }
                //اگر لاگین در سرور با موفقیت انجام نشد شرط زیر اجرا می شود
                else {
                    s_login.Loading(false);
                    s_login.NotSuccess(message);
                }
            }

            @Override
            public void onError(Throwable e) {
                s_login.Loading(false);
                s_login.OnError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا ولید بودن ادیت تکست ها چک می شود
    public void CheckValidation(Context context, EditText txt_UserName, EditText txt_Password) {

        try {
            boolean resault = true;

            //در اینجا ولیدیشن ادیت تکست نام کاربری چک می شود
            if (!Validation.NationalCode(context.getResources().getString(R.string.Please_be_careful_in_entering_your_national_code), txt_UserName)) {
                resault = false;
            }

            //در اینجا ولیدیشن ادیت تکست پسوورد چک می شود
            if (!Validation.Required(txt_Password, context.getResources().getString(R.string.Input_Your_Password))) {
                resault = false;
            }

            if (resault) {
                s_login.IsValid();
            } else {
                s_login.NotValid();
                s_login.Loading(false);
            }

        } catch (Exception e) {
            s_login.NotValid();
            s_login.Loading(false);
        }

    }

    //در اینجا یوزر آی دی کاربر در حافظه ذخیره می شود
    private void SaveUserId(int UserId) {
        tbl_user.AddUser(UserId);
    }

    //اینجا زمانی که کاربر یک نقش داشته باشد فراخوانی می شود و نقش آن ذخیر می شود
    public void GetRoles(int UserId) {
        dispose_GetRoles = api_user.GetRolesUser(UserId).subscribeWith(new DisposableSingleObserver<List<Integer>>() {
            @Override
            public void onSuccess(List<Integer> integers) {

                int RoleId = 0;

                //در اینجا چک می کند که کاربر نقش دارد اگر نداشته باشد شرط زیر اجرا نمی شود
                if (integers.size() > 0)
                    RoleId = integers.get(0);

                SaveRoleId(RoleId);
            }

            @Override
            public void onError(Throwable e) {
                s_login.Loading(false);
                s_login.OnErrorGetRole(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا نقش کاربر در حافظه ذخیره می شود
    private void SaveRoleId(int RoleId) {

        //اگر متغیر زیر صفر باشد یعنی کاربر نقش ندارد و شرط زیر اجرا نمی شود
        if (RoleId != 0)
            tbl_role.AddRole(RoleId);

        //در اینجا لودینگ غیر فعال می شود
        s_login.Loading(false);
        s_login.OnSuccessGetRole();
    }

    //در اینجا لیست دیسپوزیبل ها را پاس می دهد تا درصورت بسته شده صفحه عملیات ما هم لغو شوند
    public CompositeDisposable GetDisposables(String TAG) {
        CompositeDisposable composite = new CompositeDisposable();

        if (dispose_StartLogin != null) {
            composite.add(dispose_StartLogin);
        }

        if (dispose_GetRoles != null) {
            composite.add(dispose_GetRoles);
        }

        api_login.Cancel(TAG, context);
        api_user.Cancel(TAG, context);

        return composite;
    }

}
