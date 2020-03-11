package ir.tdaapp.mms.Model.ViewModels;

import java.util.ArrayList;
import java.util.List;

public class VM_Home {

    public VM_Home() {
        Approvals=new ArrayList<>();
        Meetings=new ArrayList<>();
        Requests=new ArrayList<>();
    }

    //برای صورت جلسات
    private List<VM_Approvals> Approvals;

    //برای جلسات
    private List<VM_Meetings> Meetings;

    //برای درخواست ها
    private List<VM_Requests> Requests;

    public List<VM_Approvals> getApprovals() {
        return Approvals;
    }

    public void setApprovals(List<VM_Approvals> approvals) {
        Approvals = approvals;
    }

    public List<VM_Meetings> getMeetings() {
        return Meetings;
    }

    public void setMeetings(List<VM_Meetings> meetings) {
        Meetings = meetings;
    }

    public List<VM_Requests> getRequests() {
        return Requests;
    }

    public void setRequests(List<VM_Requests> requests) {
        Requests = requests;
    }
}
