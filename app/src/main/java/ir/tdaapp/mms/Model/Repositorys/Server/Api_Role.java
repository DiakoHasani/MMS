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
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.ViewModels.VM_Role;

public class Api_Role extends BaseApi {

    GetJsonArrayVolley volley_GetUsersRole;

    //در اینجا نقش های کاربر را از سرور می گیرد
    public Single<List<VM_Role>> GetUsersRole(int Id) {

        return Single.create(emitter -> {

            try {

                Thread thread=new Thread(()->{
                    volley_GetUsersRole = new GetJsonArrayVolley(ApiUrl + "User/GetRolesUser?UserId=" + Id, resault -> {

                        if (resault.getResault() == ResaultCode.Success) {

                            List<VM_Role> roles = new ArrayList<>();
                            JSONArray vals = resault.getJsonArray();

                            for (int i = 0; i < vals.length(); i++) {

                                try {

                                    JSONObject object = vals.getJSONObject(i);
                                    VM_Role role = new VM_Role();
                                    role.setId(object.getInt("RoleId"));
                                    role.setTitle(object.getString("Title"));

                                    roles.add(role);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            emitter.onSuccess(roles);

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

    //در اینجا زمانی که صفحه بسته شود عملیات مربوط به کار با دیتابیس لغو می شود
    public void CancelAll(String TAG, Context context) {
        if (volley_GetUsersRole != null) {
            volley_GetUsersRole.Cancel(TAG, context);
        }
    }

}
