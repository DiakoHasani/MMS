package ir.tdaapp.mms.Model.ViewModels;

//مربوط به جواب نتیجه عملیات در سرور
public class VM_Message {

    //در اینجا کد مربوط به نتیجه عملیات نگهداری می شود
    private int Code;

    //در اینجا پیغام عملیات برای نمایش یه کاربر نگهداری می شود
    private String MessageText;

    //در اینجا نتیچه عملیات که به درستی انجام شده است یا خیر نگهداری می شود
    private boolean Resault;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public boolean isResault() {
        return Resault;
    }

    public void setResault(boolean resault) {
        Resault = resault;
    }
}
