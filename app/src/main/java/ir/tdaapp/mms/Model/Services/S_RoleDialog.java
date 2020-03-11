package ir.tdaapp.mms.Model.Services;

import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.ViewModels.VM_Role;

public interface S_RoleDialog {
    void OnStart();
    void onLoading(boolean isLoad);
    void onHideAll();
    void onError(ResaultCode resaultCode);
    void onSuccess();
    void onSetRole(VM_Role role);
    void onFinish();
    void onCheckedMyRole(int checked);
}
