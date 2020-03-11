package ir.tdaapp.mms.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.shimmer.ShimmerFrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;
import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.Adapters.ApprovalAdapter;
import ir.tdaapp.mms.Model.Adapters.MeetingAdapter;
import ir.tdaapp.mms.Model.Adapters.RequestAdapter;
import ir.tdaapp.mms.Model.Services.S_Home;
import ir.tdaapp.mms.Model.Utilitys.BaseFragment;
import ir.tdaapp.mms.Model.ViewModels.VM_Approvals;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.Presenter.P_Home;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;
import pl.droidsonroids.gif.GifImageView;

public class HomeFragment extends BaseFragment implements S_Home, View.OnClickListener {

    public static final String TAG = "HomeFragment";
    ShimmerFrameLayout shimmer_error, shimmer_internet, shimmer_slow_internet;
    LinearLayout main, No_Internet, error, Slow_Internet;
    P_Home p_home;
    GifImageView Loading;

    MeetingAdapter meetingAdapter;
    RequestAdapter requestAdapter;
    ApprovalAdapter approvalAdapter;

    RecyclerView Recycler_Meeting, Recycler_Request, Recycler_Approval;
    LinearLayoutManager layoutManager_Meeting, layoutManager_Request, layoutManager_Approval;

    RelativeLayout btn_GetAll_Meeting,btn_GetAll_Request,btn_GetAll_Approval;
    TextView btn_NoInternet_Retry, btn_Error_Again,btn_SlowInternet_Retry;
    Toolbar toolBar;
    private ActionBarDrawerToggle toggle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        FindItem(view);
        Implements();
        SetToolBar();

        //در اینجا عملیات خواندن داده ها شروع می شود
        p_home.Start();

