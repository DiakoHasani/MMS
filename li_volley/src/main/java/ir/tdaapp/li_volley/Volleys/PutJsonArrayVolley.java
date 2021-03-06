package ir.tdaapp.li_volley.Volleys;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.li_volley.Services.IGetJsonArray;
import ir.tdaapp.li_volley.Utility.AppController;
import ir.tdaapp.li_volley.Utility.CancelVolley;
import ir.tdaapp.li_volley.ViewModel.ResaultGetJsonArrayVolley;

public class PutJsonArrayVolley {
    //آدرس ای پی آی
    String Url = "";

    JSONArray input;

    //زمان انتظار برای دریافت جواب
    int TimeOut = 0;

    //تعداد دفعات تکرار
    int Retries = -1;

    float Multiplier = 1f;

    IGetJsonArray iGetJsonArray;

    JsonArrayRequest request;

    public PutJsonArrayVolley(String url, JSONArray input, IGetJsonArray iGetJsonArray) {
        Url = url;
        this.input = input;
        this.iGetJsonArray = iGetJsonArray;
        Put();
    }

    public PutJsonArrayVolley(String url, JSONArray input, int timeOut, IGetJsonArray iGetJsonArray) {
        Url = url;
        this.input = input;
        TimeOut = timeOut;
        this.iGetJsonArray = iGetJsonArray;
        Put();
    }

    public PutJsonArrayVolley(String url, JSONArray input, int timeOut, int retries, IGetJsonArray iGetJsonArray) {
        Url = url;
        this.input = input;
        TimeOut = timeOut;
        Retries = retries;
        this.iGetJsonArray = iGetJsonArray;
        Put();
    }

    public PutJsonArrayVolley(String url, JSONArray input, int timeOut, int retries, float multiplier, IGetJsonArray iGetJsonArray) {
        Url = url;
        this.input = input;
        TimeOut = timeOut;
        Retries = retries;
        Multiplier = multiplier;
        this.iGetJsonArray = iGetJsonArray;
        Put();
    }

    void Put(){

        ResaultGetJsonArrayVolley resault =new ResaultGetJsonArrayVolley();

        try{

            request=new JsonArrayRequest(Request.Method.PUT,Url,input,response -> {
                resault.setJsonArray(response);
                resault.setResault(ResaultCode.Success);
                iGetJsonArray.Get(resault);
            }
            ,error -> {
                if (error instanceof TimeoutError) {
                    resault.setJsonArray(null);
                    resault.setResault(ResaultCode.TimeoutError);
                } else if (error instanceof ServerError) {
                    resault.setJsonArray(null);
                    resault.setResault(ResaultCode.ServerError);
                } else if (error instanceof NetworkError) {
                    resault.setJsonArray(null);
                    resault.setResault(ResaultCode.NetworkError);
                } else if (error instanceof ParseError) {
                    resault.setJsonArray(null);
                    resault.setResault(ResaultCode.ParseError);
                } else {
                    resault.setJsonArray(null);
                    resault.setResault(ResaultCode.Error);
                }
                resault.setMessage(error.toString());
                iGetJsonArray.Get(resault);
            });

            RetryPolicy policy = new DefaultRetryPolicy(TimeOut, Retries, Multiplier);
            request.setRetryPolicy(policy);

            AppController.getInstance().addToRequestQueue(request);

        }catch (Exception e){
            resault.setJsonArray(null);
            resault.setResault(ResaultCode.Error);
            resault.setMessage(e.toString());
            iGetJsonArray.Get(resault);
        }

    }

    //برای لغو کردن عملیات
    public void Cancel(String TAG, Context context) {
        new CancelVolley(TAG, request, context);
    }

}
