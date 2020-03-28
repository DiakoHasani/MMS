package ir.tdaapp.mms.Model.Repositorys.Server;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Single;
import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.li_volley.Volleys.PostJsonObjectVolley;
import ir.tdaapp.mms.Model.Utilitys.BaseApi;
import ir.tdaapp.mms.Model.ViewModels.VM_Login;
import ir.tdaapp.mms.Model.ViewModels.VM_Message;

public class Api_Login extends BaseApi {

    PostJsonObjectVolley volley_data;

    //در اینجا مشخصات کاربر را برای لاگین سرور فرستاده نتیجا آن را دریافت می کند
    public Single<VM_Message> data(VM_Login login) {

        return Single.create(emitter -> {

            Thread thread = new Thread(() -> {
                try {

                    //در اینجا مشخصات کاربر در آبجکت ست می شوند
                    JSONObject object = new JSONObject();
                    object.put("NationalCode", login.getUserName());
                    object.put("Password", login.getPassword());
                    object.put("RememberMe", false);
                    object.put("ReturnUrl", "");

                    //در اینجا داده ها را به سمت سرور ارسال می کند
                    volley_data = new PostJsonObjectVolley(ApiUrl + "Account", object, resault -> {

                        //اگر بدون هیچ مشکلی مانند قطعی اینترنت یا مشکل سرور یا غیره باشد شرط زیر اجرا می شود
                        if (resault.getResault() == ResaultCode.Success) {

                            try {

                                //در اینجا نتیجه عملیات در ویو مدل ست می شود
                                VM_Message message = new VM_Message();
                                message.setResault(resault.getObject().getBoolean("Resault"));
                                message.setMessageText(resault.getObject().getString("MessageText"));
                                message.setCode(resault.getObject().getInt("Code"));

                                emitter.onSuccess(message);

                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        }
                        //در غیر اینصورت شرط زیر اجرا می شود
                        else {
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
        if (volley_data != null) {
            volley_data.Cancel(TAG, context);
        }
    }
}
