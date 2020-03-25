package ir.tdaapp.mms.Presenter;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_Home;
import ir.tdaapp.mms.Model.Services.S_Home;
import ir.tdaapp.mms.Model.Utilitys.Error;
import ir.tdaapp.mms.Model.ViewModels.VM_Approvals;
import ir.tdaapp.mms.Model.ViewModels.VM_Home;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;
import ir.tdaapp.mms.View.Dialogs.RoleDialog;

public class P_Home {

    private S_Home s_home;
    private Context context;
    private Api_Home api_home;
    Disposable disposGetHomeValues, disposSetMeetings, disposSetRequests, disposSetApprovals;

    public P_Home(Context context, S_Home s_home) {
        this.s_home = s_home;
        this.context = context;

        api_home = new Api_Home();
    }

    public void Start() {

        if (((CentralActivity) context).getTbl_role().HasRole()) {

            //اینجا برای از نو ساخته شدن شی ها فراخوانی می شود
            s_home.Start();

            //اینجا برای مخفی کردن آیتم ها فراخوانی می شود
            s_home.HideAll();

            //اینجا برای لودینگ فراخوانی می شود
            s_home.Loading();

            //در اینجا شروع به دریافت کردن داده ها می کند
            GetHomeValues();

        } else {

            Toast.makeText(context, context.getResources().getString(R.string.PleaseSelectOneRole), Toast.LENGTH_SHORT).show();
            ((CentralActivity) context).ShowRoleDialog();
        }
    }

    //در اینجا اطلاعات مربوط به صفحه اصلی گرفته می شود
    private void GetHomeValues() {

        int UserId = ((CentralActivity) context).getTbl_user().GetUserId();
        int RoleId = ((CentralActivity) context).getTbl_role().GetRoleId();

        Single<VM_Home> vals = api_home.GetVals(UserId, RoleId);

        disposGetHomeValues = vals.subscribeWith(new DisposableSingleObserver<VM_Home>() {
            @Override
            public void onSuccess(VM_Home vm_home) {

                //در اینجا تمام آیتم ها مخفی می شوند
                s_home.HideAll();

                //در اینجا رسایکلرها نمایش داده می شوند
                s_home.ShowMain();

                //در اینجا شروع به ست کردن داده ها در رسایکلر ویو می کند
                SetMeetings(vm_home);
            }

            @Override
            public void onError(Throwable e) {
                s_home.HideAll();
                s_home.OnError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا آیتم های جلسات گرفته می شود و آن را برای ست کردن در آداپتر به سمت فراگمنت ارسال می کند و پس از پایان کار متد مربوط به درخواست ها را فراخوانی می کند
    private void SetMeetings(VM_Home vals) {

        Observable<VM_Meetings> list = Observable.fromIterable(vals.getMeetings());
        disposSetMeetings = list.subscribe(vm_meetings -> {

                    //در اینجا آیتم ها را یکی یکی به سمت فرگمنت ارسال می کند
                    s_home.MeetingItem(vm_meetings);

                }, throwable -> {
                },
                () -> {
                    //هنگامی که عملیات ست کردن جلسات به پایان برسد شروع به ست کردن درخواست ها می کند
                    SetRequests(vals);
                });
    }

    //در اینجا آیتم های درخواست ها گرفته می شود و آن را برای ست کردن در آداپتر به سمت فراگمنت ارسال می کند و پس از پایان کار متد مربوط به صورت جلسات را فراخوانی می کند
    private void SetRequests(VM_Home vals) {

        Observable<VM_Requests> list = Observable.fromIterable(vals.getRequests());

        disposSetRequests = list.subscribe(requests -> {
            s_home.RequestItem(requests);
        }, throwable -> {

        }, () -> {
            //پس از پایان کار متد مربوط به ست کردن مصوبات یا صورت جلسات را فراخوانی می کند
            SetApprovals(vals);
        });
    }

    //در اینجا صورت جلسات یا مصوبات ست می شود
    private void SetApprovals(VM_Home vals) {

        Observable<VM_Approvals> list = Observable.fromIterable(vals.getApprovals());

        disposSetApprovals = list.subscribe(approvals -> {
                    s_home.ApprovalItem(approvals);
                },
                throwable -> {
                },
                () -> {
                    s_home.Finish();
                });
    }

    //در اینجا لیست دیسپوزیبل ها را پاس می دهد تا درصورت بسته شده صفحه عملیات ما هم لغو شوند
    public CompositeDisposable GetDisposables(String TAG) {
        CompositeDisposable composite = new CompositeDisposable();

        if (disposGetHomeValues != null) {
            composite.add(disposGetHomeValues);
        }

        if (disposSetMeetings != null) {
            composite.add(disposSetMeetings);
        }

        if (disposSetRequests != null) {
            composite.add(disposSetRequests);
        }

        if (disposSetApprovals != null) {
            composite.add(disposSetApprovals);
        }

        api_home.Cancel(TAG, context);

        return composite;
    }
}
