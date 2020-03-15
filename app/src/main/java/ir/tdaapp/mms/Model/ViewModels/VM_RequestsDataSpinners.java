package ir.tdaapp.mms.Model.ViewModels;

import java.util.List;

//در اینجا داده های اسپینر صفحه درخواست ست می شود
public class VM_RequestsDataSpinners {

    private List<VM_Councils> councils;
    private List<VM_Meetings> meetings;
    private boolean isManegment;
    private int countManegeRequests;

    public List<VM_Councils> getCouncils() {
        return councils;
    }

    public void setCouncils(List<VM_Councils> councils) {
        this.councils = councils;
    }

    public List<VM_Meetings> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<VM_Meetings> meetings) {
        this.meetings = meetings;
    }

    public boolean isManegment() {
        return isManegment;
    }

    public void setManegment(boolean manegment) {
        isManegment = manegment;
    }

    public int getCountManegeRequests() {
        return countManegeRequests;
    }

    public void setCountManegeRequests(int countManegeRequests) {
        this.countManegeRequests = countManegeRequests;
    }
}
