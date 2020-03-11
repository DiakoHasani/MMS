package ir.tdaapp.mms.Presenter;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_Meeting;
import ir.tdaapp.mms.Model.Services.S_Meeting;
import ir.tdaapp.mms.Model.Utilitys.Error;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class P_Meeting {
    Context context;
    S_Meeting s_meeting;
    Api_Meeting api_meeting;
    private Disposable dispose_GetVals, dispose_SetItem;

    public P_Meeting(Context context, S_Meeting s_meeting) {
        this.context = context;
        this.s_meeting = s_meeting;
        api_meeting = new Api_Meeting();
    }

    public void Start() {

        if (((CentralActivity) context).getTbl_role().HasRole()) {

            s_meeting.OnStart();
            s_meeting.onHideAll();
            s_meeting.onLoading(true);
            GetVals(0);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.PleaseSelectOneRole), Toast.LENGTH_SHORT).show();
        }


    }

    //در اینجا مقادیر از سرور گرفته می شوند
    private void GetVals(int Page) {

        Single<List<VM_Meetings>> vals = api_meeting.GetMeetings(Page);
        dispose_GetVals = vals.subscribeWith(new DisposableSingleObserver<List<VM_Meetings>>() {
            @Override
            public void onSuccess(List<VM_Meetings> vm_meetings) {
                s_meeting.onLoading(false);
                s_meeting.onHideAll();
                s_meeting.onSuccess();
                SetItem(vm_meetings);
            }

            @Override
            public void onError(Throwable e) {
                s_meeting.onHideAll();
                s_meeting.onError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا مقادیر یکی یکی برای ست شدن در رسایکلر ویو ارسال می شوند
    private void SetItem(List<VM_Meetings> vals) {
        Observable<VM_Meetings> list = Observable.fromIterable(vals);

        dispose_SetItem = list.subscribe(meetings -> {
            s_meeting.onSetItem(meetings);
        }, throwable -> {

        }, () -> {
            s_meeting.onFinish();
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
