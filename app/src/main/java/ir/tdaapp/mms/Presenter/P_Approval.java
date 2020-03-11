package ir.tdaapp.mms.Presenter;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_Approval;
import ir.tdaapp.mms.Model.Services.S_Approval;
import ir.tdaapp.mms.Model.Utilitys.Error;
import ir.tdaapp.mms.Model.ViewModels.VM_Approvals;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class P_Approval {
    private Context context;
    private S_Approval s_approval;
    private Api_Approval api_approval;
    private Disposable dispose_GetVals, dispose_SetItem;

    public P_Approval(Context context, S_Approval s_approval) {
        this.context = context;
        this.s_approval = s_approval;
        api_approval = new Api_Approval();
    }

    public void Start() {

        if (((CentralActivity) context).getTbl_role().HasRole()) {

            s_approval.OnStart();
            s_approval.onHideAll();
            s_approval.onLoading(true);
            GetVals(0);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.PleaseSelectOneRole), Toast.LENGTH_SHORT).show();
        }


    }

    //در اینجا مقادیر از سرور گرفته می شوند
    private void GetVals(int Page) {

        Single<List<VM_Approvals>> vals = api_approval.GetApprovals(Page);
        dispose_GetVals = vals.subscribeWith(new DisposableSingleObserver<List<VM_Approvals>>() {
            @Override
            public void onSuccess(List<VM_Approvals> vm_approvals) {
                s_approval.onLoading(false);
                s_approval.onHideAll();
                s_approval.onSuccess();
                SetItem(vm_approvals);
            }

            @Override
            public void onError(Throwable e) {
                s_approval.onHideAll();
                s_approval.onError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا مقادیر یکی یکی برای ست شدن در رسایکلر ویو ارسال می شوند
    private void SetItem(List<VM_Approvals> vals) {
        Observable<VM_Approvals> list = Observable.fromIterable(vals);

        dispose_SetItem = list.subscribe(approvals -> {
            s_approval.onSetItem(approvals);
        }, throwable -> {

        }, () -> {
            s_approval.onFinish();
        });
    }

    //در اینجا لیست دیسپوزیبل ها را پاس می دهد تا درصورت بسته شده صفحه عملیات ما هم لغو شوند
    public CompositeDisposable GetDisposables() {
        CompositeDisposable composite = new CompositeDisposable();

        if (dispose_GetVals != null) {
            composite.add(dispose_GetVals);
        }

        if (dispose_SetItem != null) {
            composite.add(dispose_SetItem);
        }

        return composite;
    }
}
