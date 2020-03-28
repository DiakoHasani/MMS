package ir.tdaapp.mms.Model.Repositorys.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.ViewModels.VM_Meetings;

public class Api_Meeting extends BaseApi {

    //در اینجا لیست جلسات برگشت داده می شود
    public Single<List<VM_Meetings>> GetMeetings(int Page) {

        return Single.create(emitter -> {

            Thread thread = new Thread(() -> {

                try {

                    List<VM_Meetings> vals = new ArrayList<>();

                    for (int i = 0; i < 30; i++) {

                        VM_Meetings vm_meetings = new VM_Meetings();
                        vm_meetings.setId(i);
                        vm_meetings.setTitle("جلسه شماره" + i);

                        vals.add(vm_meetings);
                    }
                    emitter.onSuccess(vals);

                } catch (Exception e) {
                    emitter.onError(e);
                }

            });

            thread.start();

        });

    }

}
