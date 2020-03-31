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
import ir.tdaapp.mms.Model.Adapters.ApprovalAdapter;
import ir.tdaapp.mms.Model.Services.S_Approval;
import ir.tdaapp.mms.Model.Utilitys.BaseFragment;
import ir.tdaapp.mms.Model.ViewModels.VM_Approvals;
import ir.tdaapp.mms.Presenter.P_Approval;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

//صفحه مربوط به صورت جلسات یا مصوبات
public class ApprovalsFragment extends BaseFragment implements S_Approval, View.OnClickListener {

    public static final String TAG = "ApprovalsFragment";
    Toolbar toolBar;
    private ActionBarDrawerToggle toggle;
    RecyclerView Recycler;
    LinearLayout No_Internet, error, Slow_Internet;
    ShimmerFrameLayout shimmer_internet, shimmer_error, Loading, shimmer_slow_internet;
    TextView btn_NoInternet_Retry, btn_Error_Again, btn_SlowInternet_Retry;
    LinearLayoutManager layoutManager;
    P_Approval p_approval;
    ApprovalAdapter approvalAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.approvals_fragment, container, false);

        FindItem(view);
        Implements();
        SetToolBar();
        p_approval.Start();

        return view;
    }

    void FindItem(View view) {
        toolBar = view.findViewById(R.id.toolBar);
        Recycler = view.findViewById(R.id.Recycler);
        No_Internet = view.findViewById(R.id.No_Internet);
        error = view.findViewById(R.id.error);
        shimmer_internet = view.findViewById(R.id.shimmer_internet);
        shimmer_error = view.findViewById(R.id.shimmer_error);
        Loading = view.findViewById(R.id.Loading);
        btn_NoInternet_Retry = view.findViewById(R.id.btn_NoInternet_Retry);
        btn_Error_Again = view.findViewById(R.id.btn_Error_Again);
        Slow_Internet = view.findViewById(R.id.Slow_Internet);
        shimmer_slow_internet = view.findViewById(R.id.shimmer_slow_internet);
        btn_SlowInternet_Retry = view.findViewById(R.id.btn_SlowInternet_Retry);
    }

    void Implements() {
        toggle = new ActionBarDrawerToggle(getActivity(), ((CentralActivity) getActivity()).getDrawer(), R.string.Open, R.string.Close);
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        btn_NoInternet_Retry.setOnClickListener(this);
        btn_Error_Again.setOnClickListener(this);
        btn_SlowInternet_Retry.setOnClickListener(this);
        p_approval = new P_Approval(getContext(), this);
    }

    //در اینجا عملیات مربوط به تولبار انجام می شود
    private void SetToolBar() {
        toolBar.setTitle(getContext().getResources().getString(R.string.Meeting_of_the_board));
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
        inflater.inflate(R.menu.approval_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.toolBar_Approval_ChangeRoll:
                ((CentralActivity) getActivity()).ShowRoleDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //در اینجا عملیات اولیه مثل شروع به گرفتن داده ها شود و در اینجا آداپتر ها و بخش های دیگر دوباره ساخته می شوند
    @Override
    public void OnStart() {
        shimmer_internet.startShimmerAnimation();
        shimmer_error.startShimmerAnimation();
        shimmer_slow_internet.startShimmerAnimation();
        Loading.startShimmerAnimation();

        approvalAdapter = new ApprovalAdapter(getContext());
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        Recycler.setLayoutManager(layoutManager);
        Recycler.setAdapter(approvalAdapter);
    }

    //در اینجا لودینگ برنامه ست می شود
    @Override
    public void onLoading(boolean isLoading) {
        if (isLoading) {
            Loading.setVisibility(View.VISIBLE);
        } else {
            Loading.setVisibility(View.GONE);
        }
    }

    //زمانی که دریافت داده ها با خطا مواجه شوند متد زیر فراخوانی می شود
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

    //زمانی که داده ها با موفقیت دریافت شوند متد زیر فراخوانی می شود
    @Override
    public void onSuccess() {
        Recycler.setVisibility(View.VISIBLE);
    }

    //زمانی که عملیات به پایان برسد متد زیر فراخوانی می شود
    @Override
    public void onFinish() {
        shimmer_internet.stopShimmerAnimation();
        shimmer_error.stopShimmerAnimation();
        Loading.stopShimmerAnimation();
        shimmer_slow_internet.stopShimmerAnimation();
    }

    //در اینجا آیتم ها در آداپتر اضافه می شوند
    @Override
    public void onSetItem(VM_Approvals approval) {
        approvalAdapter.Add(approval);
    }

    //در اینجا تمامی آیتم ها مخفی می شوند
    @Override
    public void onHideAll() {
        No_Internet.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        Loading.setVisibility(View.GONE);
        Recycler.setVisibility(View.GONE);
        Slow_Internet.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Error_Again:
            case R.id.btn_NoInternet_Retry:
            case R.id.btn_SlowInternet_Retry:
                p_approval.Start();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        p_approval.GetDisposables().dispose();
    }
}
