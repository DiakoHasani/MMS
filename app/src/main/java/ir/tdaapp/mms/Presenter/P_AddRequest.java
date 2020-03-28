package ir.tdaapp.mms.Presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ir.tdaapp.li_utility.Codes.Validation;
import ir.tdaapp.mms.Model.Repositorys.Server.Api_Request;
import ir.tdaapp.mms.Model.Services.S_AddRequest;
import ir.tdaapp.mms.Model.Utilitys.Error;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Message;
import ir.tdaapp.mms.Model.ViewModels.VM_PostRequest;
import ir.tdaapp.mms.Model.ViewModels.VM_WorkYear;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class P_AddRequest {

    Context context;
    S_AddRequest s_addRequest;
    Api_Request api_request;
    Disposable dispose_getWorkYears, dispose_getCouncils, dispose_getCouncilSessions, dispose_addFileRequest;
    Disposable dispose_addRequest;

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
    public void setDefaultValueSpinnerCouncilSession() {

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
                ArrayAdapter<VM_Meetings> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, vm_meetings);
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

    //مربوط به تبدیل گفتار به نوشتار
    public Intent promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                context.getResources().getString(R.string.speech_prompt));

        return intent;
    }

    public void choseFile() {

        Dexter.withActivity(((CentralActivity) context)).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(
                new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        new ChooserDialog((CentralActivity) context)
                                .withFilter(false, false, "pdf", "doc", "docx", "jpg", "png")
                                .withResources(R.string.title_choose_file, R.string.Select, R.string.Cancel)
                                .withChosenListener((s, file) -> {
                                    s_addRequest.onGetFilePath(s);
                                })
                                .build().show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }
        ).check();

    }

    public void CheckValidation(Spinner cmb_WorkYear, Spinner cmb_Council, Spinner cmb_Session, EditText txt_Text) {

        boolean isValid = true;

        if (((VM_WorkYear) cmb_WorkYear.getSelectedItem()).getId() == 0) {
            isValid = false;
        }

        if (((VM_Councils) cmb_Council.getSelectedItem()).getId() == 0) {
            isValid = false;
        }

        if (((VM_Meetings) cmb_Session.getSelectedItem()).getId() == 0) {
            isValid = false;
        }

        if (!Validation.Required(txt_Text, context.getResources().getString(R.string.This_value_must_be_filled))) {
            isValid = false;
        }

        if (isValid) {
            s_addRequest.onValid();
        } else {
            s_addRequest.onNotValid();
        }
    }

    //در اینجا فایل در سرور آپلود می شود و بعد از آن عملیات درج کردن داده ها در سرور شروع می شود
    public void addFileRequest(String path) {

        s_addRequest.onLoading(true);

        Single<String> d = api_request.postFile(path);
        dispose_addFileRequest = d.subscribeWith(new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String s) {
                s_addRequest.onSuccessPostFile(s.replace("\"", ""));
            }

            @Override
            public void onError(Throwable e) {
                s_addRequest.onLoading(false);
                s_addRequest.onErrorFile();
            }
        });

    }

    public void addRequest(VM_PostRequest request) {

        Single<VM_Message> message = api_request.postRequest(request);

        dispose_addRequest = message.subscribeWith(new DisposableSingleObserver<VM_Message>() {
            @Override
            public void onSuccess(VM_Message message) {
                s_addRequest.onLoading(false);
                s_addRequest.onSuccessPostRequest(message.getMessageText());
            }

            @Override
            public void onError(Throwable e) {
                s_addRequest.onLoading(false);
                s_addRequest.onErrorPostRequest(Error.GetErrorVolley(e.toString()));
            }
        });

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

        if (dispose_addFileRequest != null) {
            composite.add(dispose_addFileRequest);
        }

        if (dispose_addRequest != null) {
            composite.add(dispose_addRequest);
        }

        api_request.Cancel(TAG, context);

        return composite;
    }

}
