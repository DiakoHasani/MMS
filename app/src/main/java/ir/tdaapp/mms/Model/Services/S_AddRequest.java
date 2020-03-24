package ir.tdaapp.mms.Model.Services;

import android.widget.ArrayAdapter;

import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_WorkYear;

public interface S_AddRequest {

    void OnStart();

    void onHideAll();

    void onLoading(boolean show);

    void onGetWorkYears(ArrayAdapter<VM_WorkYear> adapter);

    void onError(ResaultCode resaultCode);

    void onShowAll();

    void onSetDefaultSpinnersData(ArrayAdapter<VM_Councils> councils, ArrayAdapter<VM_Meetings> meetings);

    void onSetDefaultSpinnerSessionData(ArrayAdapter<VM_Meetings> adapter);

    void onGetCouncils(ArrayAdapter<VM_Councils> adapter);

    void onGetCouncilSessionsId(ArrayAdapter<VM_Meetings> adapter);

    void onFinish();
}
