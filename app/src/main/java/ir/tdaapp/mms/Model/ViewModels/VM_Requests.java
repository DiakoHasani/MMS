package ir.tdaapp.mms.Model.ViewModels;

import ir.tdaapp.mms.Model.Enums.Request_Condition;

//برای لیست درخواست ها
public class VM_Requests {
    private int Id;
    private String Title;
    private Request_Condition Condition;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Request_Condition getCondition() {
        return Condition;
    }

    public void setCondition(Request_Condition condition) {
        Condition = condition;
    }
}
