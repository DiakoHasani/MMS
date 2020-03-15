package ir.tdaapp.mms.Model.Repositorys.Server;

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
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_FilterRequest;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.Model.ViewModels.VM_RequestsDataSpinners;

public class Api_Request extends BaseApi {

    //در اینجا لیست درخواست ها گرفته می شود
    public Single<List<VM_Requests>> GetRequests(VM_FilterRequest filter) {

        return Single.create(emitter -> {

            Thread thread = new Thread(() -> {

                try {
                    List<VM_Requests> requests = new ArrayList<>();

                    for (int i = 1; i < 20; i++) {
                        VM_Requests request = new VM_Requests();
                        request.setId(i);
                        request.setTitle("درخواست " + i);
                        if (i % 2 == 0) {
                            request.setCondition(Request_Condition.Accepted);
                        } else if (i == 3 && i == 11 && i == 17 && i == 19) {
                            request.setCondition(Request_Condition.Reject);
                        } else {
                            request.setCondition(Request_Condition.Waiting);
                        }

                        requests.add(request);
                    }

                    emitter.onSuccess(requests);

                } catch (Exception e) {
                    emitter.onError(e);
                }

            });
            thread.run();

        });

    }

    //در اینجا مقادیر اسپینر صفحه درخواست و دبیر بودن کاربر و تعداد درخواست های تایید نشده گرفته می شود
    public Single<VM_RequestsDataSpinners> getSpinnerData(int userId, int roleId, int councilId) {

        return Single.create(emitter -> {

            try {

                new GetJsonObjectVolley(ApiUrl + "Request?RoleId=" + roleId + "&UserId=" + userId + "&councilId=" + councilId, resault -> {

                    if (resault.getResault() == ResaultCode.Success) {

                        VM_RequestsDataSpinners vm_request = new VM_RequestsDataSpinners();
                        List<VM_Councils> councils = new ArrayList<>();
                        List<VM_Meetings> meetings = new ArrayList<>();

                        JSONArray CouncilsArray = null, MeetingsArray = null;

                        try {

                            vm_request.setManegment(resault.getObject().getBoolean("Secretary"));
                            vm_request.setCountManegeRequests(resault.getObject().getInt("CountRequest"));
                            CouncilsArray = resault.getObject().getJSONArray("Councils");
                            MeetingsArray = resault.getObject().getJSONArray("CouncilSessions");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (MeetingsArray != null) {

                            for (int i = 0; i < MeetingsArray.length(); i++) {
                                try {

                                    JSONObject object = MeetingsArray.getJSONObject(i);
                                    VM_Meetings meeting = new VM_Meetings();
                                    meeting.setId(object.getInt("Id"));
                                    meeting.setTitle(object.getString("Title"));

                                    meetings.add(meeting);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        if (CouncilsArray != null) {

                            VM_Councils v = new VM_Councils();
                            v.setId(0);
                            v.setTitle("یک شورا انتخاب کنید");
                            councils.add(v);

                            for (int i = 0; i < CouncilsArray.length(); i++) {

                                try {

                                    JSONObject object=CouncilsArray.getJSONObject(i);
                                    VM_Councils council=new VM_Councils();
                                    council.setId(object.getInt("Id"));
                                    council.setTitle(object.getString("Titel"));

                                    councils.add(council);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        vm_request.setMeetings(meetings);
                        vm_request.setCouncils(councils);

                        emitter.onSuccess(vm_request);

                    } else {
                        emitter.onError(new IOException(resault.getResault().toString()));
                    }

                });

            } catch (Exception e) {
                emitter.onError(e);
            }

        });

    }

}
