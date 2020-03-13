package ir.tdaapp.mms.Model.ViewModels;

import java.util.List;

//مربوط به صفحه درخواست ها می باشد که در آن لیست درخواست ها و لیست جلسات و لیست شورا ها که دو تای آخر برای فیلتر کردن می باشند
public class VM_MyRequests {

    //لیست درخواست ها
    private List<VM_Requests> requests;

    //لیست جلسات
    private List<VM_Meetings> meetings;

    //لیست شوراها
    private List<VM_Councils> councils;

    //در اینجا دبیر بودن یا نبودن کاربر ست می شود
    private boolean IsManegment;

    public List<VM_Requests> getRequests() {
        return requests;
    }

    public void setRequests(List<VM_Requests> requests) {
        this.requests = requests;
    }

    public List<VM_Meetings> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<VM_Meetings> meetings) {
        this.meetings = meetings;
    }

    public List<VM_Councils> getCouncils() {
        return councils;
    }

    public void setCouncils(List<VM_Councils> councils) {
        this.councils = councils;
    }

    public boolean isManegment() {
        return IsManegment;
    }

    public void setManegment(boolean manegment) {
        IsManegment = manegment;
    }
}
