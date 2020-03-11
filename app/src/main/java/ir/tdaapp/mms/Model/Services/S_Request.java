package ir.tdaapp.mms.Model.Services;

import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;

public interface S_Request {
    void Start();
    void OnError(ResaultCode resault);
    void Loading(boolean isLoading);
    void RequestItem(VM_Requests request);
    void Finish();
    void HideAll();
    void OnSuccess();
}
