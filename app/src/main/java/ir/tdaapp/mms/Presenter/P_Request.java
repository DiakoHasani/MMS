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
import ir.tdaapp.mms.Model.ViewModels.VM_RequestsDataSpinners;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class P_Request {

    private S_Request s_request;
    private Context context;
    private Api_Request api_request;
    Disposable dispose_GetVals, dispose_SetItems, dispose_getInitialValue;

    public P_Request(S_Request s_request, Context context) {
        this.s_request = s_request;
        this.context = context;
        api_request = new Api_Request();
    }

    //در اینجا عملیات اولیه انجام می شود
    public void Start(VM_FilterRequest filter, boolean getCouncils) {

        if (((CentralActivity) context).getTbl_role().HasRole()) {

            s_request.Start();
            s_request.HideAll();
            s_request.Loading(true);

            getInitialValue(filter.getCouncilId(), getCouncils);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.PleaseSelectOneRole), Toast.LENGTH_SHORT).show();
        }
    }

    //در اینجا درخواست ها را از سرور می گیرد
    public void getRequests(VM_FilterRequest filter) {

        s_request.Loading(true);

        Single<List<VM_Requests>> vals = api_request.GetRequests(filter);
        dispose_GetVals = vals.subscribeWith(new DisposableSingleObserver<List<VM_Requests>>() {
            @Override
            public void onSuccess(List<VM_Requests> requests) {
                s_request.Loading(false);
                s_request.OnSuccess();
                SetItems(requests);
            }

            @Override
            public void onError(Throwable e) {
                s_request.HideAll();
                s_request.OnError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا مقادیر اسپینرها گرفته میشود
    void getInitialValue(int councilId, boolean getCouncils) {

        int userId = ((CentralActivity) context).getTbl_user().GetUserId();
        int roleId = ((CentralActivity) context).getTbl_role().GetRoleId();

        Single<VM_RequestsDataSpinners> vals = api_request.getSpinnerData(userId, roleId, councilId);

        dispose_getInitialValue = vals.subscribeWith(new DisposableSingleObserver<VM_RequestsDataSpinners>() {
            @Override
            public void onSuccess(VM_RequestsDataSpinners vm_requestsDataSpinners) {
                s_request.Loading(false);
                s_request.HideAll();

                if (getCouncils) {
                    SetCouncil(vm_requestsDataSpinners.getCouncils());
                }

                SetMeetings(vm_requestsDataSpinners.getMeetings());
                SetShowElements(vm_requestsDataSpinners.isManegment(), vm_requestsDataSpinners.getCouncils().size());

                s_request.OnSuccessGetSpinners();
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
        } else {
            s_request.onShowManegmentRequests(false);
        }

        //در اینجا اگر تعداد شوراها یکی باشد یعنی فقط یک آیتم که آن هم ثابت است دارد و هیچ شورایی ندارد
        if (countCouncil <= 1) {
            s_request.onShowSpinnerCouncil(false);
            s_request.onNotMemberAnyCouncil();
        }
        //در اینجا چون کاربر تک شورایی است باید اسپینر شوراها مخفی شود اما بدلیل باگ اسپینر که درحالت مخفی نمی توان یک سطر را انتخاب کرد فعلا اسپینر را نشان می دهیم
        else if (countCouncil == 2) {
            s_request.onShowSpinnerCouncil(true);
        }
        //اگر تعداد شورا بیشتر از یک باشد اسپینر مربوط به شورا نمایش داده می شود
        else {
            s_request.onShowSpinnerCouncil(true);
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
    public CompositeDisposable GetDisposables(String TAG) {
        CompositeDisposable composite = new CompositeDisposable();

        if (dispose_GetVals != null) {
            composite.add(dispose_GetVals);
        }

        if (dispose_SetItems != null) {
            composite.add(dispose_SetItems);
        }

        if (dispose_getInitialValue != null) {
            composite.add(dispose_getInitialValue);
        }

        api_request.Cancel(TAG, context);

        return composite;
    }
}
