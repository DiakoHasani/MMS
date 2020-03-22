package ir.tdaapp.mms.Model.Services;

import java.util.List;
import ir.tdaapp.mms.Model.ViewModels.VM_WorkYear;

public interface S_AddRequest {
    void OnStart();
    void onGetWorkYears(List<VM_WorkYear> workYears);
}
