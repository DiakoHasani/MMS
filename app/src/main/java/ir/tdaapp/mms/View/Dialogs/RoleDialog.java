package ir.tdaapp.mms.View.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ir.tdaapp.li_volley.Enum.ResaultCode;
import ir.tdaapp.mms.Model.Services.S_RoleDialog;
import ir.tdaapp.mms.Model.Utilitys.BaseDialogFragment;
import ir.tdaapp.mms.Model.ViewModels.VM_Role;
import ir.tdaapp.mms.Presenter.P_RoleDialog;
import ir.tdaapp.mms.R;

public class RoleDialog extends BaseDialogFragment implements View.OnClickListener, S_RoleDialog,
        CompoundButton.OnCheckedChangeListener {

    public final static String TAG = "RoleDialog";

    Button btn_Cancel, btn_Select;
    RadioButton rdo_0, rdo_1, rdo_2, rdo_3, rdo_4, rdo_5, rdo_6, rdo_7;
    ScrollView Items;
    ImageView Reload;
    ProgressBar Loading;
    P_RoleDialog p_roleDialog;
    int Index = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.role_dialog, container, false);

        FindItem(view);
        Implements();

        p_roleDialog.Start();

        return view;
    }

    void FindItem(View view) {
        btn_Cancel = view.findViewById(R.id.btn_Cancel);
        btn_Select = view.findViewById(R.id.btn_Select);
        rdo_0 = view.findViewById(R.id.rdo_0);
        rdo_1 = view.findViewById(R.id.rdo_1);
        rdo_2 = view.findViewById(R.id.rdo_2);
        rdo_3 = view.findViewById(R.id.rdo_3);
        rdo_4 = view.findViewById(R.id.rdo_4);
        rdo_5 = view.findViewById(R.id.rdo_5);
        rdo_6 = view.findViewById(R.id.rdo_6);
        rdo_7 = view.findViewById(R.id.rdo_7);
        Items = view.findViewById(R.id.Items);
        Reload = view.findViewById(R.id.Reload);
        Loading = view.findViewById(R.id.Loading);
    }

    void Implements() {
        btn_Cancel.setOnClickListener(this);
        btn_Select.setOnClickListener(this);
        Reload.setOnClickListener(this);

        rdo_0.setOnCheckedChangeListener(this);
        rdo_1.setOnCheckedChangeListener(this);
        rdo_2.setOnCheckedChangeListener(this);
        rdo_3.setOnCheckedChangeListener(this);
        rdo_4.setOnCheckedChangeListener(this);
        rdo_5.setOnCheckedChangeListener(this);
        rdo_6.setOnCheckedChangeListener(this);
        rdo_7.setOnCheckedChangeListener(this);

        p_roleDialog = new P_RoleDialog(getContext(), this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Cancel:
                getDialog().dismiss();
                break;
            case R.id.btn_Select:
                SelectRole();
                break;
            case R.id.Reload:
                p_roleDialog.Start();
                break;
        }
    }

    //در اینجا بر اساس رادیو باتن انتخاب شده شماره آرایه پرزنتر را به دست می آوریم و اقدام به ذخیره کردن نقش می کند
    void SelectRole() {

        int Checked = 0;

        if (rdo_0.isChecked())
            Checked = 0;

        if (rdo_1.isChecked())
            Checked = 1;

        if (rdo_2.isChecked())
            Checked = 2;

        if (rdo_3.isChecked())
            Checked = 3;

        if (rdo_4.isChecked())
            Checked = 4;

        if (rdo_5.isChecked())
            Checked = 5;

        if (rdo_6.isChecked())
            Checked = 6;

        if (rdo_7.isChecked())
            Checked = 7;

        p_roleDialog.SaveRole(Checked);
    }

    //زمانی که شروع به گرفتن داده ها بکند متد زیر فراخوانی می شود
    @Override
    public void OnStart() {
        SetCheckedRadioButtons();
        btn_Select.setEnabled(false);
        btn_Select.setTextColor(getResources().getColor(R.color.colorGray));
    }

    //در اینجا برنامه در حالت لودینگ قرار می گیرد
    @Override
    public void onLoading(boolean isLoad) {
        if (isLoad) {
            Loading.setVisibility(View.VISIBLE);
        } else {
            Loading.setVisibility(View.GONE);
        }
    }

    //در اینجا تمامی آیتم ها مخفی می شوند
    @Override
    public void onHideAll() {
        Loading.setVisibility(View.GONE);
        Reload.setVisibility(View.GONE);
        Items.setVisibility(View.GONE);
    }

    //اگر در گرفتن داده ها خطای رخ دهد متد زیر فراخوانی می شود
    @Override
    public void onError(ResaultCode resaultCode) {

        Reload.setVisibility(View.VISIBLE);

        switch (resaultCode) {
            case NetworkError:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.Please_Check_Your_Internet),
                        Toast.LENGTH_SHORT).show();
                break;
            case TimeoutError:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.Your_Internet_Speed_Is_Very_Slow),
                        Toast.LENGTH_SHORT).show();
                break;
            case ServerError:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.An_Error_Has_Occurred_On_The_Server),
                        Toast.LENGTH_SHORT).show();
                break;
            case ParseError:
            case Error:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.There_Was_an_Error_In_The_Application),
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }

    // زمانی که گرفتن داده ها با موفقیت انجام شود متد زیر فراخوانی می شود
    @Override
    public void onSuccess() {
        Items.setVisibility(View.VISIBLE);
    }

    //در اینجا رادیو باتن ها نمایش داده می شوند
    @Override
    public void onSetRole(VM_Role role) {
        switch (Index++) {
            case 0:
                rdo_0.setText(role.getTitle());
                rdo_0.setVisibility(View.VISIBLE);
                break;
            case 1:
                rdo_1.setText(role.getTitle());
                rdo_1.setVisibility(View.VISIBLE);
                break;
            case 2:
                rdo_2.setText(role.getTitle());
                rdo_2.setVisibility(View.VISIBLE);
                break;
            case 3:
                rdo_3.setText(role.getTitle());
                rdo_3.setVisibility(View.VISIBLE);
                break;
            case 4:
                rdo_4.setText(role.getTitle());
                rdo_4.setVisibility(View.VISIBLE);
                break;
            case 5:
                rdo_5.setText(role.getTitle());
                rdo_5.setVisibility(View.VISIBLE);
                break;
            case 6:
                rdo_6.setText(role.getTitle());
                rdo_6.setVisibility(View.VISIBLE);
                break;
            case 7:
                rdo_7.setText(role.getTitle());
                rdo_7.setVisibility(View.VISIBLE);
                break;
        }
    }

    //زمانی که عملیات تمام شود متد زیر فراخوانی می شود
    @Override
    public void onFinish() {

    }

    //در اینجا نقش فعلی کاربر را تیک دار می کند
    @Override
    public void onCheckedMyRole(int checked) {
        switch (checked){
            case 0:
                rdo_0.setChecked(true);
                break;
            case 1:
                rdo_1.setChecked(true);
                break;
            case 2:
                rdo_2.setChecked(true);
                break;
            case 3:
                rdo_3.setChecked(true);
                break;
            case 4:
                rdo_4.setChecked(true);
                break;
            case 5:
                rdo_5.setChecked(true);
                break;
            case 6:
                rdo_6.setChecked(true);
                break;
            case 7:
                rdo_7.setChecked(true);
                break;
        }
    }

    //در اینجا تیک رادیو باتن ها برداشته می شود
    void SetCheckedRadioButtons() {
        rdo_0.setChecked(false);
        rdo_1.setChecked(false);
        rdo_2.setChecked(false);
        rdo_3.setChecked(false);
        rdo_4.setChecked(false);
        rdo_5.setChecked(false);
        rdo_6.setChecked(false);
        rdo_7.setChecked(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            if (!btn_Select.isEnabled()) {
                btn_Select.setEnabled(true);
                btn_Select.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        p_roleDialog.GetDisposables(TAG).dispose();
    }
}
