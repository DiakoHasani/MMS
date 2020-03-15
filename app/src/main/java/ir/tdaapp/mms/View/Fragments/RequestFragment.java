package ir.tdaapp.mms.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_FilterRequest;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
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
    LinearLayout No_Internet, error, Slow_Internet;
    ShimmerFrameLayout shimmer_error, shimmer_internet, shimmer_slow_internet, shimmer_NotMemberAnyCouncil;
    TextView btn_Error_Again, btn_NoInternet_Retry, btn_SlowInternet_Retry;
    Spinner cmb_Meetings, cmb_Council;
    LinearLayout spinners, NotMemberAnyCouncil;
    MenuItem menu_manegment_requests;
    ProgressBar Loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_fragment, container, false);

        FindItem(view);
        Implements();
        SetToolBar();

        //در اینجا گرفتن داده ها شروع می شود
        p_request.Start(getFilter(), true);

        return view;
    }

    void Implements() {
        toggle = new ActionBarDrawerToggle(getActivity(), ((CentralActivity) getActivity()).getDrawer(), R.string.Open, R.string.Close);
        p_request = new P_Request(this, getContext());
        btn_Error_Again.setOnClickListener(this);
        btn_NoInternet_Retry.setOnClickListener(this);
        btn_SlowInternet_Retry.setOnClickListener(this);

        //برای زمانی که آیتم اسپینر جلسات تغییر کند
        cmb_Meetings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Start();
                p_request.getRequests(getFilter());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //برای زمانی که آیتم اسپینر شورا تغییر کند
        cmb_Council.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int Id = ((VM_Councils) adapterView.getItemAtPosition(i)).getId();
                if (Id != 0) {
                    Start();
                    p_request.Start(getFilter(), false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        Slow_Internet = view.findViewById(R.id.Slow_Internet);
        shimmer_slow_internet = view.findViewById(R.id.shimmer_slow_internet);
        btn_SlowInternet_Retry = view.findViewById(R.id.btn_SlowInternet_Retry);
        cmb_Meetings = view.findViewById(R.id.cmb_Meetings);
        cmb_Council = view.findViewById(R.id.cmb_Council);
        spinners = view.findViewById(R.id.spinners);
        NotMemberAnyCouncil = view.findViewById(R.id.NotMemberAnyCouncil);
        shimmer_NotMemberAnyCouncil = view.findViewById(R.id.shimmer_NotMemberAnyCouncil);
        Loading = view.findViewById(R.id.Loading);
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

    //در اینجا فیلتر های کاربر برای نمایش درخواست ها ارسال می شود
    VM_FilterRequest getFilter() {
        VM_FilterRequest filterRequest = new VM_FilterRequest();

        //اگر صفحه برای اولین بار بازشود و اسپینر شورا هیچ آداپتری نداشته باشد شرط زیر اجرا می شود
        if (cmb_Council.getAdapter() == null) {
            filterRequest.setCouncilId(0);
        } else {
            if (cmb_Council.getAdapter().getCount() > 0) {
                filterRequest.setCouncilId(((VM_Councils) cmb_Council.getSelectedItem()).getId());
            } else {
                filterRequest.setCouncilId(0);
            }
        }

        if (cmb_Meetings.getAdapter() == null) {
            filterRequest.setMeetingId(0);
        } else {
            if (cmb_Meetings.getAdapter().getCount() > 0) {
                filterRequest.setMeetingId(((VM_Meetings) cmb_Meetings.getSelectedItem()).getId());
            } else {
                filterRequest.setMeetingId(0);
            }
        }

        filterRequest.setRoleId(((CentralActivity) getActivity()).getTbl_role().GetRoleId());
        filterRequest.setUserId(((CentralActivity) getActivity()).getTbl_user().GetUserId());

        return filterRequest;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.request_menu, menu);

        menu_manegment_requests = menu.findItem(R.id.toolBar_Request_ManegeRequest);
        menu_manegment_requests.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.toolBar_Request_ChangeRoll:
                ((CentralActivity) getActivity()).ShowRoleDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
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
        shimmer_NotMemberAnyCouncil.startShimmerAnimation();
    }

    //اگر در برگشت داده ها خطای رخ دهد متد زیر فراخوانی می شود
    @Override
    public void OnError(ResaultCode resault) {
        switch (resault) {
            case NetworkError:
                No_Internet.setAnimation(((CentralActivity) getActivity()).getAniFadeIn());
                No_Internet.setVisibility(View.VISIBLE);
                break;
            case TimeoutError:
                Slow_Internet.setAnimation(((CentralActivity) getActivity()).getAniFadeIn());
                Slow_Internet.setVisibility(View.VISIBLE);
            case ServerError:
            case ParseError:
            case Error:
                error.setAnimation(((CentralActivity) getActivity()).getAniFadeIn());
                error.setVisibility(View.VISIBLE);
                break;
        }
    }

    //در اینجا حالت لودینگ ست می شود که یک مقدار می گیرد که آیا برنامه در حالت لودینگ قرار گیرد
    @Override
    public void Loading(boolean isLoading) {
        if (isLoading) {
            Loading.setVisibility(View.VISIBLE);
        } else {
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
        shimmer_slow_internet.stopShimmerAnimation();
        shimmer_NotMemberAnyCouncil.stopShimmerAnimation();
    }

    //در اینجا تمامی آیتم ها مخفی می شوند
    @Override
    public void HideAll() {

        Recycler.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
        Recycler.setVisibility(View.GONE);

        No_Internet.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
        No_Internet.setVisibility(View.GONE);

        error.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
        error.setVisibility(View.GONE);

        Slow_Internet.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
        Slow_Internet.setVisibility(View.GONE);

        cmb_Meetings.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
        cmb_Meetings.setVisibility(View.GONE);

        cmb_Council.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
        cmb_Council.setVisibility(View.GONE);

        NotMemberAnyCouncil.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
        NotMemberAnyCouncil.setVisibility(View.GONE);

        Loading.setVisibility(View.GONE);
    }

    //در اینجا زمانی که داده ها با موفقیت دریافت شوند متد زیر برای نمایش رسایکلر فراخوانی می شود
    @Override
    public void OnSuccess() {
        Recycler.setAnimation(((CentralActivity) getActivity()).getAniFadeIn());
        Recycler.setVisibility(View.VISIBLE);
    }

    //زمانی که داده های اسپینر گرفته شوند متد زیر فراخوانی می شود
    @Override
    public void OnSuccessGetSpinners() {

        //در اینجا اگر اسپینر جلسات آداپتر داشته باشد شرط زیر اجرا می شود در غیر این صورت اسپینر را مخفی می کند
        if (cmb_Meetings.getAdapter() != null) {

            //اگر اسپینر جلسات آیتم داشته باشد آن رانمایش می دهد در غیر این صورت آن را مخفی می کند
            if (cmb_Meetings.getAdapter().getCount() > 0) {

                cmb_Meetings.setAnimation(((CentralActivity) getActivity()).getAniFadeIn());
                cmb_Meetings.setVisibility(View.VISIBLE);
            } else {

                cmb_Meetings.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
                cmb_Meetings.setVisibility(View.GONE);

                if (cmb_Council.getAdapter() != null) {
                    //اگر کاربر تک شورایی باشد به صورت پیش فرض یکی از آیتم های آن را انتخاب می کند
                    if (cmb_Council.getAdapter().getCount() == 2) {
                        cmb_Council.setSelection(1);
                    }
                }
            }

        } else {
            cmb_Meetings.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
            cmb_Meetings.setVisibility(View.GONE);
        }
    }

    //در اینجا داده های اسپینر جلسات دریافت می شود
    @Override
    public void onGetMeetings(ArrayAdapter<VM_Meetings> adapter) {
        cmb_Meetings.setAdapter(adapter);
    }

    //در اینجا داده های اسپینر شوراها ست می شود
    @Override
    public void onGetCouncil(ArrayAdapter<VM_Councils> adapter) {
        cmb_Council.setAdapter(adapter);
    }

    //در اینجا معلوم می شود که اسپینر شوراها نمایش داده شود یا مخفی شود
    @Override
    public void onShowSpinnerCouncil(boolean show) {
        if (show) {
            cmb_Council.setAnimation(((CentralActivity) getActivity()).getAniFadeIn());
            cmb_Council.setVisibility(View.VISIBLE);
        } else {
            cmb_Council.setAnimation(((CentralActivity) getActivity()).getAniFadeOut());
            cmb_Council.setVisibility(View.GONE);
        }
    }

    //در اینجا ست می شود که دکمه بررسی درخواست ها در تولبار نمایش داده شود یا خیر
    @Override
    public void onShowManegmentRequests(boolean show) {
        if (show){
            menu_manegment_requests.setVisible(true);
        }else{
            menu_manegment_requests.setVisible(false);
        }
    }

    //برای نمایش آیتم شما عضو هیچ شورایی نیستید
    @Override
    public void onNotMemberAnyCouncil() {
        NotMemberAnyCouncil.setAnimation(((CentralActivity) getActivity()).getAniFadeIn());
        NotMemberAnyCouncil.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Error_Again:
            case R.id.btn_NoInternet_Retry:
            case R.id.btn_SlowInternet_Retry:

                if (cmb_Council.getAdapter()!=null){
                    cmb_Council.setAdapter(null);
                }

                if (cmb_Meetings.getAdapter()!=null){
                    cmb_Meetings.setAdapter(null);
                }

                p_request.Start(getFilter(), true);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        p_request.GetDisposables().dispose();
    }
}
