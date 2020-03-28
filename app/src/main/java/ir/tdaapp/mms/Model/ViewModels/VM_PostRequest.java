package ir.tdaapp.mms.Model.ViewModels;

//داده های مربوط به افزودن درخواست
public class VM_PostRequest {

    int RoleId, UserId, WorkYearId, CouncilId, SessionId;
    String RequestText,AttachmentFile;

    public int getRoleId() {
        return RoleId;
    }

    public void setRoleId(int roleId) {
        RoleId = roleId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getWorkYearId() {
        return WorkYearId;
    }

    public void setWorkYearId(int workYearId) {
        WorkYearId = workYearId;
    }

    public int getCouncilId() {
        return CouncilId;
    }

    public void setCouncilId(int councilId) {
        CouncilId = councilId;
    }

    public int getSessionId() {
        return SessionId;
    }

    public void setSessionId(int sessionId) {
        SessionId = sessionId;
    }

    public String getRequestText() {
        return RequestText;
    }

    public void setRequestText(String requestText) {
        RequestText = requestText;
    }

    public String getAttachmentFile() {
        return AttachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        AttachmentFile = attachmentFile;
    }
}
