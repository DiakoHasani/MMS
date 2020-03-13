package ir.tdaapp.mms.Model.ViewModels;

import androidx.annotation.NonNull;

//برای جلسات
public class VM_Meetings {
    private int Id;
    private String Title;

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
