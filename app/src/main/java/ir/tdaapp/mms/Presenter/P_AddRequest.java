package ir.tdaapp.mms.Presenter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_Request;
import ir.tdaapp.mms.Model.Services.S_AddRequest;
import ir.tdaapp.mms.Model.Utilitys.Error;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_WorkYear;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class P_AddRequest {

    Context context;
    S_AddRequest s_addRequest;
    Api_Request api_request;
    Disposable dispose_getWorkYears, dispose_getCouncils, dispose_getCouncilSessions;

    public P_AddRequest(Context context, S_AddRequest s_addRequest) {
        this.context = context;
        this.s_addRequest = s_addRequest;
        api_request = new Api_Request();
    }

    public void start() {
        s_addRequest.OnStart();

        s_addRequest.onHideAll();
        s_addRequest.onLoading(true);

        getWorkYears();
    }

    //در اینجا سال کاری دریافت می شود
    void getWorkYears() {

        int userId = ((CentralActivity) context).getTbl_user().GetUserId();
        Single<List<VM_WorkYear>> workYears = api_request.getWorkYears(context, userId);

        dispose_getWorkYears = workYears.subscribeWith(new DisposableSingleObserver<List<VM_WorkYear>>() {
            @Override
            public void onSuccess(List<VM_WorkYear> workYears) {

                s_addRequest.onHideAll();
                s_addRequest.onShowAll();

                ArrayAdapter<VM_WorkYear> adapter = new ArrayAdapter<>(context,
                        R.layout.spinner_item,
                        workYears);

                s_addRequest.onGetWorkYears(adapter);
                SetDefaultSpinnerData();
            }

            @Override
            public void onError(Throwable e) {
                s_addRequest.onHideAll();
                s_addRequest.onError(Error.GetErrorVolley(e.toString()));
            }
        });

    }

    //در اینجا به اسپینرها مقدار پیش فرض داده می شود که بیشتر برای زیبایی می باشد
    public void SetDefaultSpinnerData() {

        List<VM_Councils> councils = new ArrayList<>();
        List<VM_Meetings> meetings = new ArrayList<>();

        councils.add(new VM_Councils(0, context.getResources().getString(R.string.SelectedOneYearOfWork)));
        meetings.add(new VM_Meetings(0, context.getResources().getString(R.string.SelectedOneCouncil)));

        ArrayAdapter<VM_Councils> councilsAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, councils);
        ArrayAdapter<VM_Meetings> meetingsAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, meetings);

        s_addRequest.onSetDefaultSpinnersData(councilsAdapter, meetingsAdapter);
    }

    //در اینجا داده پیش فرض اسپینر جلسات ست می شود
    public void setDefaultValueSpinnerCouncilSession(){

        List<VM_Meetings> meetings = new ArrayList<>();
        meetings.add(new VM_Meetings(0, context.getResources().getString(R.string.SelectedOneCouncil)));
        ArrayAdapter<VM_Meetings> meetingsAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, meetings);

        s_addRequest.onSetDefaultSpinnerSessionData(meetingsAdapter);
    }

    //در اینجا لیست شوراها از سرور گرفته می شود
    public void getCouncils(int workId) {

        s_addRequest.onLoading(true);

        //در اینجا اگر قبلا کاربر این متد را فراخوانی کرده باشد قبلی را لغو می کند
        if (dispose_getCouncils != null) {
            dispose_getCouncils.dispose();
        }

        int userId = ((CentralActivity) context).getTbl_user().GetUserId();

        Single<List<VM_Councils>> councils = api_request.getCoucils(context, userId, workId);

        dispose_getCouncils = councils.subscribeWith(new DisposableSingleObserver<List<VM_Councils>>() {
            @Override
            public void onSuccess(List<VM_Councils> vm_councils) {
                s_addRequest.onLoading(false);

                ArrayAdapter<VM_Councils> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, vm_councils);
                s_addRequest.onGetCouncils(adapter);
            }

            @Override
            public void onError(Throwable e) {
                s_addRequest.onHideAll();
                s_addRequest.onError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا لیست جلسات از سرور گرفته می شود
    public void getCouncilSessions(int workId, int councilId) {

        s_addRequest.onLoading(true);

        if (dispose_getCouncilSessions != null) {
            dispose_getCouncilSessions.dispose();
        }

        int userId = ((CentralActivity) context).getTbl_user().GetUserId();

        Single<List<VM_Meetings>> meetings = api_request.getConcilSessions(context, userId, workId, councilId);

        dispose_getCouncilSessions = meetings.subscribeWith(new DisposableSingleObserver<List<VM_Meetings>>() {
            @Override
            public void onSuccess(List<VM_Meetings> vm_meetings) {

                s_addRequest.onLoading(false);
                ArrayAdapter<VM_Meetings> adapter=new ArrayAdapter<>(context,R.layout.spinner_item,vm_meetings);
                s_addRequest.onGetCouncilSessionsId(adapter);
                s_addRequest.onFinish();
            }

            @Override
            public void onError(Throwable e) {
                s_addRequest.onHideAll();
                s_addRequest.onError(Error.GetErrorVolley(e.toString()));
            }
        });

    }

    //در اینجا لیست دیسپوزیبل ها را پاس می دهد تا درصورت بسته شده صفحه عملیات ما هم لغو شوند
    public CompositeDisposable GetDisposables() {
        CompositeDisposable composite = new CompositeDisposable();

        if (dispose_getWorkYears != null) {
            composite.add(dispose_getWorkYears);
        }

        if (dispose_getCouncils != null) {
            composite.add(dispose_getCouncils);
        }

        return composite;
    }

}
