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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.Adapters.ManegmentRequestAdapter;
import ir.tdaapp.mms.Model.Enums.BottomBarItem;
import ir.tdaapp.mms.Model.Services.S_ManegmentRequest;
import ir.tdaapp.mms.Model.Utilitys.BaseFragment;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.Model.ViewModels.VM_WorkYear;
import ir.tdaapp.mms.Presenter.P_ManegmentRequest;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class ManegmentRequestFragment extends BaseFragment implements S_ManegmentRequest {

    public final static String TAG = "ManegmentRequestFragment";

    P_ManegmentRequest p_manegmentRequest;
    Toolbar toolBar;
    Spinner cmb_WorkYear, cmb_Council, cmb_Session;
    RelativeLayout loading;
    RecyclerView recycler;
    ManegmentRequestAdapter requestAdapter;
    TextView btn_Error_Again, btn_NoInternet_Retry, btn_SlowInternet_Retry;
    ShimmerFrameLayout shimmer_error, shimmer_internet, shimmer_slow_internet;
    LinearLayout No_Internet, error, Slow_Internet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manegment_request_fragment, container, false);

        findItem(view);
        implement();
        setToolbar();

        p_manegmentRequest.start();

        return view;
    }

    void findItem(View view) {
        toolBar = view.findViewById(R.id.toolBar);
        cmb_WorkYear = view.findViewById(R.id.cmb_WorkYear);
        cmb_Council = view.findViewById(R.id.cmb_Council);
        cmb_Session = view.findViewById(R.id.cmb_Session);
        loading = view.findViewById(R.id.loading);
        recycler = view.findViewById(R.id.recycler);
        btn_Error_Again = view.findViewById(R.id.btn_Error_Again);
        btn_NoInternet_Retry = view.findViewById(R.id.btn_NoInternet_Retry);
        btn_SlowInternet_Retry = view.findViewById(R.id.btn_SlowInternet_Retry);
        shimmer_error = view.findViewById(R.id.shimmer_error);
        shimmer_internet = view.findViewById(R.id.shimmer_internet);
        shimmer_slow_internet = view.findViewById(R.id.shimmer_slow_internet);
        No_Internet = view.findViewById(R.id.No_Internet);
        error = view.findViewById(R.id.error);
        Slow_Internet = view.findViewById(R.id.Slow_Internet);
    }

    void implement() {
        p_manegmentRequest = new P_ManegmentRequest(this, getContext());

        //زمانی که کاربر یک از آیتم های اسپینر سال کاری را انتخاب کند براساس آی دی آن شوراهای آن برگشت داده می شود
        cmb_WorkYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int Id = ((VM_WorkYear) adapterView.getItemAtPosition(i)).getId();

                if (Id != 0) {
                    p_manegmentRequest.getCouncils(Id);
                } else {
                    p_manegmentRequest.setDefaultSpinnerData();
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
                    p_manegmentRequest.getCouncilSessions(workId, councilId);
                } else {
                    p_manegmentRequest.setDefaultValueSpinnerCouncilSession();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cmb_Session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int councilId = ((VM_Councils) adapterView.getItemAtPosition(i)).getId();
                int workId = ((VM_WorkYear) cmb_WorkYear.getSelectedItem()).getId();
                int councilSessionId = ((VM_Meetings) cmb_Session.getSelectedItem()).getId();

                if (councilId != 0 && workId != 0 && councilSessionId != 0) {
                    p_manegmentRequest.getRequests(workId, councilId, councilSessionId);
                } else {
                    requestAdapter = new ManegmentRequestAdapter(getContext());
                    recycler.setAdapter(requestAdapter);
                    recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setToolbar() {

        toolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolBar.setTitle(getContext().getResources().getString(R.string.ManegeRequests));
        ((CentralActivity) getActivity()).setSupportActionBar(toolBar);
        ((CentralActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        toolBar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        setHasOptionsMenu(true);
        ((CentralActivity) getActivity()).SetEnableDrawer(BottomBarItem.Request);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    //زمانی که عملیات گرفتن داده ها شروع شود برای اولین بار متد زیر فراخوانی می شود
    @Override
    public void OnStart() {
        requestAdapter = new ManegmentRequestAdapter(getContext());
        recycler.setAdapter(requestAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        shimmer_error.startShimmerAnimation();
        shimmer_internet.startShimmerAnimation();
        shimmer_slow_internet.startShimmerAnimation();
    }

    //در اینجا حالت لودینگ صفحه ست می شود
    @Override
    public void onLoading(boolean load) {
        if (load) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    //در اینجا تمام آیتم ها مخفی می شوند
    @Override
    public void onHideAll() {
        loading.setVisibility(View.GONE);
        cmb_WorkYear.setVisibility(View.GONE);
        cmb_Council.setVisibility(View.GONE);
        cmb_Session.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);
        No_Internet.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        Slow_Internet.setVisibility(View.GONE);
    }

    //زمانی که دریافت داده ها با خطا مواجه شود متد زیر فراخوانی می شود
    @Override
    public void onError(ResaultCode resaultCode) {
        switch (resaultCode) {

            case NetworkError:
                No_Internet.setVisibility(View.VISIBLE);
                break;
            case TimeoutError:
                Slow_Internet.setVisibility(View.VISIBLE);
            case ServerError:
            case ParseError:
            case Error:
                error.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onFinish() {
        shimmer_error.stopShimmerAnimation();
        shimmer_internet.stopShimmerAnimation();
        shimmer_slow_internet.stopShimmerAnimation();
    }

    //در اینجا مقادیر پیش فرض اسپینر ها ست می شود
    @Override
    public void onSetDefaultSpinnersData(ArrayAdapter<VM_Councils> councils, ArrayAdapter<VM_Meetings> meetings) {
        cmb_Council.setAdapter(councils);
        cmb_Session.setAdapter(meetings);
    }

    //در اینجا مقادیر پیش فرض اسپینر جلسات ست می شود
    @Override
    public void onSetDefaultSpinnerSessionData(ArrayAdapter<VM_Meetings> adapter) {
        cmb_Session.setAdapter(adapter);
    }

    //در اینجا مقادیر اسپینر سال کاری ست می شود
    @Override
    public void onGetSpinnerWorkYearData(ArrayAdapter<VM_WorkYear> adapter) {
        cmb_WorkYear.setVisibility(View.VISIBLE);
        cmb_WorkYear.setAdapter(adapter);
    }

    //در اینجا مقادیر اسپینر شوراها ست می شود
    @Override
    public void onGetSpinnerCouncilData(ArrayAdapter<VM_Councils> adapter) {
        cmb_Council.setVisibility(View.VISIBLE);
        cmb_Council.setAdapter(adapter);
    }

    //در اینجا مقادیر اسپینر جلسات ست می شود
    @Override
    public void onGetSpinnerCouncilSessionData(ArrayAdapter<VM_Meetings> adapter) {
        cmb_Session.setVisibility(View.VISIBLE);
        cmb_Session.setAdapter(adapter);
    }

    //در اینجا درخواست ها یکی یکی به رسایکلر اضافه می شوند
    @Override
    public void onRequestRecycler(VM_Requests v) {
        requestAdapter.Add(v);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        p_manegmentRequest.GetDisposables(TAG).dispose();
    }
}
