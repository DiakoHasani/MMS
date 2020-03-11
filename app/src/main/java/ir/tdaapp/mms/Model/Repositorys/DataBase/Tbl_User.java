package ir.tdaapp.mms.Model.Repositorys.DataBase;

import android.content.Context;
import android.content.SharedPreferences;

import ir.tdaapp.mms.Model.Utilitys.SharedPreferences_Names;

import static android.content.Context.MODE_PRIVATE;

public class Tbl_User {
    Context context;

    public Tbl_User(Context context) {
        this.context = context;
    }

    //در اینجا یوزر آی دی کاربر ثبت می شود
    public void AddUser(int UserId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPreferences_Names.MyPREFERENCES, MODE_PRIVATE).edit();
        editor.putInt(SharedPreferences_Names.UserId, UserId);
        editor.apply();
    }

    //در اینجا چک می شود که کاربر اکانت دارد یا خیر
    public boolean HasAccount() {
        SharedPreferences editor = context.getSharedPreferences(SharedPreferences_Names.MyPREFERENCES, MODE_PRIVATE);
        int UserId = editor.getInt(SharedPreferences_Names.UserId, 0);

        return UserId > 0;

    }

    //در اینجا آی دی کاربر برگشت داده می شود
    public int GetUserId(){
        SharedPreferences editor = context.getSharedPreferences(SharedPreferences_Names.MyPREFERENCES, MODE_PRIVATE);
        int UserId = editor.getInt(SharedPreferences_Names.UserId, 0);

        return UserId;
    }
}
