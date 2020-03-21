package ir.tdaapp.mms.Presenter;

import android.content.Context;

import ir.tdaapp.mms.Model.Services.S_AddRequest;

public class P_AddRequest {

    Context context;
    S_AddRequest s_addRequest;

    public P_AddRequest(Context context, S_AddRequest s_addRequest) {
        this.context = context;
        this.s_addRequest = s_addRequest;
    }

}
