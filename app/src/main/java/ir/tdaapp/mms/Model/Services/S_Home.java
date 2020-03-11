package ir.tdaapp.mms.Model.Services;

import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.ViewModels.VM_Approvals;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;

public interface S_Home {
    void OnError(ResaultCode resault);
    void Loading();
    void HideAll();
    void MeetingItem(VM_Meetings meeting);
    void RequestItem(VM_Requests request);
    void ApprovalItem(VM_Approvals approval);
    void Start();
    void ShowMain();
    void Finish();
}
