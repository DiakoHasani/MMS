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
import ir.tdaapp.li_volley.Volleys.PostJsonArrayVolley;
import ir.tdaapp.li_volley.Volleys.PostJsonObjectVolley;
import ir.tdaapp.mms.Model.Enums.Request_Condition;
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.Utilitys.FileManger;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_FilterRequest;
import ir.tdaapp.mms.Model.ViewModels.VM_FilterRequestsSecretary;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_Message;
import ir.tdaapp.mms.Model.ViewModels.VM_PostRequest;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;
import ir.tdaapp.mms.Model.ViewModels.VM_RequestsDataSpinners;
import ir.tdaapp.mms.Model.ViewModels.VM_WorkYear;
import ir.tdaapp.mms.R;

public class Api_Request extends BaseApi {

    GetJsonArrayVolley volley_GetRequests;
    GetJsonObjectVolley volley_getSpinnerData, volley_getWorkYears, volley_getCoucils, volley_getConcilSessions;
    PostJsonObjectVolley volley_postRequest;
    PostJsonArrayVolley volley_getRequestsSecretary;
    GetJsonObjectVolley volley_getWorkYearsSecretary, volley_getCouncilsSecretary, volley_getCouncilSessionsSecretary;
    FileManger fileManger;

    //در اینجا لیست درخواست ها گرفته می شود
    public Single<List<VM_Requests>> GetRequests(VM_FilterRequest filter) {

        return Single.create(emitter -> {

            Thread thread = new Thread(() -> {

                try {

                    volley_GetRequests = new GetJsonArrayVolley(ApiUrl + "Request/GetMyRequest?UserId=" + filter.getUserId() + "&CouncilsessionId=" + filter.getMeetingId(), resault -> {

                        if (resault.getResault() == ResaultCode.Success) {

                            try {

                                List<VM_Requests> requests = new ArrayList<>();
                                JSONArray array = resault.getJsonArray();

                                for (int i = 0; i < array.length(); i++) {
                                    try {

                                        JSONObject object = array.getJSONObject(i);
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

                                emitter.onSuccess(requests);

                            } catch (Exception e) {
                                emitter.onError(e);
                            }

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

    //در اینجا مقادیر اسپینر صفحه درخواست و دبیر بودن کاربر و تعداد درخواست های تایید نشده گرفته می شود
    public Single<VM_RequestsDataSpinners> getSpinnerData(int userId, int roleId, int councilId) {

        return Single.create(emitter -> {

            try {

                Thread thread = new Thread(() -> {
                    volley_getSpinnerData = new GetJsonObjectVolley(ApiUrl + "Request/GetCouncilAndCouncilsession?RoleId=" + roleId + "&UserId=" + userId + "&councilId=" + councilId, resault -> {

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

                                        JSONObject object = CouncilsArray.getJSONObject(i);
                                        VM_Councils council = new VM_Councils();
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
                });

                thread.start();

            } catch (Exception e) {
                emitter.onError(e);
            }

        });

    }

    //دراینجا لیست سال کاری برگشت داده می شود
    public Single<List<VM_WorkYear>> getWorkYears(Context context, int userId) {

        return Single.create(emitter -> {

            try {

                Thread thread = new Thread(() -> {
                    volley_getWorkYears = new GetJsonObjectVolley(ApiUrl + "Request/GetAddRequest?councilSessionId=0&userId=" + userId, resault -> {

                        if (resault.getResault() == ResaultCode.Success) {

                            List<VM_WorkYear> workYears = new ArrayList<>();

                            try {

                                JSONArray array = resault.getObject().getJSONArray("WorkYears");

                                VM_WorkYear v = new VM_WorkYear();
                                v.setId(0);
                                v.setTitle(context.getResources().getString(R.string.WorkYear));
                                workYears.add(v);

                                for (int i = 0; i < array.length(); i++) {

                                    try {
                                        JSONObject object = array.getJSONObject(i);
                                        VM_WorkYear workYear = new VM_WorkYear();
                                        workYear.setId(object.getInt("Id"));
                                        workYear.setTitle(object.getString("Title"));

                                        workYears.add(workYear);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                emitter.onSuccess(workYears);

                            } catch (Exception e) {
                                emitter.onError(new IOException(resault.getResault().toString()));
                            }
                        } else {
                            emitter.onError(new IOException(resault.getResault().toString()));
                        }

                    });
                });

                thread.start();

            } catch (Exception e) {
                emitter.onError(e);
            }

        });

    }

    //در اینجا آی دی سال کاری می گیرد و لیست شوراهای آن را پاس می دهد
    public Single<List<VM_Councils>> getCoucils(Context context, int userId, int workYearId) {

        return Single.create(emitter -> {

            try {

                Thread thread = new Thread(() -> {
                    volley_getCoucils = new GetJsonObjectVolley(ApiUrl + "Request/GetCouncilsAndSessionsForAddRequests?councilId=null&workYearId=" + workYearId + "&userId=" + userId, resault -> {

                        if (resault.getResault() == ResaultCode.Success) {

                            try {

                                List<VM_Councils> councils = new ArrayList<>();
                                JSONArray array = resault.getObject().getJSONArray("Councils");

                                councils.add(new VM_Councils(0, context.getResources().getString(R.string.Council)));

                                for (int i = 0; i < array.length(); i++) {

                                    try {

                                        JSONObject object = array.getJSONObject(i);
                                        VM_Councils council = new VM_Councils();
                                        council.setId(object.getInt("Id"));
                                        council.setTitle(object.getString("Titel"));

                                        councils.add(council);

                                    } catch (Exception e) {
                                    }

                                }

                                emitter.onSuccess(councils);

                            } catch (Exception e) {
                                emitter.onError(e);
                            }

                        } else {
                            emitter.onError(new IOException(resault.getResault().toString()));
                        }

                    });
                });

                thread.start();

            } catch (Exception e) {
                emitter.onError(e);
            }

        });

    }

    //در اینجا آی دی سالف کاری و شورا را می گیرد و جلسات آن را برگشت می دهد
    public Single<List<VM_Meetings>> getConcilSessions(Context context, int userId, int workYearId, int councilId) {

        return Single.create(emitter -> {

            try {

                Thread thread = new Thread(() -> {

                    volley_getConcilSessions = new GetJsonObjectVolley(ApiUrl + "Request/GetCouncilsAndSessionsForAddRequests?councilId=" + councilId + "&workYearId=" + workYearId + "&userId=" + userId, resault -> {

                        try {

                            JSONArray array = resault.getObject().getJSONArray("CouncilSessions");
                            List<VM_Meetings> meetings = new ArrayList<>();
                            meetings.add(new VM_Meetings(0, context.getResources().getString(R.string.Meetings)));

                            for (int i = 0; i < array.length(); i++) {

                                try {

                                    JSONObject object = array.getJSONObject(i);
                                    VM_Meetings meeting = new VM_Meetings();
                                    meeting.setId(object.getInt("Id"));
                                    meeting.setTitle(object.getString("Title"));

                                    meetings.add(meeting);

                                } catch (Exception e) {
                                }

                            }

                            emitter.onSuccess(meetings);

                        } catch (Exception e) {
                            emitter.onError(e);
                        }

                    });

                });

                thread.start();

            } catch (Exception e) {
                emitter.onError(e);
            }

        });
    }

    //در اینجا فایل درخواست آپلود می شود
    public Single<String> postFile(String path) {

        return Single.create(emitter -> {

            try {

                Thread thread = new Thread(() -> {
                    try {

                        String Url = ApiUrl + "PostFileRequest/PostFile";
                        fileManger = new FileManger(Url);
                        emitter.onSuccess(fileManger.uploadFile(path));

                    } catch (Exception e) {
                        //اگر خطای رخ دهد به احتمال زیاد کاربر فایلی آپلود نکرده است و ما هم مقدار زیر را پاس می دهیم
                        emitter.onSuccess("noFile");
                    }
                });

                thread.start();

            } catch (Exception e) {
                emitter.onError(e);
            }

        });
    }

    //در اینجا یک درخواست اضافه می شود
    public Single<VM_Message> postRequest(VM_PostRequest request) {

        return Single.create(emitter -> {

            new Thread(() -> {

                try {

                    JSONObject object = new JSONObject();
                    object.put("SelectedWorkYearId", request.getWorkYearId());
                    object.put("SelectedCouncilId", request.getCouncilId());
                    object.put("SelectedSessionId", request.getSessionId());
                    object.put("SelectedRoleId", request.getRoleId());
                    object.put("RequestText", request.getRequestText());
                    object.put("AttachmentFile", request.getAttachmentFile());
                    object.put("RoleId", request.getRoleId());
                    object.put("UserId", request.getUserId());

                    volley_postRequest = new PostJsonObjectVolley(ApiUrl + "Request/PostAddRequest", object, resault -> {
                        if (resault.getResault() == ResaultCode.Success) {

                            VM_Message message = new VM_Message();

                            try {
                                message.setCode(resault.getObject().getInt("Code"));
                                message.setMessageText(resault.getObject().getString("MessageText"));
                                message.setResault(resault.getObject().getBoolean("Resault"));
                            } catch (Exception e) {

                            }

                            emitter.onSuccess(message);

                        } else {
                            emitter.onError(new IOException(resault.getResault().toString()));
                        }
                    });

                } catch (Exception e) {
                    emitter.onError(e);
                }

            }).start();

        });

    }

    //در اینجا لیست سال های کاری را به دبیر پاس می دهد
    public Single<List<VM_WorkYear>> getWorkYearsSecretary(int userId, Context context) {

        return Single.create(emitter -> {

            new Thread(() -> {
                volley_getWorkYearsSecretary = new GetJsonObjectVolley(ApiUrl + "Request/GetCheckRequest?UserId=" + userId + "&CouncilSessionId=null", resault -> {

                    try {

                        if (resault.getResault() == ResaultCode.Success) {

                            JSONArray array = resault.getObject().getJSONArray("WorkYears");
                            List<VM_WorkYear> workYears = new ArrayList<>();

                            workYears.add(new VM_WorkYear(0, context.getResources().getString(R.string.WorkYear)));

                            for (int i = 0; i < array.length(); i++) {

                                try {

                                    JSONObject object = array.getJSONObject(i);
                                    VM_WorkYear workYear = new VM_WorkYear();

                                    workYear.setId(object.getInt("Id"));
                                    workYear.setTitle(object.getString("Title"));

                                    workYears.add(workYear);

                                } catch (Exception e) {
                                }
                            }

                            emitter.onSuccess(workYears);

                        } else {
                            emitter.onError(new IOException(resault.getResault().toString()));
                        }

                    } catch (Exception e) {
                        emitter.onError(e);
                    }

                });
            }).start();

        });

    }

    //در اینجا لیست شوراها برای دبیر ارسال می شود
    public Single<List<VM_Councils>> getCouncilsSecretary(Context context, int userId, int workYearId) {

        return Single.create(emitter -> {

            new Thread(() -> {
                volley_getCouncilsSecretary = new GetJsonObjectVolley(ApiUrl + "Request/GetCouncilsAndSesssionsForCheckRequests?UserId=" + userId + "&councilId=null&workYearId=" + workYearId, resault -> {

                    if (resault.getResault() == ResaultCode.Success) {

                        try {

                            JSONArray array = resault.getObject().getJSONArray("Councils");
                            List<VM_Councils> councils = new ArrayList<>();

                            councils.add(new VM_Councils(0, context.getResources().getString(R.string.Council)));

                            for (int i = 0; i < array.length(); i++) {

                                try {

                                    JSONObject object = array.getJSONObject(i);
                                    councils.add(new VM_Councils(object.getInt("Id"), object.getString("Titel")));

                                } catch (Exception e) {
                                }
                            }

                            emitter.onSuccess(councils);

                        } catch (Exception e) {
                            emitter.onError(e);
                        }

                    } else {
                        emitter.onError(new IOException(resault.getResault().toString()));
                    }

                });
            }).start();

        });

    }

    //در اینجا لیست جلسات برگشت داده می شود
    public Single<List<VM_Meetings>> getCouncilSessionsSecretary(Context context, int userId, int workYearId, int councilId) {
        return Single.create(emitter -> {

            new Thread(() -> {
                volley_getCouncilSessionsSecretary = new GetJsonObjectVolley(ApiUrl + "Request/GetCouncilsAndSesssionsForCheckRequests?UserId=" + userId + "&councilId=" + councilId + "&workYearId=" + workYearId, resault -> {

                    if (resault.getResault() == ResaultCode.Success) {

                        try {

                            List<VM_Meetings> meetings = new ArrayList<>();
                            JSONArray array = resault.getObject().getJSONArray("CouncilSessions");
                            meetings.add(new VM_Meetings(0, context.getResources().getString(R.string.Meetings)));

                            for (int i = 0; i < array.length(); i++) {

                                try {

                                    JSONObject object = array.getJSONObject(i);
                                    meetings.add(new VM_Meetings(object.getInt("Id"), object.getString("Title")));

                                } catch (Exception e) {
                                }

                            }

                            emitter.onSuccess(meetings);

                        } catch (Exception e) {
                            emitter.onError(e);
                        }

                    } else {
                        emitter.onError(new IOException(resault.getResault().toString()));
                    }

                });
            }).start();

        });
    }

    //در اینجا لیست درخواست ها برای دبیر برگشت داده می شود
    public Single<List<VM_Requests>> getRequestsSecretary(VM_FilterRequestsSecretary filter) {

        return Single.create(emitter -> {

            new Thread(() -> {


                JSONArray array = new JSONArray();
                try {
                    JSONObject object = new JSONObject();
                    object.put("UserId", filter.getUserId());
                    object.put("CouncilSessionId", filter.getCouncilSessionId());
                    object.put("CouncilId", filter.getCouncilId());
                    object.put("WorkYearId", filter.getWorkYearId());
                    object.put("LoadPreviousRequestThatAreHavingCheckingStatus", filter.isLoadPreviousRequestThatAreHavingCheckingStatus());
                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                volley_getRequestsSecretary = new PostJsonArrayVolley(ApiUrl + "Request/RequetsForCheckRequests", array, resault -> {

                    if (resault.getResault() == ResaultCode.Success) {

                        try {

                            List<VM_Requests> requests = new ArrayList<>();
                            JSONArray a = resault.getJsonArray();

                            for (int i = 0; i < a.length(); i++) {

                                try {

                                    JSONObject object = a.getJSONObject(i);
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

                            emitter.onSuccess(requests);

                        } catch (Exception e) {
                            emitter.onError(e);
                        }

                    } else {
                        emitter.onError(new IOException(resault.getResault().toString()));
                    }

                });

            }).start();

        });

    }

    public void Cancel(String TAG, Context context) {

        if (volley_GetRequests != null) {
            volley_GetRequests.Cancel(TAG, context);
        }

        if (volley_getSpinnerData != null) {
            volley_getSpinnerData.Cancel(TAG, context);
        }

        if (volley_getWorkYears != null) {
            volley_getWorkYears.Cancel(TAG, context);
        }

        if (volley_getCoucils != null) {
            volley_getCoucils.Cancel(TAG, context);
        }

        if (volley_getConcilSessions != null) {
            volley_getConcilSessions.Cancel(TAG, context);
        }

        if (volley_postRequest != null) {
            volley_postRequest.Cancel(TAG, context);
        }

        if (volley_getWorkYearsSecretary != null) {
            volley_getWorkYearsSecretary.Cancel(TAG, context);
        }

        if (volley_getCouncilsSecretary != null) {
            volley_getCouncilsSecretary.Cancel(TAG, context);
        }

        if (volley_getCouncilSessionsSecretary != null) {
            volley_getCouncilSessionsSecretary.Cancel(TAG, context);
        }

        if (volley_getRequestsSecretary != null) {
            volley_getRequestsSecretary.Cancel(TAG, context);
        }
    }

}
