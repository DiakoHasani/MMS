package ir.tdaapp.mms.Model.ViewModels;
//برای زمانی که برای دریافت درخواست ها فیلتر خود را به سمت سرور ارسال کند
public class VM_FilterRequest {
    private int UserId,RoleId,MeetingId,CouncilId;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getRoleId() {
        return RoleId;
    }

    public void setRoleId(int roleId) {
        RoleId = roleId;
    }

    public int getMeetingId() {
        return MeetingId;
    }

    public void setMeetingId(int meetingId) {
        MeetingId = meetingId;
    }

    public int getCouncilId() {
        return CouncilId;
    }

    public void setCouncilId(int councilId) {
        CouncilId = councilId;
    }
}
