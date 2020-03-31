package ir.tdaapp.mms.View.Fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import ir.tdaapp.mms.Model.Enums.BottomBarItem;
import ir.tdaapp.mms.Model.Services.S_Text_OnlyEditTextFragment;
import ir.tdaapp.mms.Model.Utilitys.BaseFragment;
import ir.tdaapp.mms.Model.Utilitys.KeyBoard;
import ir.tdaapp.mms.R;
import ir.tdaapp.mms.View.Activitys.CentralActivity;

//این صفحه برای زمانی است که کاربر روی بزرگنمایی ادیت تکست در صفحه افزودن درخواست کلیک کرد این صفحه باز می شود که دارای ادیت تکست بزرگتری است
public class OnlyEditTextFragment extends BaseFragment implements View.OnClickListener {

    S_Text_OnlyEditTextFragment s_text_onlyEditTextFragment;
    TextView txt_Text;
    RelativeLayout btn_AddRequest;
    String text;
    Toolbar toolBar;

    public OnlyEditTextFragment(String text) {
        this.text = text;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.only_edit_text_fragment, container, false);

        findItem(view);
        implement();
        setToolbar();

        KeyBoard.hideKeyboard(getActivity());

        txt_Text.setText(text);

        return view;
    }

    void findItem(View view) {
        txt_Text = view.findViewById(R.id.txt_Text);
        btn_AddRequest = view.findViewById(R.id.btn_AddRequest);
        toolBar = view.findViewById(R.id.toolBar);
    }

    void implement() {
        btn_AddRequest.setOnClickListener(this);
    }

    void setToolbar() {

        toolBar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolBar.setTitle(getContext().getResources().getString(R.string.RequestText));
        ((CentralActivity) getActivity()).setSupportActionBar(toolBar);
        ((CentralActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        toolBar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        setHasOptionsMenu(true);
        ((CentralActivity) getActivity()).SetEnableDrawer(BottomBarItem.Request);
    }

    public void setS_Text_OnlyEditTextFragment(S_Text_OnlyEditTextFragment s_text_onlyEditTextFragment) {
        this.s_text_onlyEditTextFragment = s_text_onlyEditTextFragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_AddRequest:
                s_text_onlyEditTextFragment.getText(txt_Text.getText().toString());
                getActivity().onBackPressed();
                break;
        }
    }
}
