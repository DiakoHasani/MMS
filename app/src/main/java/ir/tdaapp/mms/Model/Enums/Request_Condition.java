package ir.tdaapp.mms.Model.Enums;

//اینجا برای وضعیت درخواست که این درخواست رد شده یا پذیرفته شده است یا در  حال انتظار است
public enum Request_Condition {

    //تایید شده
    Accepted,

    //رد شده
    Reject,

    //در انتظار
    Waiting
}
