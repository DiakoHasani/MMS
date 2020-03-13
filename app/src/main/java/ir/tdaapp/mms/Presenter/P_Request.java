package ir.tdaapp.mms.Presenter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_Request;
import ir.tdaapp.mms.Model.Services.S_Request;
import ir.tdaapp.mms.Model.Utilitys.Error;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_FilterRequest;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_MyRequests;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class P_Request {

    private S_Request s_request;
    private Context context;
    private Api_Request api_request;
    Disposable dispose_GetVals, dispose_SetItems;

    public P_Request(S_Request s_request, Context context) {
        this.s_request = s_request;
        this.context = context;
        api_request = new Api_Request();
    }

    //در اینجا عملیات اولیه انجام می شود
    public void Start(VM_FilterRequest filter) {

        if (((CentralActivity) context).getTbl_role().HasRole()) {

            s_request.Start();
            s_request.HideAll();
            s_request.Loading(true);
            GetVals(filter);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.PleaseSelectOneRole), Toast.LENGTH_SHORT).show();
        }


    }

    //در اینجا درخواست ها را از سرور می گیرد
    private void GetVals(VM_FilterRequest filter) {

        Single<VM_MyRequests> vals = api_request.GetRequests(filter);
        dispose_GetVals = vals.subscribeWith(new DisposableSingleObserver<VM_MyRequests>() {
            @Override
            public void onSuccess(VM_MyRequests my_Requests) {
                s_request.Loading(false);
                s_request.HideAll();
                s_request.OnSuccess();

                SetItems(my_Requests.getRequests());
                SetMeetings(my_Requests.getMeetings());
                SetCouncil(my_Requests.getCouncils());
                SetShowElements(my_Requests.isManegment(),my_Requests.getCouncils().size());
            }

            @Override
            public void onError(Throwable e) {
                s_request.HideAll();
                s_request.OnError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا آداپتر جلسات برای اسپینر صفحه درخواست ها ارسال می شود
    private void SetMeetings(List<VM_Meetings> meetings) {
        ArrayAdapter<VM_Meetings> adapter = new ArrayAdapter<>(context,
                R.layout.spinner_item,
                meetings);

        s_request.onGetMeetings(adapter);
    }

    //در اینجا آداپتر مشاوران برای اسپینر صفحه درخواست ها ارسال می شود
    private void SetCouncil(List<VM_Councils> councils) {
        ArrayAdapter<VM_Councils> adapter = new ArrayAdapter<>(context,
                R.layout.spinner_item,
                councils);

        s_request.onGetCouncil(adapter);
    }

    //در اینجا ست می شود که المنت های صفحه مانند اسپینر شوراها یا دکمه بررسی درخواست ها نمایش داده شود یا خیر
    private void SetShowElements(boolean isManegment, int countCouncil) {

        if (isManegment) {
            s_request.onShowManegmentRequests(true);
        }else{
            s_request.onShowManegmentRequests(false);
        }

        if (countCouncil > 1) {
            s_request.onShowSpinnerCouncil(true);
        }else{
            s_request.onShowSpinnerCouncil(false);
        }

    }

    // در اینجا پس از دریافت مقدارها از سرور آن ها را یکی یکی برای افزودن به رسایکلر ویو ارسال می کند
    private void SetItems(List<VM_Requests> vals) {
        Observable<VM_Requests> list = Observable.fromIterable(vals);

        dispose_SetItems = list.subscribe(vm_requests -> {
            s_request.RequestItem(vm_requests);
        }, throwable -> {

        }, () -> {
            s_request.Finish();
        });
    }

    //در اینجا لیست دیسپوزیبل ها را پاس می دهد تا درصورت بسته شده صفحه عملیات ما هم لغو شوند
    public CompositeDisposable GetDisposables() {
        CompositeDisposable composite = new CompositeDisposable();

        if (dispose_GetVals != null) {
            composite.add(dispose_GetVals);
        }

        if (dispose_SetItems != null) {
            composite.add(dispose_SetItems);
        }
        return composite;
    }
}
