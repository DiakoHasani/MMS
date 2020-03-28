package ir.tdaapp.mms.View.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.Enums.BottomBarItem;
import ir.tdaapp.mms.Model.Services.S_AddRequest;
import ir.tdaapp.mms.Model.Utilitys.BaseFragment;
import ir.tdaapp.mms.Model.Utilitys.ETC;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_PostRequest;
import ir.tdaapp.mms.Model.ViewModels.VM_WorkYear;
import ir.tdaapp.mms.Presenter.P_AddRequest;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

import static android.app.Activity.RESULT_OK;

public class AddRequestFragment extends BaseFragment implements S_AddRequest, View.OnClickListener {

    public final static String TAG = "AddRequestFragment";

    Toolbar toolBar;
    P_AddRequest p_addRequest;
    Spinner cmb_WorkYear, cmb_Council, cmb_Session;
    CardView file;
    EditText txt_Text;
    ImageView voice, img_bigEditText;
    LinearLayout Slow_Internet, No_Internet, error;
    ShimmerFrameLayout shimmer_slow_internet, shimmer_internet, shimmer_error;
    TextView btn_SlowInternet_Retry, btn_NoInternet_Retry, btn_Error_Again, lbl_TitleFile;
    RelativeLayout layout_Text, btn_AddRequest, loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_request_fragment, container, false);

        findItem(view);
        setToolbar();
        implement();

        p_addRequest.start();

        return view;
    }

    void findItem(View view) {
        toolBar = view.findViewById(R.id.toolBar);
        loading = view.findViewById(R.id.loading);
        cmb_WorkYear = view.findViewById(R.id.cmb_WorkYear);
        cmb_Council = view.findViewById(R.id.cmb_Council);
        cmb_Session = view.findViewById(R.id.cmb_Session);
        file = view.findViewById(R.id.file);
        txt_Text = view.findViewById(R.id.txt_Text);
        voice = view.findViewById(R.id.voice);
        Slow_Internet = view.findViewById(R.id.Slow_Internet);
        shimmer_slow_internet = view.findViewById(R.id.shimmer_slow_internet);
        btn_SlowInternet_Retry = view.findViewById(R.id.btn_SlowInternet_Retry);
        No_Internet = view.findViewById(R.id.No_Internet);
        shimmer_internet = view.findViewById(R.id.shimmer_internet);
        btn_NoInternet_Retry = view.findViewById(R.id.btn_NoInternet_Retry);
        error = view.findViewById(R.id.error);
        shimmer_error = view.findViewById(R.id.shimmer_error);
        btn_Error_Again = view.findViewById(R.id.btn_Error_Again);
        lbl_TitleFile = view.findViewById(R.id.lbl_TitleFile);
        layout_Text = view.findViewById(R.id.layout_Text);
        img_bigEditText = view.findViewById(R.id.img_bigEditText);
        btn_AddRequest = view.findViewById(R.id.btn_AddRequest);
    }

    void setToolbar() {

        toolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolBar.setTitle(getContext().getResources().getString(R.string.AddRequest));
        ((CentralActivity) getActivity()).setSupportActionBar(toolBar);
        ((CentralActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        toolBar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        setHasOptionsMenu(true);
        ((CentralActivity) getActivity()).SetEnableDrawer(BottomBarItem.Request);
    }

    void implement() {

        p_addRequest = new P_AddRequest(getContext(), this);

        btn_SlowInternet_Retry.setOnClickListener(this);
        btn_NoInternet_Retry.setOnClickListener(this);
        btn_Error_Again.setOnClickListener(this);
        voice.setOnClickListener(this);
        file.setOnClickListener(this);
        img_bigEditText.setOnClickListener(this);
        btn_AddRequest.setOnClickListener(this);

        //زمانی که کاربر یک از آیتم های اسپینر سال کاری را انتخاب کند براساس آی دی آن شوراهای آن برگشت داده می شود
        cmb_WorkYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int Id = ((VM_WorkYear) adapterView.getItemAtPosition(i)).getId();

                if (Id != 0) {
                    p_addRequest.getCouncils(Id);
                } else {
                    p_addRequest.SetDefaultSpinnerData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //در اینجا زمانی که یکی از آیتم های اسپینر شورا انتخاب شود جلسات مربوط به آن در اسپینر جلسات ست می شود
        cmb_Council.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int councilId = ((VM_Councils) adapterView.getItemAtPosition(i)).getId();
                int workId = ((VM_WorkYear) cmb_WorkYear.getSelectedItem()).getId();

                if (councilId != 0 && workId != 0) {
                    p_addRequest.getCouncilSessions(workId, councilId);
                } else {
                    p_addRequest.setDefaultValueSpinnerCouncilSession();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //در اینجا صفحه ادیت تکست بزرگ که مربوط به متن درخواست است نمایش داده می شود
    void showBigEditText() {
        OnlyEditTextFragment e = new OnlyEditTextFragment(txt_Text.getText().toString());
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameRequest, e).commit();
        e.setS_Text_OnlyEditTextFragment(text -> txt_Text.setText(text));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    //در اینجا کارهای اولیه قبل از گرفتن داده ها انجام می شود
    @Override
    public void OnStart() {
        shimmer_slow_internet.startShimmerAnimation();
        shimmer_internet.startShimmerAnimation();
        shimmer_error.startShimmerAnimation();
        cmb_WorkYear.setAdapter(null);
        cmb_Session.setAdapter(null);
        cmb_Council.setAdapter(null);
    }

    //در اینجا تمامی آیتم ها مخفی می شوند
    @Override
    public void onHideAll() {
        cmb_Council.setVisibility(View.GONE);
        cmb_Session.setVisibility(View.GONE);
        cmb_WorkYear.setVisibility(View.GONE);
        file.setVisibility(View.GONE);
        layout_Text.setVisibility(View.GONE);
        voice.setVisibility(View.GONE);
        Slow_Internet.setVisibility(View.GONE);
        No_Internet.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    //در اینجا لودینگ نمایش یا مخفی می شود
    @Override
    public void onLoading(boolean show) {
        if (show) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    //در اینجا داده های اسپینر سال کاری گرفته می شود
    @Override
    public void onGetWorkYears(ArrayAdapter<VM_WorkYear> adapter) {
        cmb_WorkYear.setAdapter(adapter);
    }

    //اگر در طول عملیات خطای رخ دهد متد زیر فراخوانی می شود
    @Override
    public void onError(ResaultCode resaultCode) {
        switch (resaultCode) {
            case NetworkError:
                No_Internet.setVisibility(View.VISIBLE);
                break;
            case TimeoutError:
                Slow_Internet.setVisibility(View.VISIBLE);
                break;
            case ServerError:
            case ParseError:
            case Error:
                error.setVisibility(View.VISIBLE);
                break;
        }
    }

    //در اینجا آیتم های صفحه نمایش داده می شوند
    @Override
    public void onShowAll() {
        cmb_Council.setVisibility(View.VISIBLE);
        cmb_Session.setVisibility(View.VISIBLE);
        cmb_WorkYear.setVisibility(View.VISIBLE);
        file.setVisibility(View.VISIBLE);
        layout_Text.setVisibility(View.VISIBLE);
        voice.setVisibility(View.VISIBLE);
    }

    //در اینجا به اسپینرها مقدار پیش فرض داده می شود که بیشتر برای زیبایی می باشد
    @Override
    public void onSetDefaultSpinnersData(ArrayAdapter<VM_Councils> councils, ArrayAdapter<VM_Meetings> meetings) {
        cmb_Council.setAdapter(councils);
        cmb_Session.setAdapter(meetings);
    }

    //در اینجا مقدار پیش فرض اسپینر جلسات ست می شود
    @Override
    public void onSetDefaultSpinnerSessionData(ArrayAdapter<VM_Meetings> adapter) {
        cmb_Session.setAdapter(adapter);
    }

    //در اینجا مقادیر اسپینر شوراها ست می شود
    @Override
    public void onGetCouncils(ArrayAdapter<VM_Councils> adapter) {
        cmb_Council.setAdapter(adapter);
    }

    // در اینجا مقدار اسپینر جلسات ست می شود
    @Override
    public void onGetCouncilSessionsId(ArrayAdapter<VM_Meetings> adapter) {
        cmb_Session.setAdapter(adapter);
    }

    //زمانی که کاربر یک فایل انتخاب کند آدرس آن در اینجا گرفته می شود
    @Override
    public void onGetFilePath(String path) {

        if (!path.equalsIgnoreCase("")){
            lbl_TitleFile.setText(path);
            lbl_TitleFile.setTextDirection(View.TEXT_DIRECTION_LTR);
            lbl_TitleFile.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

    }

    //اگر تمام مقادیر ولید باشند متد زیر فراخوانی می شود تا فرآیند ارسال داده ها شروع شود
    @Override
    public void onValid() {
        p_addRequest.addFileRequest(lbl_TitleFile.getText().toString());
    }

    //اگر مقادیر ولید نباشند متد زیر فراخوانی می شود
    @Override
    public void onNotValid() {
        btn_AddRequest.setEnabled(true);
        Toast.makeText(getContext(), getContext().getResources().getString(R.string.Please_be_careful_in_entering_values), Toast.LENGTH_SHORT).show();
    }

    ///زمانی که آپلود فایل با خطا مواجه شود متد زیر فراخوانی می شود
    @Override
    public void onErrorFile() {
        btn_AddRequest.setEnabled(true);
        Toast.makeText(getContext(), getResources().getString(R.string.Your_file_has_encountered_an_error), Toast.LENGTH_SHORT).show();
    }

    //زمانی که فایل با موفقیت آپلود شود این متد برای ادامه عملیات فراخوانی می شود
    @Override
    public void onSuccessPostFile(String fileName) {

        VM_PostRequest request = new VM_PostRequest();

        try {

            int userId = ((CentralActivity) getActivity()).getTbl_user().GetUserId();
            int roleId = ((CentralActivity) getActivity()).getTbl_role().GetRoleId();

            request.setWorkYearId(((VM_WorkYear) cmb_WorkYear.getSelectedItem()).getId());
            request.setCouncilId(((VM_Councils) cmb_Council.getSelectedItem()).getId());
            request.setSessionId(((VM_Meetings) cmb_Session.getSelectedItem()).getId());
            request.setAttachmentFile(fileName);
            request.setRequestText(txt_Text.getText().toString());
            request.setRoleId(roleId);
            request.setUserId(userId);

        } catch (Exception e) {
        }

        p_addRequest.addRequest(request);
    }

    //زمان افزودن درخواست اگر خطای رخ دهد متد زیر فراخوانی می شود
    @Override
    public void onErrorPostRequest(ResaultCode resault) {

        btn_AddRequest.setEnabled(true);

        switch (resault) {
            case NetworkError:
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.Please_Check_Your_Internet), Toast.LENGTH_SHORT).show();
                break;
            case TimeoutError:
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.Your_Internet_Speed_Is_Very_Slow), Toast.LENGTH_SHORT).show();
                break;
            case ServerError:
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.An_Error_Has_Occurred_On_The_Server), Toast.LENGTH_SHORT).show();
                break;
            case ParseError:
            case Error:
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.There_Was_an_Error_In_The_Application), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //زمانی که درخواست با موفقیت ثبت شود متد زیر فراخوانی می شود
    @Override
    public void onSuccessPostRequest(String message) {
        btn_AddRequest.setEnabled(true);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    //زمانی که تمام داده ها از سرور گرفته شوند و کار ما به پایان برسد متد زیر فراخوانی می شود
    @Override
    public void onFinish() {
        shimmer_slow_internet.stopShimmerAnimation();
        shimmer_internet.stopShimmerAnimation();
        shimmer_error.stopShimmerAnimation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        p_addRequest.GetDisposables(TAG).dispose();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_SlowInternet_Retry:
            case R.id.btn_NoInternet_Retry:
            case R.id.btn_Error_Again:
                p_addRequest.start();
                break;
            case R.id.voice:
                try {
                    startActivityForResult(p_addRequest.promptSpeechInput(), 1);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getContext(),
                            getContext().getResources().getString(R.string.speech_not_supported),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.file:
                p_addRequest.choseFile();
                break;
            case R.id.img_bigEditText:
                showBigEditText();
                break;
            case R.id.btn_AddRequest:
                btn_AddRequest.setEnabled(false);
                p_addRequest.CheckValidation(cmb_WorkYear, cmb_Council, cmb_Session, txt_Text);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {

            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            txt_Text.setText(ETC.ReplaceSpechToText(result.get(0)));
        }
    }
}
