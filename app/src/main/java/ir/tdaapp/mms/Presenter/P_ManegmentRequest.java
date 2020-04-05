package ir.tdaapp.mms.Presenter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_Request;
import ir.tdaapp.mms.Model.Services.S_ManegmentRequest;
import ir.tdaapp.mms.Model.Utilitys.Error;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_FilterRequest;
import ir.tdaapp.mms.Model.ViewModels.VM_FilterRequestsSecretary;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.Model.ViewModels.VM_WorkYear;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class P_ManegmentRequest {

    S_ManegmentRequest s_manegmentRequest;
    Context context;
    Api_Request api_request;
    Disposable dispose_getWorkYears, dispose_getCouncils, dispose_getCouncilSessions, dispose_getRequests;
    Disposable dispose_setItemRecycler;

    public P_ManegmentRequest(S_ManegmentRequest s_manegmentRequest, Context context) {
        this.s_manegmentRequest = s_manegmentRequest;
        this.context = context;
        api_request = new Api_Request();
    }

    public void start() {
        s_manegmentRequest.onHideAll();
        s_manegmentRequest.onLoading(true);

        getWorkYears();
    }

    //در اینجا سال کاری گرفته می شود
    void getWorkYears() {

        if (dispose_getWorkYears != null) {
            dispose_getWorkYears.dispose();
        }

        int userId = ((CentralActivity) context).getTbl_user().GetUserId();

        Single<List<VM_WorkYear>> data = api_request.getWorkYearsSecretary(userId, context);

        dispose_getWorkYears = data.subscribeWith(new DisposableSingleObserver<List<VM_WorkYear>>() {
            @Override
            public void onSuccess(List<VM_WorkYear> workYears) {
                s_manegmentRequest.onLoading(false);
                setSpinnerWorkYearData(workYears);
                setDefaultSpinnerData();
            }

            @Override
            public void onError(Throwable e) {
                s_manegmentRequest.onHideAll();
                s_manegmentRequest.onError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا لیست شوراها گرفته می شود
    public void getCouncils(int workId) {

        s_manegmentRequest.onLoading(true);

        if (dispose_getCouncils != null) {
            dispose_getCouncils.dispose();
        }

        int userId = ((CentralActivity) context).getTbl_user().GetUserId();

        Single<List<VM_Councils>> data = api_request.getCouncilsSecretary(context, userId, workId);

        dispose_getCouncils = data.subscribeWith(new DisposableSingleObserver<List<VM_Councils>>() {
            @Override
            public void onSuccess(List<VM_Councils> vm_councils) {
                s_manegmentRequest.onLoading(false);
                setSpinnerCouncilData(vm_councils);
                setDefaultValueSpinnerCouncilSession();
            }

            @Override
            public void onError(Throwable e) {
                s_manegmentRequest.onHideAll();
                s_manegmentRequest.onError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا لیست جلسات گرفته می شود
    public void getCouncilSessions(int workYearId, int councilId) {

        s_manegmentRequest.onLoading(true);

        if (dispose_getCouncilSessions != null) {
            dispose_getCouncilSessions.dispose();
        }

        int userId = ((CentralActivity) context).getTbl_user().GetUserId();

        Single<List<VM_Meetings>> data = api_request.getCouncilSessionsSecretary(context, userId, workYearId, councilId);

        dispose_getCouncilSessions = data.subscribeWith(new DisposableSingleObserver<List<VM_Meetings>>() {
            @Override
            public void onSuccess(List<VM_Meetings> vm_meetings) {
                s_manegmentRequest.onLoading(false);
                setSpinnerCouncilSessionData(vm_meetings);
            }

            @Override
            public void onError(Throwable e) {
                s_manegmentRequest.onHideAll();
                s_manegmentRequest.onError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا به اسپینرها مقدار پیش فرض داده می شود که بیشتر برای زیبایی می باشد
    public void setDefaultSpinnerData() {

        List<VM_Councils> councils = new ArrayList<>();
        List<VM_Meetings> meetings = new ArrayList<>();

        councils.add(new VM_Councils(0, context.getResources().getString(R.string.SelectedOneYearOfWork)));
        meetings.add(new VM_Meetings(0, context.getResources().getString(R.string.SelectedOneCouncil)));

        ArrayAdapter<VM_Councils> councilsAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, councils);
        ArrayAdapter<VM_Meetings> meetingsAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, meetings);

        s_manegmentRequest.onSetDefaultSpinnersData(councilsAdapter, meetingsAdapter);
    }

    //در اینجا عملیات گرفتن درخواست ها شروع می شود
    public void getRequests(int WorkId, int CouncilId, int CouncilSessionId) {

        int userId = ((CentralActivity) context).getTbl_user().GetUserId();

        VM_FilterRequestsSecretary filter = new VM_FilterRequestsSecretary();
        filter.setUserId(userId);
        filter.setCouncilId(CouncilId);
        filter.setCouncilSessionId(CouncilSessionId);
        filter.setWorkYearId(WorkId);
        filter.setLoadPreviousRequestThatAreHavingCheckingStatus(false);

        Single<List<VM_Requests>> data = api_request.getRequestsSecretary(filter);

        dispose_getRequests = data.subscribeWith(new DisposableSingleObserver<List<VM_Requests>>() {
            @Override
            public void onSuccess(List<VM_Requests> vm_requests) {
                setItemRecycler(vm_requests);
            }

            @Override
            public void onError(Throwable e) {
                s_manegmentRequest.onHideAll();
                s_manegmentRequest.onError(Error.GetErrorVolley(e.toString()));
            }
        });
    }

    //در اینجا درخواست ها یکی یکی به رسایکلر اضافه می شوند
    void setItemRecycler(List<VM_Requests> vm_requests) {

        Observable<VM_Requests> list = Observable.fromIterable(vm_requests);

        dispose_setItemRecycler = list.subscribe(v -> {
                    s_manegmentRequest.onRequestRecycler(v);
                }
                , throwable -> {
                },
                () -> {
                    s_manegmentRequest.onFinish();
                });
    }

    //در اینجا داده پیش فرض اسپینر جلسات ست می شود
    public void setDefaultValueSpinnerCouncilSession() {

        List<VM_Meetings> meetings = new ArrayList<>();
        meetings.add(new VM_Meetings(0, context.getResources().getString(R.string.SelectedOneCouncil)));
        ArrayAdapter<VM_Meetings> meetingsAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, meetings);

        s_manegmentRequest.onSetDefaultSpinnerSessionData(meetingsAdapter);
    }

    //در اینجا داده های اسپینر سال کاری ست می شود
    void setSpinnerWorkYearData(List<VM_WorkYear> workYears) {
        ArrayAdapter<VM_WorkYear> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, workYears);
        s_manegmentRequest.onGetSpinnerWorkYearData(adapter);
    }

    //در اینجا داده های اسپینر شورا ست می شود
    void setSpinnerCouncilData(List<VM_Councils> councils) {
        ArrayAdapter<VM_Councils> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, councils);
        s_manegmentRequest.onGetSpinnerCouncilData(adapter);
    }

    //در اینجا مقادیر اسپینر جلسات ست می شود
    void setSpinnerCouncilSessionData(List<VM_Meetings> meetings) {
        ArrayAdapter<VM_Meetings> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, meetings);
        s_manegmentRequest.onGetSpinnerCouncilSessionData(adapter);
    }

    //در اینجا لیست دیسپوزیبل ها را پاس می دهد تا درصورت بسته شده صفحه عملیات ما هم لغو شوند
    public CompositeDisposable GetDisposables(String TAG) {
        CompositeDisposable composite = new CompositeDisposable();

        if (dispose_getWorkYears != null) {
            composite.add(dispose_getWorkYears);
        }

        if (dispose_getCouncils != null) {
            composite.add(dispose_getCouncils);
        }

        if (dispose_getCouncilSessions != null) {
            composite.add(dispose_getCouncilSessions);
        }

        if (dispose_getRequests != null) {
            composite.add(dispose_getRequests);
        }

        if (dispose_setItemRecycler != null) {
            composite.add(dispose_setItemRecycler);
        }
        api_request.Cancel(TAG, context);

        return composite;
    }
}
