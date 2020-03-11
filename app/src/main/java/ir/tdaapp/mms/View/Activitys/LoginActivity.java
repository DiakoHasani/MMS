package ir.tdaapp.mms.View.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import ir.tdaapp.li_utility.Codes.Replace;
import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.Services.S_Login;
import ir.tdaapp.mms.Model.ViewModels.VM_Login;
import ir.tdaapp.mms.Model.ViewModels.VM_Message;
import ir.tdaapp.mms.Presenter.P_Login;
import ir.tdaapp.mms.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, S_Login {

    ShimmerFrameLayout Loading;
    P_Login p_login;
    TextInputEditText txt_UserName, txt_Password;
    CardView btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FindItem();
        Implements();
    }

    void FindItem() {
        Loading = findViewById(R.id.Loading);
        txt_UserName = findViewById(R.id.txt_UserName);
        txt_Password = findViewById(R.id.txt_Password);
        btn_Login = findViewById(R.id.btn_Login);
    }

    void Implements() {
        p_login = new P_Login(getApplicationContext(), this);
        btn_Login.setOnClickListener(this);
    }

    //مربوط به رویداد کلیک
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Login:
                p_login.CheckValidation(getApplicationContext(), txt_UserName, txt_Password);
                break;
        }
    }

    //در اینجا حالت لودینگ صفحه ست می شود
    @Override
    public void Loading(boolean IsLoading) {
        SetLoding(IsLoading);
    }

    //زمانی که مقادیر ادیت تکست ها ولید نباشند متد زیر فراخوانی می شود
    @Override
    public void NotValid() {
        Toast.makeText(this, getResources().getString(R.string.Please_be_Careful_in_Entering_Information), Toast.LENGTH_SHORT).show();
    }

    //در اینجا زمانی که مقادیر تکست باکس ولید باشند متد زیر فراخوانی می شوند
    @Override
    public void IsValid() {
        VM_Login vm_login = new VM_Login();
        vm_login.setUserName(Replace.Number_fn_To_en(txt_UserName.getText().toString()));
        vm_login.setPassword(Replace.Number_fn_To_en(txt_Password.getText().toString()));

        p_login.StartLogin(vm_login);
    }

    //اگر لاگین با موفقیت انجام شود
    @Override
    public void IsSuccess(int CountRole, int UserId) {
        //اگر کاربر بیشتر از یک نقش داشته باشد آن را به صفحه اصلی فرستاده تا در آنجا نقش خود را انتخاب نماید
        if (CountRole > 1) {
            startActivity(new Intent(this, CentralActivity.class));
            finish();
        }
        //اگر یک نقش داشته باشد به صورت اتومات آن نقش را گرفته و در حافظه ذخیر می کند
        else {
            p_login.GetRoles(UserId);
        }
    }

    //اگر لاگین با موفقیت انجام نشود
    @Override
    public void NotSuccess(VM_Message message) {
        Toast.makeText(getApplicationContext(), message.getMessageText(), Toast.LENGTH_SHORT).show();
    }

    //اگر خطای در عملیات رخ دهد
    @Override
    public void OnError(ResaultCode resault) {
        switch (resault) {
            case NetworkError:
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.Please_Check_Your_Internet),
                        Toast.LENGTH_SHORT).show();
                break;
            case TimeoutError:
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.Your_Internet_Speed_Is_Very_Slow),
                        Toast.LENGTH_SHORT).show();
                break;
            case ServerError:
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.An_Error_Has_Occurred_On_The_Server),
                        Toast.LENGTH_SHORT).show();
                break;
            case ParseError:
            case Error:
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.There_Was_an_Error_In_The_Application),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //در اینجا زمانی که درج نقش ها با موفقیت انجام شود این متد فراخوانی می شود
    @Override
    public void OnSuccessGetRole() {
        startActivity(new Intent(this, CentralActivity.class));
        finish();
    }

    //در اینجا اگر درج نقش با مشکل مواجه شود متد زیر فراخوانی می شود
    @Override
    public void OnErrorGetRole(ResaultCode resault) {
        switch (resault) {
            case NetworkError:
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.Please_Check_Your_Internet),
                        Toast.LENGTH_SHORT).show();
                break;
            case TimeoutError:
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.Your_Internet_Speed_Is_Very_Slow),
                        Toast.LENGTH_SHORT).show();
                break;
            case ServerError:
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.An_Error_Has_Occurred_On_The_Server),
                        Toast.LENGTH_SHORT).show();
                break;
            case ParseError:
            case Error:
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.YourLoginWas_Successful_But_There_Was_a_problemRegistering_Your_Role),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //در اینجا قرار گرفتن صفحه در حالت انتظار ست می شود
    private void SetLoding(boolean load) {
        if (load) {
            Loading.startShimmerAnimation();
            btn_Login.setEnabled(false);
        } else {
            Loading.stopShimmerAnimation();
            btn_Login.setEnabled(true);
        }
    }
}
