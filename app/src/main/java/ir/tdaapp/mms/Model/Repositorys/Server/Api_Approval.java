package ir.tdaapp.mms.Model.Repositorys.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.ViewModels.VM_Approvals;

public class Api_Approval extends BaseApi {

    //در اینجا لیست صورت جلسات برگشت داده می شود
    public Single<List<VM_Approvals>> GetApprovals(int Page) {

        return Single.create(emitter -> {

            Thread thread = new Thread(() -> {

                List<VM_Approvals> vals = new ArrayList<>();

                for (int i = 0; i < 20; i++) {

                    VM_Approvals vm_approvals = new VM_Approvals();
                    vm_approvals.setId(i);
                    vm_approvals.setTitle("صورت جلسه " + i);
                    vals.add(vm_approvals);
                }

                emitter.onSuccess(vals);

            });
            thread.start();

        });

    }

}