        return view;
    }

    void FindItem(View view) {
        shimmer_error = view.findViewById(R.id.shimmer_error);
        shimmer_internet = view.findViewById(R.id.shimmer_internet);
        main = view.findViewById(R.id.main);
        No_Internet = view.findViewById(R.id.No_Internet);
        error = view.findViewById(R.id.error);
        Loading = view.findViewById(R.id.Loading);
        Recycler_Meeting = view.findViewById(R.id.Recycler_Meeting);
        Recycler_Request = view.findViewById(R.id.Recycler_Request);
        Recycler_Approval = view.findViewById(R.id.Recycler_Approval);
        btn_NoInternet_Retry = view.findViewById(R.id.btn_NoInternet_Retry);
        btn_Error_Again = view.findViewById(R.id.btn_Error_Again);
        btn_GetAll_Approval = view.findViewById(R.id.btn_GetAll_Approval);
        btn_GetAll_Request = view.findViewById(R.id.btn_GetAll_Request);
        btn_GetAll_Meeting = view.findViewById(R.id.btn_GetAll_Meeting);
        toolBar = view.findViewById(R.id.toolBar);
        btn_SlowInternet_Retry = view.findViewById(R.id.btn_SlowInternet_Retry);
        Slow_Internet = view.findViewById(R.id.Slow_Internet);
        shimmer_slow_internet = view.findViewById(R.id.shimmer_slow_internet);
    }

    void Implements() {
        p_home = new P_Home(getContext(), this);
        layoutManager_Meeting = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        layoutManager_Request = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        layoutManager_Approval = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        btn_NoInternet_Retry.setOnClickListener(this);
        btn_Error_Again.setOnClickListener(this);
        btn_GetAll_Approval.setOnClickListener(this);
        btn_GetAll_Request.setOnClickListener(this);
        btn_GetAll_Meeting.setOnClickListener(this);
        btn_SlowInternet_Retry.setOnClickListener(this);

        toggle = new ActionBarDrawerToggle(getActivity(), ((CentralActivity) getActivity()).getDrawer(), R.string.Open, R.string.Close);
    }

    //در اینجا عملیات مربوط به تولبار انجام می شود
    private void SetToolBar() {
        toolBar.setTitle(getContext().getResources().getString(R.string.Home));
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()){
            case R.id.toolBar_ChangeRoll:
                ((CentralActivity)getActivity()).ShowRoleDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //زمانی که خطای رخ دهد متد زیر فراخوانی می شود
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

    //زمانی که برنامه در حالت لودینگ در آید متد زیر فراخوانی می شود
    @Override
    public void Loading() {
        Loading.setVisibility(View.VISIBLE);
    }

    //زمانی که تمام آیتم ها مثل لودینگ یا خطا یا رسایکلر ها مخفی می شوند
    @Override
    public void HideAll() {
        main.setVisibility(View.INVISIBLE);
        No_Internet.setVisibility(View.INVISIBLE);
        error.setVisibility(View.INVISIBLE);
        Loading.setVisibility(View.INVISIBLE);
        Slow_Internet.setVisibility(View.INVISIBLE);
    }

    //در اینجا آیتم های جلسات ست می شود
    @Override
    public void MeetingItem(VM_Meetings meeting) {
        meetingAdapter.Add(meeting);
    }

    //در اینجا آیتم های درخواست ها ست می شود
    @Override
    public void RequestItem(VM_Requests request) {
        requestAdapter.Add(request);
    }

    //در اینجا آیتم های صورت جلسات ست می شود
    @Override
    public void ApprovalItem(VM_Approvals approval) {
        approvalAdapter.Add(approval);
    }

    //در اینجا شی های مثل آداپتر را نیو می کنیم این متد زمانی که برنامه از صفر شروع شود فراخوانی می شود
    @Override
    public void Start() {
        meetingAdapter = new MeetingAdapter(getContext());
        requestAdapter = new RequestAdapter(getContext());
        approvalAdapter = new ApprovalAdapter(getContext());

        Recycler_Meeting.setAdapter(meetingAdapter);
        Recycler_Request.setAdapter(requestAdapter);
        Recycler_Approval.setAdapter(approvalAdapter);

        Recycler_Approval.setLayoutManager(layoutManager_Approval);
        Recycler_Request.setLayoutManager(layoutManager_Request);
        Recycler_Meeting.setLayoutManager(layoutManager_Meeting);

        shimmer_error.startShimmerAnimation();
        shimmer_internet.startShimmerAnimation();
        shimmer_slow_internet.startShimmerAnimation();
    }

    //در اینجا رسایکلرها را نمایش می دهد
    @Override
    public void ShowMain() {
        main.setVisibility(View.VISIBLE);
    }

    //اینجا زمانی که عملیات با موفقیت انجام شود متد زیر فراخوانی می شود
    @Override
    public void Finish() {
        shimmer_error.stopShimmerAnimation();
        shimmer_internet.stopShimmerAnimation();
        shimmer_slow_internet.stopShimmerAnimation();

        HideBtnShowAlls();
    }

    //در اینجا در صورت بسته شده صفحه عملیات خواندن داده ها را لغو می کند
    @Override
    public void onDestroy() {
        super.onDestroy();
        p_home.GetDisposables().dispose();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_GetAll_Approval:
                ((CentralActivity) getActivity()).getBottomBar().setSelectedItemId(R.id.BottomBar_Approvals);
                break;

            case R.id.btn_GetAll_Request:
                ((CentralActivity) getActivity()).getBottomBar().setSelectedItemId(R.id.BottomBar_Request);
                break;

            case R.id.btn_GetAll_Meeting:
                ((CentralActivity) getActivity()).getBottomBar().setSelectedItemId(R.id.BottomBar_Meetings);
                break;

            case R.id.btn_Error_Again:
            case R.id.btn_NoInternet_Retry:
            case R.id.btn_SlowInternet_Retry:
                p_home.Start();
                break;
        }
    }

    //در اینجا اگر یکی از رسایکلرهای ما خالی باشد دکمه نمایش همه مربوط به آن مخفی می شود
    void HideBtnShowAlls(){

        if (meetingAdapter.getItemCount()==0){
            btn_GetAll_Meeting.setVisibility(View.GONE);
        }

        if (approvalAdapter.getItemCount()==0){
            btn_GetAll_Approval.setVisibility(View.GONE);
        }

        if (requestAdapter.getItemCount()==0){
            btn_GetAll_Request.setVisibility(View.GONE);
        }

    }

}
