package ir.tdaapp.mms.Model.Repositorys.DataBase;

import android.content.Context;
import android.content.SharedPreferences;

import ir.tdaapp.mms.Model.Utilitys.SharedPreferences_Names;

import static android.content.Context.MODE_PRIVATE;

public class Tbl_Role {
    Context context;

    public Tbl_Role(Context context) {
        this.context = context;
    }

    //در اینجا نقش کاربر ثبت می شود
    public void AddRole(int RoleId){
        SharedPreferences.Editor editor = context.getSharedPreferences(SharedPreferences_Names.MyPREFERENCES, MODE_PRIVATE).edit();
        editor.putInt(SharedPreferences_Names.RoleId, RoleId);
        editor.apply();
    }

    //در اینجا چک می کند که کاربر نقش دارد یا خیر
    public boolean HasRole(){
        SharedPreferences editor = context.getSharedPreferences(SharedPreferences_Names.MyPREFERENCES, MODE_PRIVATE);
        int RoleId = editor.getInt(SharedPreferences_Names.RoleId, 0);

        return RoleId > 0;
    }

    //در اینجا آی دی نقش کاربر برگشت داده می شود
    public int GetRoleId(){
        SharedPreferences editor = context.getSharedPreferences(SharedPreferences_Names.MyPREFERENCES, MODE_PRIVATE);
        int RoleId = editor.getInt(SharedPreferences_Names.RoleId, 0);

        return RoleId;
    }
}
