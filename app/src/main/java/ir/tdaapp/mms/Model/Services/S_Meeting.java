package ir.tdaapp.mms.Model.Services;

import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;

public interface S_Meeting {
    void OnStart();
    void onLoading(boolean isLoading);
    void onError(ResaultCode resaultCode);
    void onSuccess();
    void onFinish();
    void onSetItem(VM_Meetings meeting);
    void onHideAll();
}
