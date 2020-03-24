package ir.tdaapp.mms.Model.ViewModels;

import androidx.annotation.NonNull;

//برای جلسات
public class VM_Meetings {
    private int Id;
    private String Title;

    public VM_Meetings() {
    }

    public VM_Meetings(int id, String title) {
        Id = id;
        Title = title;
    }

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

    @NonNull
    @Override
    public String toString() {
        return getTitle();
    }
}
