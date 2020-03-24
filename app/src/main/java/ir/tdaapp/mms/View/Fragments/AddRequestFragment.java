package ir.tdaapp.mms.View.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.Enums.BottomBarItem;
import ir.tdaapp.mms.Model.Services.S_AddRequest;
import ir.tdaapp.mms.Model.Utilitys.BaseFragment;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_WorkYear;
import ir.tdaapp.mms.Presenter.P_AddRequest;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class AddRequestFragment extends BaseFragment implements S_AddRequest, View.OnClickListener {

    Toolbar toolBar;
    P_AddRequest p_addRequest;
    Spinner cmb_WorkYear, cmb_Council, cmb_Session;
    CardView file;
    EditText txt_Text;
    ProgressBar loading;
    ImageView voice;
    LinearLayout Slow_Internet, No_Internet, error;
    ShimmerFrameLayout shimmer_slow_internet, shimmer_internet, shimmer_error;
    TextView btn_SlowInternet_Retry, btn_NoInternet_Retry, btn_Error_Again;

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
        txt_Text.setVisibility(View.GONE);
        voice.setVisibility(View.GONE);
        Slow_Internet.setVisibility(View.GONE);
        No_Internet.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        loading.setVisibility(View.INVISIBLE);
    }

    //در اینجا لودینگ نمایش یا مخفی می شود
    @Override
    public void onLoading(boolean show) {
        if (show) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.INVISIBLE);
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
        txt_Text.setVisibility(View.VISIBLE);
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
        p_addRequest.GetDisposables().dispose();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_SlowInternet_Retry:
            case R.id.btn_NoInternet_Retry:
            case R.id.btn_Error_Again:
                p_addRequest.start();
                break;
        }

    }
}
