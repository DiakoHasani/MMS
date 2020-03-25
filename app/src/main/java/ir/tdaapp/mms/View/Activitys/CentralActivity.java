package ir.tdaapp.mms.View.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.plugins.RxJavaPlugins;
import ir.tdaapp.mms.Model.Enums.BottomBarItem;
import ir.tdaapp.mms.Model.Repositorys.DataBase.Tbl_Role;
import ir.tdaapp.mms.Model.Repositorys.DataBase.Tbl_User;
import ir.tdaapp.mms.Model.Services.S_Central;
import ir.tdaapp.mms.Presenter.P_Central;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Dialogs.RoleDialog;
import ir.tdaapp.mms.View.Fragments.AddRequestFragment;
import ir.tdaapp.mms.View.Fragments.ApprovalsFragment;
import ir.tdaapp.mms.View.Fragments.HomeFragment;
import ir.tdaapp.mms.View.Fragments.MeetingsFragment;
import ir.tdaapp.mms.View.Fragments.RequestFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class CentralActivity extends AppCompatActivity implements S_Central, NavigationView.OnNavigationItemSelectedListener {

    private P_Central p_central;
    private BottomNavigationView BottomBar;
    private FrameLayout FrameHome, FrameRequest, FrameApprovals, FrameMeetings;
    private DrawerLayout drawer;
    private Animation aniFadeIn, aniFadeOut, aniSlideDown, aniSlideUp;
    private Tbl_User tbl_user;
    private Tbl_Role tbl_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        FindItem();
        Implements();
    }

    //در اینجا کاربر در هر فرگمنتی باشد با فراخوانی این متد به باتوم بار دسترسی پیدا خواهد کرد
    public BottomNavigationView getBottomBar() {
        return BottomBar;
    }

    void FindItem() {
        BottomBar = findViewById(R.id.BottomBar);
        FrameHome = findViewById(R.id.FrameHome);
        FrameRequest = findViewById(R.id.FrameRequest);
        FrameApprovals = findViewById(R.id.FrameApprovals);
        FrameMeetings = findViewById(R.id.FrameMeetings);
        drawer = findViewById(R.id.drawer);
    }

    void Implements() {
        aniFadeIn = AnimationUtils.loadAnimation(this, R.anim.short_fadein);
        aniFadeOut = AnimationUtils.loadAnimation(this, R.anim.short_fadeout);
        aniSlideUp=AnimationUtils.loadAnimation(this, R.anim.slide_up);
        aniSlideDown=AnimationUtils.loadAnimation(this, R.anim.slide_down);
        p_central = new P_Central(getSupportFragmentManager(), this);
        BottomBar.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        tbl_user = new Tbl_User(getApplicationContext());
        tbl_role = new Tbl_Role(getApplicationContext());
    }

    //مربوط به رویداد کلیک باتوم نویگیشن ویو
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        //در اینجا نشان می دهد کدام آیتم انتخاب شده است
        int ItemSelection = BottomBar.getSelectedItemId();

        switch (menuItem.getItemId()) {

            //در اینجا صفحه اصلی نمایش داده می شود
            case R.id.BottomBar_Home:
                if (ItemSelection != R.id.BottomBar_Home) {

                    Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.FrameHome);
                    p_central.SetEnabledDrawer(fragmentInFrame);

                    p_central.SetPage(BottomBarItem.Home);

                    SetEnableDrawer(BottomBarItem.Home);
                }
                break;

            //در اینجا صفحه درخواست ها نمایش داده می شود
            case R.id.BottomBar_Request:
                if (ItemSelection != R.id.BottomBar_Request) {

                    Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.FrameRequest);
                    p_central.SetEnabledDrawer(fragmentInFrame);

                    p_central.SetPage(BottomBarItem.Request);

                    SetEnableDrawer(BottomBarItem.Request);
                }
                break;

            //در اینجا صفحه مصوبات نمایش داده می شود
            case R.id.BottomBar_Approvals:
                if (ItemSelection != R.id.BottomBar_Approvals) {

                    Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.FrameApprovals);
                    p_central.SetEnabledDrawer(fragmentInFrame);

                    p_central.SetPage(BottomBarItem.Approvals);

                    SetEnableDrawer(BottomBarItem.Approvals);
                }
                break;

            //در اینجا جلسات نمایش داده می شود
            case R.id.BottomBar_Meetings:
                if (ItemSelection != R.id.BottomBar_Meetings) {

                    Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.FrameMeetings);
                    p_central.SetEnabledDrawer(fragmentInFrame);

                    p_central.SetPage(BottomBarItem.Meetings);

                    SetEnableDrawer(BottomBarItem.Meetings);
                }
                break;
        }

        return true;
    }

    //در اینجا یک فرگمنت را می گیرد و فریم لایوت آن را مخفی می کند
    @Override
    public void HideFrameLayout(BottomBarItem item) {
        switch (item) {
            case Home:
                FrameHome.startAnimation(aniFadeOut);
                FrameHome.setVisibility(View.INVISIBLE);
                break;
            case Request:
                FrameRequest.startAnimation(aniFadeOut);
                FrameRequest.setVisibility(View.INVISIBLE);
                break;
            case Approvals:
                FrameApprovals.startAnimation(aniFadeOut);
                FrameApprovals.setVisibility(View.INVISIBLE);
                break;
            case Meetings:
                FrameMeetings.startAnimation(aniFadeOut);
                FrameMeetings.setVisibility(View.INVISIBLE);
                break;
        }
    }

    //در اینجا یک فرگمنت می گیرد و فریم لایوت آن را نمایش می دهد
    @Override
    public void ShowFrameLayout(BottomBarItem item) {
        switch (item) {
            case Home:
                FrameHome.startAnimation(aniFadeIn);
                FrameHome.setVisibility(View.VISIBLE);
                break;
            case Request:
                FrameRequest.startAnimation(aniFadeIn);
                FrameRequest.setVisibility(View.VISIBLE);
                break;
            case Approvals:
                FrameApprovals.startAnimation(aniFadeIn);
                FrameApprovals.setVisibility(View.VISIBLE);
                break;
            case Meetings:
                FrameMeetings.startAnimation(aniFadeIn);
                FrameMeetings.setVisibility(View.VISIBLE);
                break;
        }
    }

    //در اینجا فعال بودن یا بسته شدن منوی بغل ست می شود برای زمانی است که کاربر صفحه گوشی خود را به سمت چپ می کشد و منوی بغل نمایش داده می شود
    @Override
    public void EnableDrawer(boolean res) {
        if (res) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onBackPressed() {

        //در اینجا نمایش می دهد که الان کدام فریم لایوت در حال نمایش است
        BottomBarItem ItemSelected = p_central.getItemSelect();

        if (ItemSelected != null) {

            //اگر فریم لایوت صفحه اصلی باز باشد شرط زیر اجرا می شود
            if (ItemSelected == BottomBarItem.Home) {

                //در اینجا فرگمنت جاری روی فریم لایوت صفحه اصلی بدست می آوریم
                Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.FrameHome);

                if (fragmentInFrame instanceof HomeFragment) {
                    if (!CloseDrawer())
                        super.onBackPressed();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragmentInFrame).commitNow();
                }
                SetEnableDrawer(BottomBarItem.Home);
            }

            //اگر فریم لایوت صفحه درخواست باز باشد شرط زیر اجرا می شود
            else if (ItemSelected == BottomBarItem.Request) {

                //در اینجا فرگمنت جاری روی فریم لایوت درخواست ها بدست می آوریم
                Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.FrameRequest);

                if (fragmentInFrame instanceof RequestFragment) {
                    if (!CloseDrawer())
                        BottomBar.setSelectedItemId(R.id.BottomBar_Home);
                } else {

                    //در اینجا اگر در صفحه افزودن درخواست باشد باتوم بار را نمایش می دهد
                    if (fragmentInFrame instanceof AddRequestFragment) {
                        ShowBottombar();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .remove(fragmentInFrame).commitNow();
                }

                SetEnableDrawer(BottomBarItem.Request);
            }

            //اگر فریم لایوت صفحه مصوبات باز باشد شرط زیر اجرا می شود
            else if (ItemSelected == BottomBarItem.Approvals) {

                //در اینجا فرگمنت جاری روی فریم لایوت مصوبات بدست می آوریم
                Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.FrameApprovals);

                if (fragmentInFrame instanceof ApprovalsFragment) {
                    if (!CloseDrawer())
                        BottomBar.setSelectedItemId(R.id.BottomBar_Home);
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragmentInFrame).commitNow();
                }

                SetEnableDrawer(BottomBarItem.Approvals);
            }

            //اگر فریم لایوت صفحه جلسات باز باشد شرط زیر اجرا می شود
            else if (ItemSelected == BottomBarItem.Meetings) {

                //در اینجا فرگمنت جاری روی فریم لایوت جلسات بدست می آوریم
                Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.FrameMeetings);

                if (fragmentInFrame instanceof MeetingsFragment) {
                    if (!CloseDrawer())
                        BottomBar.setSelectedItemId(R.id.BottomBar_Home);
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragmentInFrame).commitNow();
                }

                SetEnableDrawer(BottomBarItem.Meetings);
            }

        }
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    //در اینج اگر منوی بل باز باشد آن را می بندد
    boolean CloseDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return true;
        }
        return false;
    }

    //در اینجا فریم لوت جاری را گرفته و عملیات فعالشدن و غیر فعال شدن منوی بغل را انجام می دهد
    public void SetEnableDrawer(BottomBarItem item) {

        Fragment fragment;

        if (item == BottomBarItem.Home) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.FrameHome);
        } else if (item == BottomBarItem.Request) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.FrameRequest);
        } else if (item == BottomBarItem.Approvals) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.FrameApprovals);
        } else {
            fragment = getSupportFragmentManager().findFragmentById(R.id.FrameMeetings);
        }

        p_central.SetEnabledDrawer(fragment);
    }

    public Tbl_User getTbl_user() {
        return tbl_user;
    }

    public Tbl_Role getTbl_role() {
        return tbl_role;
    }

    public Animation getAniFadeIn() {
        return aniFadeIn;
    }

    public Animation getAniFadeOut() {
        return aniFadeOut;
    }

    //در اینجا دیالوگ مربوط به انتخاب نقش ها نمایش داده می شود
    public void ShowRoleDialog() {

        Thread thread = new Thread(() -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(RoleDialog.TAG);
            if (prev == null) {
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new RoleDialog();
                dialogFragment.show(ft, RoleDialog.TAG);
            }
        });
        thread.run();

    }

    public void ShowBottombar() {
        getBottomBar().setAnimation(aniSlideUp);
        getBottomBar().setVisibility(View.VISIBLE);
    }

    public void HideBottombar() {
        getBottomBar().setAnimation(aniSlideDown);
        getBottomBar().setVisibility(View.GONE);
    }
}

