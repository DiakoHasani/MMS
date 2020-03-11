package ir.tdaapp.mms.Presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_Role;
import ir.tdaapp.mms.Model.Services.S_RoleDialog;
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.Utilitys.Error;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.Model.ViewModels.VM_Role;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;
import ir.tdaapp.mms.View.Activitys.MainActivity;

public class P_RoleDialog extends BaseApi {

    Context context;
    S_RoleDialog s_roleDialog;
    Api_Role api_role;
    Disposable dispose_GetRoles, dispose_SetRoles;
    //در اینجا آی دی نقش ها نگهداری می شوند به طوری اولین خانه آرایه مربوط به اولین رادیو باتن نقش ها می باشد و دومین خانه هم به این صورت
    Integer[] RolesId;

    //در اینجا مشخص می شود که در کدام خانه آرایه هستیم
    int Index = 0;

    public P_RoleDialog(Context context, S_RoleDialog s_roleDialog) {
        this.context = context;
        this.s_roleDialog = s_roleDialog;
        api_role = new Api_Role();
        RolesId = new Integer[8];
    }

    public void Start() {
        s_roleDialog.OnStart();
        s_roleDialog.onHideAll();
        s_roleDialog.onLoading(true);

        GetRoles();
    }

    //در اینجا نقش ها در سرور گرفته می شوند
    private void GetRoles() {

        //در اینجا آی دی کاربر گرفته می شود
        int UserId = ((CentralActivity) context).getTbl_user().GetUserId();

        //در اینجا براساس آی دی کاربر نقش های آن در سرور گرفته می شود
        Single<List<VM_Role>> vals = api_role.GetUsersRole(UserId);

        dispose_GetRoles = vals.subscribeWith(new DisposableSingleObserver<List<VM_Role>>() {
            @Override
            public void onSuccess(List<VM_Role> vm_roles) {
                s_roleDialog.onHideAll();
                s_roleDialog.onSuccess();
                SetRoles(vm_roles);
            }

            @Override
            public void onError(Throwable e) {
                s_roleDialog.onHideAll();
                s_roleDialog.onError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا نقش ها به صفحه پاس داده می شوند
    private void SetRoles(List<VM_Role> roles) {

        Observable<VM_Role> list = Observable.fromIterable(roles);

        //در اینجا نقش فعلی کاربر را به دست می آوریم
        int RoleUser = ((CentralActivity) context).getTbl_role().GetRoleId();

        dispose_SetRoles = list.subscribe(vm_role -> {
            RolesId[Index] = vm_role.getId();
            s_roleDialog.onSetRole(vm_role);

            //در اینجا بر اساس نقش فعلی کاربر رادیو باتن آن را تیک دار می کنیم
            if (RoleUser == vm_role.getId()) {
                s_roleDialog.onCheckedMyRole(Index);
            }

            Index++;
        }, throwable -> {

        }, () -> {
            s_roleDialog.onFinish();
        });
    }

    //در اینجا نقش کاربر ذخیره می شود
    public void SaveRole(int Checked) {

        //در اینجا نقش کاربر را از آرایه می گیریم
        int RoleId = RolesId[Checked];

        try {
            //در اینجا نقش را اضافه می کنیم
            ((CentralActivity) context).getTbl_role().AddRole(RoleId);

            //در اینجا برنامه را ریست می کنیم
            context.startActivity(new Intent(context, MainActivity.class));
            ((CentralActivity) context).finish();

        } catch (Exception e) {
            Toast.makeText(context, context.getResources().getString(R.string.Operation_error_occurred), Toast.LENGTH_SHORT).show();
        }

    }

    //در اینجا لیست دیسپوزیبل ها را پاس می دهد تا درصورت بسته شده صفحه عملیات ما هم لغو شوند
    public CompositeDisposable GetDisposables() {
        CompositeDisposable composite = new CompositeDisposable();

        if (dispose_GetRoles != null) {
            composite.add(dispose_GetRoles);
        }

        if (dispose_SetRoles != null) {
            composite.add(dispose_SetRoles);
        }

        return composite;
    }

}
