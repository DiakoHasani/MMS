package ir.tdaapp.mms.Model.Repositorys.Server;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.li_volley.Volleys.GetJsonArrayVolley;
import ir.tdaapp.li_volley.Volleys.GetJsonObjectVolley;
import ir.tdaapp.mms.Model.Enums.Request_Condition;
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.ViewModels.VM_Approvals;
import ir.tdaapp.mms.Model.ViewModels.VM_Home;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;

public class Api_Home extends BaseApi {

    GetJsonObjectVolley volley_GetVals;

    //دراینجا اطلاعات مربوط به صفحه اصلی می باشد
    public Single<VM_Home> GetVals(int UserId, int RoleId) {

        return Single.create(emitter -> {

            Thread thread = new Thread(() -> {

                try {

                    volley_GetVals = new GetJsonObjectVolley(ApiUrl + "Home/GetHome?RoleId=" + RoleId + "&UserId=" + UserId, resault -> {

                        if (resault.getResault() == ResaultCode.Success) {

                            VM_Home vm_home = new VM_Home();

                            //در اینجا لیست جلسات ست می شود
                            List<VM_Meetings> meetings = new ArrayList<>();

                            //در اینجا لیست درخواست ها ست می شود
                            List<VM_Requests> requests = new ArrayList<>();

                            //در اینجا لیست صورت جلسات ست می شود
                            List<VM_Approvals> approvals = new ArrayList<>();

                            JSONArray array_Meeting = null, array_Request = null, array_Approval = null;

                            try {
                                //در اینجا لیست جلسات گرفته می شود
                                array_Meeting = resault.getObject().getJSONArray("CouncilSessions");

                                //در اینجا لیست درخواست ها گرفته می شود
                                array_Request = resault.getObject().getJSONArray("Requests");

                                //در اینجا لیست صورت جلسات گرفته می شود
                                array_Approval = resault.getObject().getJSONArray("Approvals");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //در اینجا داده های جلسات در لیست ست می شوند
                            if (array_Meeting != null) {

                                for (int i = 0; i < array_Meeting.length(); i++) {

                                    try {

                                        JSONObject object = array_Meeting.getJSONObject(i);
                                        VM_Meetings meeting = new VM_Meetings();

                                        meeting.setId(object.getInt("Id"));
                                        meeting.setTitle(object.getString("Title"));

                                        meetings.add(meeting);

                                    } catch (Exception e) {
                                    }

                                }

                            }

                            //در اینجا درخواست ها ست می شود
                            if (array_Request != null) {

                                for (int i = 0; i < array_Request.length(); i++) {

                                    try {

                                        JSONObject object = array_Request.getJSONObject(i);
                                        VM_Requests request = new VM_Requests();

                                        request.setId(object.getInt("Id"));
                                        request.setTitle(object.getString("Title"));

                                        if (object.getInt("Condition") == 0) {
                                            request.setCondition(Request_Condition.Waiting);
                                        } else if (object.getInt("Condition") == 1) {
                                            request.setCondition(Request_Condition.Accepted);
                                        } else {
                                            request.setCondition(Request_Condition.Reject);
                                        }

                                        requests.add(request);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }

                            //در اینجا داده های صورت جلسات ست می شود
                            if (array_Approval != null) {

                                for (int i = 0; i < array_Approval.length(); i++) {

                                    try {

                                        JSONObject object = array_Approval.getJSONObject(i);
                                        VM_Approvals approval = new VM_Approvals();

                                        approval.setId(object.getInt("Id"));
                                        approval.setTitle(object.getString("Title"));

                                        approvals.add(approval);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }

                            //در اینجا لیست های ما در ویو مدل ست می شوند تا آن ها را به سمت پرزنترمان ارسال کنیم
                            vm_home.setApprovals(approvals);
                            vm_home.setMeetings(meetings);
                            vm_home.setRequests(requests);

                            emitter.onSuccess(vm_home);

                        } else {
                            emitter.onError(new IOException(resault.getResault().toString()));
                        }

                    });

                } catch (Exception e) {
                    emitter.onError(e);
                }

            });

            thread.start();
        });
    }

    public void Cancel(String TAG, Context context) {
        if (volley_GetVals != null) {
            volley_GetVals.Cancel(TAG, context);
        }
    }

}
