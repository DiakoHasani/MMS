package ir.tdaapp.mms.Model.Services;

import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.ViewModels.VM_Message;

public interface S_Login {
    void Loading(boolean IsLoading);
    void NotValid();
    void IsValid();
    void IsSuccess(int CountRole,int UserId);
    void NotSuccess(VM_Message message);
    void OnError(ResaultCode resault);
    void OnSuccessGetRole();
    void OnErrorGetRole(ResaultCode resault);
}
