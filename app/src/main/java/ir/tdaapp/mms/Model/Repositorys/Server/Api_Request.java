package ir.tdaapp.mms.Model.Repositorys.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ir.tdaapp.mms.Model.Enums.Request_Condition;
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.ViewModels.VM_Requests;

public class Api_Request extends BaseApi {

    //در اینجا لیست درخواست ها گرفته می شود
    public Single<List<VM_Requests>> GetRequests(int Page) {

        return Single.create(emitter -> {

            Thread thread = new Thread(() -> {

                try {

                    List<VM_Requests> requests = new ArrayList<>();

                    for (int i = 0; i < 30; i++) {
                        VM_Requests vm_requests = new VM_Requests();
                        vm_requests.setTitle("درخواست" + i);
                        vm_requests.setId(i);

                        if (i % 2 == 0) {
                            vm_requests.setCondition(Request_Condition.Accepted);
                        } else if (i == 3 || i == 7 || i == 9) {
                            vm_requests.setCondition(Request_Condition.Waiting);
                        }else{
                            vm_requests.setCondition(Request_Condition.Reject);
                        }

                        requests.add(vm_requests);
                    }

                    emitter.onSuccess(requests);

                } catch (Exception e) {
                    emitter.onError(e);
                }

            });
            thread.run();

        });

    }

}
