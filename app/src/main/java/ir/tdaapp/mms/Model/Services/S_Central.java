package ir.tdaapp.mms.Model.Services;

import ir.tdaapp.mms.Model.Enums.BottomBarItem;

public interface S_Central {
    void HideFrameLayout(BottomBarItem item);
    void ShowFrameLayout(BottomBarItem item);
    void EnableDrawer(boolean res);
}
