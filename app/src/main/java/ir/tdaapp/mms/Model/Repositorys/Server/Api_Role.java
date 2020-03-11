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
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.ViewModels.VM_Role;

public class Api_Role extends BaseApi {

    //در اینجا نقش های کاربر را از سرور می گیرد
    public Single<List<VM_Role>> GetUsersRole(int Id) {

        return Single.create(emitter -> {

            try {

                new GetJsonArrayVolley(ApiUrl + "User?UserId=" + Id, resault -> {

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

            } catch (Exception e) {
                emitter.onError(e);
            }

        });

    }

}
