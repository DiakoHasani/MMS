package ir.tdaapp.mms.Model.Services;

import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.ViewModels.VM_Approvals;

public interface S_Approval {
    void OnStart();
    void onLoading(boolean isLoading);
    void onError(ResaultCode resaultCode);
    void onSuccess();
    void onFinish();
    void onSetItem(VM_Approvals approval);
    void onHideAll();
}
