package ir.tdaapp.mms.Model.Repositorys.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ir.tdaapp.mms.Model.Enums.Request_Condition;
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.ViewModels.VM_Councils;
import ir.tdaapp.mms.Model.ViewModels.VM_FilterRequest;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;
import ir.tdaapp.mms.Model.ViewModels.VM_MyRequests;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;

public class Api_Request extends BaseApi {

    //در اینجا لیست درخواست ها گرفته می شود
    public Single<VM_MyRequests> GetRequests(VM_FilterRequest filter) {

        return Single.create(emitter -> {

            Thread thread = new Thread(() -> {

                try {

                    VM_MyRequests my_Requests=new VM_MyRequests();
                    List<VM_Requests> requests = new ArrayList<>();
                    List<VM_Meetings> meetings = new ArrayList<>();
                    List<VM_Councils> councils = new ArrayList<>();

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

                    for (int i = 1; i < 5; i++) {
                        VM_Meetings meeting = new VM_Meetings();
                        meeting.setId(i);
                        meeting.setTitle("جلسه " + i);

                        meetings.add(meeting);

                        if (i <= 3) {
                            VM_Councils council = new VM_Councils();
                            council.setId(i);
                            council.setTitle("شورا " + i);

                            councils.add(council);
                        }
                    }

                    my_Requests.setCouncils(councils);
                    my_Requests.setMeetings(meetings);
                    my_Requests.setRequests(requests);
                    my_Requests.setManegment(true);

                    emitter.onSuccess(my_Requests);

                } catch (Exception e) {
                    emitter.onError(e);
                }

            });
            thread.run();

        });

    }

}
