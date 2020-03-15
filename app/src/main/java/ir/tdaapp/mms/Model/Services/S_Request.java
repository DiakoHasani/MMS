package ir.tdaapp.mms.Model.Services;

import android.widget.ArrayAdapter;

import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;

public interface S_Request {
    void Start();

    void OnError(ResaultCode resault);

    void Loading(boolean isLoading);

    void RequestItem(VM_Requests request);

    void Finish();

    void HideAll();

    void OnSuccess();

    void OnSuccessGetSpinners();

    void onGetMeetings(ArrayAdapter<VM_Meetings> adapter);

    void onGetCouncil(ArrayAdapter<VM_Councils> adapter);

    void onShowSpinnerCouncil(boolean show);

    void onShowManegmentRequests(boolean show);

    void onNotMemberAnyCouncil();
}
