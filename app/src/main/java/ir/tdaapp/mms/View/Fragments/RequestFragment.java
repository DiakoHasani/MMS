package ir.tdaapp.mms.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.Adapters.RequestAdapter;
import ir.tdaapp.mms.Model.Services.S_Request;
import ir.tdaapp.mms.Model.Utilitys.BaseFragment;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.Presenter.P_Request;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

public class RequestFragment extends BaseFragment implements S_Request, View.OnClickListener {

    public static final String TAG = "RequestFragment";
    Toolbar toolBar;
    private ActionBarDrawerToggle toggle;
    RecyclerView Recycler;
    P_Request p_request;
    RequestAdapter requestAdapter;
    LinearLayoutManager layoutManager;
    LinearLayout No_Internet, error,Slow_Internet;
    ShimmerFrameLayout shimmer_error, shimmer_internet, Loading,shimmer_slow_internet;
    TextView btn_Error_Again, btn_NoInternet_Retry,btn_SlowInternet_Retry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_fragment, container, false);

        FindItem(view);
        Implements();
        SetToolBar();

        //در اینجا گرفتن داده ها شروع می شود
        p_request.Start();

        return view;
    }

    void Implements() {
        toggle = new ActionBarDrawerToggle(getActivity(), ((CentralActivity) getActivity()).getDrawer(), R.string.Open, R.string.Close);
        p_request = new P_Request(this, getContext());
        btn_Error_Again.setOnClickListener(this);
        btn_NoInternet_Retry.setOnClickListener(this);
        btn_SlowInternet_Retry.setOnClickListener(this);
    }

    void FindItem(View view) {
        toolBar = view.findViewById(R.id.toolBar);
        Recycler = view.findViewById(R.id.Recycler);
        No_Internet = view.findViewById(R.id.No_Internet);
        error = view.findViewById(R.id.error);
        shimmer_error = view.findViewById(R.id.shimmer_error);
        shimmer_internet = view.findViewById(R.id.shimmer_internet);
        btn_Error_Again = view.findViewById(R.id.btn_Error_Again);
        btn_NoInternet_Retry = view.findViewById(R.id.btn_NoInternet_Retry);
        Loading = view.findViewById(R.id.Loading);
        Slow_Internet = view.findViewById(R.id.Slow_Internet);
        shimmer_slow_internet = view.findViewById(R.id.shimmer_slow_internet);
        btn_SlowInternet_Retry = view.findViewById(R.id.btn_SlowInternet_Retry);
    }

    //در اینجا عملیات مربوط به تولبار انجام می شود
    private void SetToolBar() {
        toolBar.setTitle(getContext().getResources().getString(R.string.Request));
        toolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        ((CentralActivity) getActivity()).setSupportActionBar(toolBar);
        ((CentralActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorWhite));
        ((CentralActivity) getActivity()).getDrawer().addDrawerListener(toggle);
        toggle.syncState();

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.central_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //در اینجا عملیات اولیه انجام می شود مثل نیو کردن آداپترها و عملیات دیگر
    @Override
    public void Start() {
        requestAdapter = new RequestAdapter(getContext());
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        Recycler.setLayoutManager(layoutManager);
        Recycler.setAdapter(requestAdapter);

        shimmer_error.startShimmerAnimation();
        shimmer_internet.startShimmerAnimation();
        shimmer_slow_internet.startShimmerAnimation();
    }

    //اگر در برگشت داده ها خطای رخ دهد متد زیر فراخوانی می شود
    @Override
    public void OnError(ResaultCode resault) {
        switch (resault) {
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

    //در اینجا حالت لودینگ ست می شود که یک مقدار می گیرد که آیا برنامه در حالت لودینگ قرار گیرد
    @Override
    public void Loading(boolean isLoading) {
        if (isLoading){
            Loading.startShimmerAnimation();
            Loading.setVisibility(View.VISIBLE);
        }else{
            Loading.stopShimmerAnimation();
            Loading.setVisibility(View.GONE);
        }
    }

    //در اینجا آیتم درخواست ها ست می شود
    @Override
    public void RequestItem(VM_Requests request) {
        requestAdapter.Add(request);
    }

    //اینجا زمانی که عملیات به اتمام برسد متد زیر فراخوانی می شود
    @Override
    public void Finish() {
        shimmer_error.stopShimmerAnimation();
        shimmer_internet.stopShimmerAnimation();
        Loading.stopShimmerAnimation();
        shimmer_slow_internet.stopShimmerAnimation();
    }

    //در اینجا تمامی آیتم ها مخفی می شوند
    @Override
    public void HideAll() {
        Recycler.setVisibility(View.GONE);
        No_Internet.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        Loading.setVisibility(View.GONE);
        Slow_Internet.setVisibility(View.GONE);
    }

    //در اینجا زمانی که داده ها با موفقیت دریافت شوند متد زیر برای نمایش رسایکلر فراخوانی می شود
    @Override
    public void OnSuccess() {
        Recycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Error_Again:
            case R.id.btn_NoInternet_Retry:
            case R.id.btn_SlowInternet_Retry:
                p_request.Start();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        p_request.GetDisposables().dispose();
    }
}
