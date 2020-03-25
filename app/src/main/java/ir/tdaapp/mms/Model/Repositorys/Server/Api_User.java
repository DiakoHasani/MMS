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

//در اینجا عملیات مربوط به کاربر در سرور انجام می شود
public class Api_User extends BaseApi {

    GetJsonArrayVolley volley_GetRolesUser;

    //در اینجا نقش های کاربر در سرور گرفته می شود
    public Single<List<Integer>> GetRolesUser(int UserId) {
        return Single.create(emitter -> {
            Thread thread = new Thread(() -> {
                try {
                    String Url = ApiUrl + "User?UserId=" + UserId;

                    volley_GetRolesUser = new GetJsonArrayVolley(Url, resault -> {
                        if (resault.getResault() == ResaultCode.Success) {

                            List<Integer> vals = new ArrayList<>();
                            JSONArray array = resault.getJsonArray();

                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject object = array.getJSONObject(i);
                                    vals.add(object.getInt("RoleId"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            emitter.onSuccess(vals);

                        } else {
                            emitter.onError(new IOException(resault.getResault().toString()));
                        }
                    });

                } catch (Exception e) {
                    emitter.onError(e);
                }
            });
            thread.run();
        });
    }

    public void Cancel(String TAG, Context context) {
        if (volley_GetRolesUser != null) {
            volley_GetRolesUser.Cancel(TAG, context);
        }
    }
}
