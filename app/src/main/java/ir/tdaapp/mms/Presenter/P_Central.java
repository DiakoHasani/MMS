package ir.tdaapp.mms.Presenter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import ir.tdaapp.mms.Model.Enums.BottomBarItem;
import ir.tdaapp.mms.Model.Services.S_Central;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Fragments.ApprovalsFragment;
import ir.tdaapp.mms.View.Fragments.HomeFragment;
import ir.tdaapp.mms.View.Fragments.MeetingsFragment;
import ir.tdaapp.mms.View.Fragments.RequestFragment;

public class P_Central {

    FragmentManager fragmentManager;
    S_Central s_central;

    HomeFragment homeFragment;
    RequestFragment requestFragment;
    ApprovalsFragment approvalsFragment;
    MeetingsFragment meetingsFragment;
    BottomBarItem ItemSelect;

    public BottomBarItem getItemSelect() {
        return ItemSelect;
    }

    public P_Central(FragmentManager fragmentManager, S_Central s_central) {
        this.fragmentManager = fragmentManager;
        this.s_central = s_central;

        SetPage(BottomBarItem.Home);
    }


    //اینجا برای نمایش صفحه مورد نظر کاربر می باشد
    public void SetPage(BottomBarItem item) {

        switch (item) {
            case Home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.FrameHome, homeFragment, HomeFragment.TAG).commit();
                }

                s_central.ShowFrameLayout(BottomBarItem.Home);

                if (ItemSelect != null)
                    s_central.HideFrameLayout(ItemSelect);

                ItemSelect = BottomBarItem.Home;

                break;
            case Request:

                if (requestFragment == null) {
                    requestFragment = new RequestFragment();
                    fragmentManager.beginTransaction().add(R.id.FrameRequest, requestFragment, RequestFragment.TAG).commit();
                }

                s_central.ShowFrameLayout(BottomBarItem.Request);

                if (ItemSelect != null)
                    s_central.HideFrameLayout(ItemSelect);

                ItemSelect = BottomBarItem.Request;

                break;
            case Approvals:

                if (approvalsFragment == null) {
                    approvalsFragment = new ApprovalsFragment();
                    fragmentManager.beginTransaction().add(R.id.FrameApprovals, approvalsFragment, ApprovalsFragment.TAG).commit();
                }

                s_central.ShowFrameLayout(BottomBarItem.Approvals);

                if (ItemSelect != null)
                    s_central.HideFrameLayout(ItemSelect);

                ItemSelect = BottomBarItem.Approvals;

                break;
            case Meetings:

                if (meetingsFragment == null) {
                    meetingsFragment = new MeetingsFragment();
                    fragmentManager.beginTransaction().add(R.id.FrameMeetings, meetingsFragment, MeetingsFragment.TAG).commit();
                }

                s_central.ShowFrameLayout(BottomBarItem.Meetings);

                if (ItemSelect != null)
                    s_central.HideFrameLayout(ItemSelect);

                ItemSelect = BottomBarItem.Meetings;

                break;
        }
    }

    //در اینجا فعال یا غیر فعال بودن درگر منوی بغل ست می شود
    public void SetEnabledDrawer(Fragment fragment) {

        //اگر فرگمنت جاری یکی از صفحات زیر باشد درگر فعال می شود در غیر این صورت غیر فعال می شود
        if (fragment instanceof HomeFragment || fragment instanceof RequestFragment ||
                fragment instanceof ApprovalsFragment || fragment instanceof MeetingsFragment ||
                fragment == null) {
            s_central.EnableDrawer(true);
        } else {
            s_central.EnableDrawer(false);
        }
    }
}
